package uk.me.webpigeon.wolf.newcode.players;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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

/**
 * A basis for players based around the new game engine.
 */
public abstract class AbstractPlayer implements Runnable, SessionManager {
	
	private String name;
	private RoleI role;
	
	private WolfController controller;
	private BlockingQueue<EventI> eventQueue;
	private GameState state;
	private BeliefSystem system;
	protected ActionI currentAction;
	
	public AbstractPlayer() {
		this.system = new BeliefSystem();
	}

	@Override
	public void bind(String name, WolfController controller, BlockingQueue<EventI> eventQueue) {		
		this.name = name;
		this.controller = controller;
		this.eventQueue = eventQueue;
		system.setPlayerName(name);
	}
	
	public abstract ActionI selectAction(BeliefSystem system);
	
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
				
				if (state == GameState.DAYTIME || state == GameState.NIGHTTIME) {
					ActionI action = selectAction(system);
					if (action != null && !action.equals(currentAction)) {
						controller.addTask(name, action);
						currentAction = action;
					}
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
				system.clear();
				GameStarted gs = (GameStarted)event;
				system.setPlayers(gs.players);
				break;
				
			case "stateChange":
				StateChanged sc = (StateChanged)event;
				state = sc.newState;
				clearBlocks();
				break;
				
			case "vote":
				PlayerVote pv = (PlayerVote)event;
				system.registerVote(pv.vote);
				//system.recordRole(pv.player, pv.vote);
				break;
				
			case "role":
				PlayerRole pr = (PlayerRole)event;
				system.recordRole(pr.name, pr.role.getName());
				if (name.equals(pr.name)){
					role = pr.role;
				}
				break;
		
			case "death":
				PlayerDeath pd = (PlayerDeath)event;
				system.recordRole(pd.player, pd.role);
				system.removePlayer(pd.player);
				break;
				
			case "chat":
				ChatMessage pc = (ChatMessage)event;
				system.parseChat(pc);
				break;
				
			default:
				System.err.println("unknown event "+event.getType());
		}
	}

	public boolean isState(GameState state) {
		return this.state.equals(state);
	}

	public boolean isRole(String role) {
		return role.equals(this.role.getName());
	}

	public String getName() {
		return name;
	}
	
	protected void clearBlocks() {
		currentAction = null;
		system.cleanVotes();
	}
	
}
