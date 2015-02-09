package uk.me.webpigeon.wolf.newcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.Team;

public class WolfModel {
	private Collection<String> players;
	private Map<String, RoleI> roleMapping;
	private Map<Team, Collection<String>> teams;
	
	public WolfModel() {
		this.players = new ArrayList<String>();
		this.roleMapping = new TreeMap<String, RoleI>();
		this.teams = new HashMap<Team, Collection<String>>();
	}

	public void addPlayer(String player) {
		players.add(player);
	}
	
	public void removePlayer(String player) {
		players.remove(player);
		roleMapping.remove(player);
		
		for (Collection<String> playerList : teams.values()) {
			playerList.remove(player);
		}
	}
	
	public Map<String, RoleI> assignRoles(Collection<RoleI> roles, RoleI defaultRole) {
		List<String> playerList = new ArrayList<String>(players);
		Collections.shuffle(playerList);
		
		List<RoleI> roleList = new ArrayList<RoleI>(roles);
		
		Iterator<RoleI> roleItr = roleList.iterator();
		
		for (String player : playerList) {
			RoleI role = defaultRole;
			if (roleItr.hasNext()) {
				role = roleItr.next();
			}
			
			assignRole(player, role);
		}

		return Collections.unmodifiableMap(roleMapping);
	}

	private void assignRole(String player, RoleI role) {
		roleMapping.put(player, role);
		for (Team team : role.getTeams()) {
			assignTeam(player, team);
		}
	}
	
	private void assignTeam(String player, Team team) {
		Collection<String> playerTeam = teams.get(team);
		if (playerTeam == null) {
			playerTeam = new ArrayList<String>();
			teams.put(team, playerTeam);
		}
		
		playerTeam.add(player);
	}

	public boolean isAlivePlayer(String victim) {
		return players.contains(victim);
	}

	public RoleI getRole(String player) {
		assert player != null;
		assert roleMapping.containsKey(player);
		
		return roleMapping.get(player);
	}

	public Collection<String> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	public boolean isGameOver() {
		Collection<String> wolves = teams.get(Team.WOLVES);
		Collection<String> villagers = teams.get(Team.VILLAGERS);
		
		return wolves.isEmpty() || wolves.size() >= villagers.size();
	}

}
