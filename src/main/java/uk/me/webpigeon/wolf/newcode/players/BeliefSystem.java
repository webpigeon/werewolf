package uk.me.webpigeon.wolf.newcode.players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class BeliefSystem {
	private final Collection<String> alivePlayers;
	private final Map<String, String> roles;

	public BeliefSystem() {
		this.alivePlayers = new ArrayList<String>();
		this.roles = new TreeMap<String, String>();
	}
	
	/**
	 * Set the list of players to be only those passed in via the argument.
	 * 
	 * clears the list of players if it is not empty and then adds all players to the list.
	 * 
	 * @param players
	 */
	void setPlayers(Collection<String> players) {
		if (!players.isEmpty()) {
			alivePlayers.clear();
		}
		alivePlayers.addAll(players);
	}
	
	/**
	 * Wipe all data from the belief system
	 */
	public void clear() {
		alivePlayers.clear();
		roles.clear();
	}

	/**
	 * Record a players role.
	 * 
	 * @param player the player to record the role of
	 * @param role the role to record
	 */
	public void recordRole(String player, String role) {
		roles.put(player, role);
	}
	
	/**
	 * mark a player as dead
	 * @param player the player to remove
	 */
	public void removePlayer(String player) {
		assert player != null;
		assert alivePlayers.contains(player);
		alivePlayers.remove(player);
	}

	public Collection<String> getPlayers() {
		return Collections.unmodifiableCollection(alivePlayers);
	}

	public String getRole(String player) {
		return roles.get(player);
	}
	
}
