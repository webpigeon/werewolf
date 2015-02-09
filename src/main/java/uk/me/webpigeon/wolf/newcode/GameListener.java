package uk.me.webpigeon.wolf.newcode;

import java.util.Collection;
import java.util.Queue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.action.ActionI;

public interface GameListener {

	void onGameStart(Collection<String> players);
	void onJoin(String name, Queue<ActionI> actionQueue);
	
	void onDiscoverRole(String player, RoleI role);
	void onStateChange(GameState newState);
	void onMessage(String player, String message, String channel);
	void onDeath(String victim, String cause);

}
