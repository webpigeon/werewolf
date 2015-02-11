package uk.me.webpigeon.wolf.newcode;

import java.util.Collection;
import java.util.Queue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.action.ActionI;

public interface GameListener {

	void onDiscoverRole(String player, RoleI role);

	void onJoin(String name, Queue<ActionI> actionQueue);

	void onStateChange(GameState newState);

	void onGameStart(Collection<String> players);

	void onMessage(String player, String message, String channel);

	void onDeath(String player, String cause);

	void onVoteEntered(String voter, String candidate);

}
