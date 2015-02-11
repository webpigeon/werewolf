package uk.me.webpigeon.wolf.newcode.legacy;

import java.util.Collection;
import java.util.Queue;

import uk.me.webpigeon.wolf.GameObserver;
import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.newcode.GameListener;

public class LegacyWrapper implements GameListener {
	private GameObserver player;
	private LegacyController c;
	
	public LegacyWrapper(GameObserver gameObserver, LegacyController c) {
		this.player = gameObserver;
		this.c = c;
	}

	@Override
	public void onGameStart(Collection<String> players) {
		
	}

	@Override
	public void onJoin(String name, Queue<ActionI> actionQueue) {
		c.setName(name);
		player.bind(c);
	}

	@Override
	public void onDiscoverRole(String playerName, RoleI role) {
		player.notifyRole(playerName, role);
	}

	@Override
	public void onStateChange(GameState newState) {
		switch (newState) {
			case DAYTIME:
				player.notifyDaytime(null);
			case NIGHTTIME:
				player.notifyNighttime(null);
			default:
				// player does not deal with any other cases
		}
		
	}

	@Override
	public void onMessage(String playerName, String message, String channel) {
		if ("public".equals(channel)) {
			player.notifyMessage(playerName, message);
		}
	}

	@Override
	public void onDeath(String victim, String cause) {
		player.notifyDeath(victim, cause);
	}

	@Override
	public void onVoteEntered(String voter, String candidate) {
		player.notifyVote(voter, candidate);
	}

}
