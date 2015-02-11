package uk.me.webpigeon.wolf.newcode.players;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.events.EventI;

/**
 * A basis for players based around the new game engine.
 */
public abstract class AbstractPlayer implements Runnable, SessionManager {
	
	private String name;
	private WolfController controller;
	private BlockingQueue<EventI> eventQueue;
	private GameState state;
	private BeliefSystem system;
	private ActionI currentAction;
	
	public AbstractPlayer() {
		this.system = new BeliefSystem();
	}

	@Override
	public void bind(String name, WolfController controller, BlockingQueue<EventI> eventQueue) {		
		this.name = name;
		this.controller = controller;
		this.eventQueue = eventQueue;
	}
	
	public abstract ActionI selectAction(BeliefSystem system);
	
	public void run() {
		
		while (!Thread.interrupted()) {
			
			try {
				EventI event = eventQueue.take();
				//TODO parse events
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			ActionI action = selectAction(system);
			if (action != null && !action.equals(currentAction)) {
				controller.addTask(name, action);
				currentAction = action;
			}
		}
		
	}
	
}
