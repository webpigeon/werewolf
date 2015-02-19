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
public class SelectionPlayer implements Runnable, SessionManager {
	
	private String name;

	private WolfController controller;
	private BlockingQueue<EventI> eventQueue;
	private BeliefSystem system;
	private SelectionStrategy strategy;
	
	protected ActionI currentAction;
	
	public SelectionPlayer(SelectionStrategy strat, BeliefSystem system) {
		this.strategy = strat;
		this.system = system;
	}

	@Override
	public void bind(String name, WolfController controller, BlockingQueue<EventI> eventQueue) {		
		this.name = name;
		this.controller = controller;
		this.eventQueue = eventQueue;
		system.setPlayerName(name);
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
				
				ActionI action = strategy.selectAction(system);
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
				system.clear();
				GameStarted gs = (GameStarted)event;
				system.setPlayers(gs.players);
				break;
				
			case "stateChange":
				StateChanged sc = (StateChanged)event;
				system.setState(sc.newState);
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
					system.setRole(pr.role);
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

	public String getName() {
		return name;
	}
	
	protected void clearBlocks() {
		currentAction = null;
		system.cleanVotes();
	}
	
}
