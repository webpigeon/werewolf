package uk.me.webpigeon.wolf.newcode.players;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.events.ChatMessage;
import uk.me.webpigeon.wolf.newcode.events.EventI;
import uk.me.webpigeon.wolf.newcode.events.GameStarted;
import uk.me.webpigeon.wolf.newcode.events.PlayerDeath;
import uk.me.webpigeon.wolf.newcode.events.PlayerRole;
import uk.me.webpigeon.wolf.newcode.events.PlayerVote;
import uk.me.webpigeon.wolf.newcode.events.StateChanged;
import uk.me.webpigeon.wolf.newcode.players.behavours.Facts;

/**
 * A basis for players based around the new game engine.
 */
public class SelectionPlayer implements Runnable, SessionManager {
	
	private String name;

	private final Pattern chatPattern;
	private final SelectionStrategy strategy;
	
	private WolfController controller;
	private BlockingQueue<EventI> eventQueue;
	private FactBase beliefs;
	
	protected ActionI currentAction;
	
	public SelectionPlayer(SelectionStrategy strat, FactBase beliefs) {
		this.chatPattern = Pattern.compile("\\((\\w+),(\\w+),(\\w+)\\)");
		this.strategy = strat;
		this.beliefs = beliefs;
	}

	@Override
	public void bind(String name, WolfController controller, BlockingQueue<EventI> eventQueue) {		
		this.name = name;
		this.controller = controller;
		this.eventQueue = eventQueue;
	}
	
	public void run() {
		
		while (!Thread.interrupted()) {
			if (eventQueue == null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				EventI event = eventQueue.poll(5, TimeUnit.SECONDS);
				if (event != null) {
					processEvent(event);
				}
				
				ActionI action = strategy.selectAction(beliefs);
				if (action != null && !action.equals(currentAction)) {
					controller.addTask(name, action);
					currentAction = action;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
	
	private void processEvent(EventI event) {
		switch(event.getType()) {
			case "gameStarted":
				beliefs.reset();
				beliefs.storeFact(Facts.AGENT_NAME, name);
				beliefs.storeFact(Facts.GAME_ID, "wolf-"+System.nanoTime());
				GameStarted gs = (GameStarted)event;
				for (String player : gs.players) {
					beliefs.storeFact(Facts.ALIVE_PLAYERS, player);
				}
				break;
				
			case "stateChange":
				StateChanged sc = (StateChanged)event;
				beliefs.removeFact(Facts.GAME_STATE);
				beliefs.storeFact(Facts.GAME_STATE, sc.newState.name());
				clearBlocks();
				break;
				
			case "vote":
				PlayerVote pv = (PlayerVote)event;
				//TODO cleanup changed votes
				//TODO record (voter,votee) pairs
				beliefs.storeFact(Facts.CURRENT_VOTES, pv.vote);
				//system.recordRole(pv.player, pv.vote);
				break;
				
			case "role":
				PlayerRole pr = (PlayerRole)event;
				
				String roleFactName = String.format(Facts.PLAYER_ROLE, pr.name);
				beliefs.storeFact(roleFactName, pr.role.getName());
				if (name.equals(pr.name)){
					beliefs.storeFact(Facts.AGENT_ROLE, pr.role.getName());
				}
				break;
		
			case "death":
				PlayerDeath pd = (PlayerDeath)event;
				String deathRoleFactName = String.format(Facts.PLAYER_DEATH_ROLE, pd.player);
				beliefs.storeFact(deathRoleFactName, pd.role);
				beliefs.removeFact(Facts.ALIVE_PLAYERS, pd.player);
				beliefs.storeFact(Facts.DEAD_PLAYERS, pd.player);
				break;
				
			case "chat":
				ChatMessage pc = (ChatMessage)event;
				List<String> untrustworthyPlayers = beliefs.getValues(Facts.UNTRUSTWORTHY_PLAYERS);
				if (!untrustworthyPlayers.contains(pc.player)) {
					parseChat(pc);	
				}
				break;
				
			default:
				System.err.println("unknown event "+event.getType());
		}
	}

	public String getName() {
		return name;
	}
	
	protected void clearBlocks() {
		currentAction = null;
		beliefs.removeFact(Facts.CURRENT_VOTES);
		
		strategy.announceNewTurn();
	}
	
	public void parseChat(ChatMessage pc) {
		Matcher m = chatPattern.matcher(pc.message);
		if (!m.matches()) {
			return;
		}
		
		Tripple t = new Tripple();
		t.object = m.group(1);
		t.verb = m.group(2);
		t.subject = m.group(3);

		if ("role".equals(t.verb)) {
			String roleFactName = String.format(Facts.PLAYER_ROLE, t.object);
			beliefs.storeFact(roleFactName, t.subject);
		}
		
		// store what they said in the list of things they said
		String saidFactString = String.format(Facts.PLAYER_MESSAGES, pc.player);
		beliefs.storeFact(saidFactString, t.toStore());
	}
	
	private class Tripple {
		public String object;
		public String verb;
		public String subject;
		
		public String toStore() {
			return String.format("%s %s %s",object,verb,subject);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((object == null) ? 0 : object.hashCode());
			result = prime * result
					+ ((subject == null) ? 0 : subject.hashCode());
			result = prime * result + ((verb == null) ? 0 : verb.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tripple other = (Tripple) obj;
			if (object == null) {
				if (other.object != null)
					return false;
			} else if (!object.equals(other.object))
				return false;
			if (subject == null) {
				if (other.subject != null)
					return false;
			} else if (!subject.equals(other.subject))
				return false;
			if (verb == null) {
				if (other.verb != null)
					return false;
			} else if (!verb.equals(other.verb))
				return false;
			return true;
		}

		public String serialise() {
			return String.format("(%s,%s,%s)", object, verb, subject);
		}
		
		public String toString() {
			return serialise();
		}

	}
	
}
