package uk.me.webpigeon.wolf.graphs;

import java.util.Collection;
import java.util.Queue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.GameListener;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;

public class StatsTracker implements GameListener {
	private LifetimeStats stats;
	
	public StatsTracker(LifetimeStats stats) {
		this.stats = stats;
	}

	@Override
	public void onDiscoverRole(String player, RoleI role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoin(String name, Queue<ActionI> actionQueue) {
		
	}

	@Override
	public void onStateChange(GameState newState) {
		if (newState == GameState.DAYTIME || newState == GameState.NIGHTTIME) {
			System.out.printf("[TURN] Turn changed, it is now %s\n", newState);
			stats.incrementTurn();
		} else if (newState == GameState.GAMEOVER) {
			//TODO reward players for surviving till the end
			System.out.println("game over: "+stats.getScores());
		}
		
	}

	@Override
	public void onGameStart(Collection<String> players) {
		stats.resetGame(players);
		System.out.println("game started: "+players);
	}

	@Override
	public void onMessage(String player, String message, String channel) {
		// TODO Auto-generated method stub
		System.out.printf("[MSG] <%s> %s\n", player, message);
	}

	@Override
	public void onVoteEntered(String voter, String candidate) {
		System.out.printf("[VOTE] %s voted for %s\n", voter, candidate);
	}

	@Override
	public void onDeath(String player, String cause, String role) {
		stats.setDead(player);
		System.out.printf("[DEATH] %s died of %s, they were a %s\n", player, cause, role);
	}

}
