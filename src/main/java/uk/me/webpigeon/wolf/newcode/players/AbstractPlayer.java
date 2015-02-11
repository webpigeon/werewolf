package uk.me.webpigeon.wolf.newcode.players;

import java.util.Collection;
import java.util.Queue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.newcode.GameListener;

/**
 * A basis for players based around the new game engine.
 */
public abstract class AbstractPlayer implements Runnable, GameListener {
	
	private String name;
	private Queue<ActionI> actionQueue;
	private GameState state;
	private BeliefSystem system;
	private ActionI currentAction;
	
	public AbstractPlayer() {
		this.system = new BeliefSystem();
	}

	@Override
	public void onGameStart(Collection<String> players) {
		system.clear();
		system.setPlayers(players);
	}

	@Override
	public void onJoin(String name, Queue<ActionI> actionQueue) {		
		this.name = name;
		this.actionQueue = actionQueue;
	}

	@Override
	public void onDiscoverRole(String player, RoleI role) {
		system.recordRole(player, role.getName());	
	}

	@Override
	public void onStateChange(GameState newState) {
		this.state = newState;
	}

	@Override
	public void onMessage(String player, String message, String channel) {
		
	}

	@Override
	public void onDeath(String victim, String cause) {
		system.removePlayer(victim);
	}

	@Override
	public void onVoteEntered(String voter, String candidate) {
		
	}
	
	public abstract ActionI selectAction(BeliefSystem system);
	
	public void run() {
		
		while (!Thread.interrupted()) {
			ActionI action = selectAction(system);
			if (action != null && !action.equals(currentAction)) {
				System.out.println("performing action");
				actionQueue.add(action);
				currentAction = action;
			}
		}
		
	}
	
}
