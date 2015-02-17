package uk.me.webpigeon.wolf.newcode.players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.me.webpigeon.wolf.newcode.events.ChatMessage;

public class BeliefSystem {
	private final Collection<String> alivePlayers;
	private final Map<String, String> roles;
	private final Pattern chatPattern;
	
	public String name;

	public BeliefSystem() {
		this.chatPattern = Pattern.compile("\\((\\w+),(\\w+),(\\w+)\\)");
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
	
	void setPlayerName(String name) {
		this.name = name;
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
			roles.put(t.object, t.subject);
		}
	}
	
	private class Tripple {
		public String object;
		public String verb;
		public String subject;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
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
			if (!getOuterType().equals(other.getOuterType()))
				return false;
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

		private BeliefSystem getOuterType() {
			return BeliefSystem.this;
		}
	}

	public boolean isAlive(String target) {
		return alivePlayers.contains(target);
	}
	
	public String getMyName() {
		return name;
	}
	
}
