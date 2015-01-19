package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WolfGame implements Runnable {
	private final Integer MIN_PLAYERS = 5;
	
	private List<Role> roles;
	private List<Player> players;
	private Map<Player, Role> playerRoles;
	
	private Map<Player, Player> votes;
	private Map<Player, Integer> voteCounts;
	
	private GameState state;

	public WolfGame(List<Role> roles) {
		this.roles = roles;
		this.players = new ArrayList<Player>();
		this.playerRoles = new HashMap<Player, Role>();
		
		this.votes = new HashMap<Player, Player>();
		this.voteCounts = new HashMap<Player, Integer>();
		
		this.state = GameState.STARTING;
	}
	
	public void add(Player player) {
		if (state != GameState.STARTING) {
			throw new RuntimeException("The game has already started");
		}
			
		players.add(player);
	}
	
	public void run() {
		
		state = GameState.INIT;
		
		
		System.out.println("Welcome to AI warewolf");
		System.out.println("Players are: "+players);
		
		assignRoles();
		notifyRoles();
		state = GameState.DAYTIME;
		
		while(state != GameState.GAMEOVER) {
			
			boolean wolfWin = didWolvesWin();
			if (!wolfWin) {
				Player lynch = doDaytime();
				if (lynch == null) {
					System.out.println("The villagers decide not to lynch anyone today");
				} else {
					System.out.println("The villages decide to lynch "+lynch+" who turns out to be a "+playerRoles.get(lynch));
					players.remove(lynch);
				}
			} else {
				state = GameState.GAMEOVER;
			}
			
			boolean villagerWin = didVillagersWin();
			if (!villagerWin) {
				Player eaten = doNighttime();
				if (eaten == null) {
					System.out.println("The villagers wake and find no one was eaten");
				} else {
					System.out.println("The villages wake to find "+eaten+" dead, who turns out to be a "+playerRoles.get(eaten));
					players.remove(eaten);
				}
			} else {
				state = GameState.GAMEOVER;
			}

		}
		
		System.out.println("Game over, winners are: "+getWinningTeam());
	}
	
	public synchronized Player doDaytime() {
		state = GameState.DAYTIME;
		voteCounts.clear();
		votes.clear();
		
		System.out.println("[GAME] It is now daytime");
		System.out.println("[GAME] alive players are: "+players);
		
		for (Player player : players) {
			player.notifyDaytime(new DaytimePlayerController(player, this));
		}
		
		//TODO block until vote
		Player votedFor = null;
	    while(votes.size() != players.size()) {
	        try {
	            wait();
	        } catch (InterruptedException e) {}
    		votedFor = getHighestVote(players.size()/2);
	    }
		
		return votedFor;
	}
	
	public synchronized Player doNighttime() {
		state = GameState.NIGHTTIME;
		voteCounts.clear();
		votes.clear();
		
		System.out.println("[GAME] It is now night time");
		System.out.println("[GAME] alive players are: "+players);
		
		for (Player player : players) {
			player.notifyNighttime(new NighttimePlayerController(player, this));
		}
		
		//TODO block until vote
		Player votedFor = null;
	    while(votedFor == null) {
	        try {
	            wait();
	        } catch (InterruptedException e) {}
    		votedFor = getHighestVote(1);
	    }
		
		return votedFor;
	}
	
	public void notifyRoles() {
		Iterator<Entry<Player, Role>> itr = playerRoles.entrySet().iterator();
		
		while(itr.hasNext()) {
			Entry<Player, Role> entry = itr.next();
			Player p = entry.getKey();
			Role r = entry.getValue();
			
			p.notifyRole(p, r);
		}
	}
	
	public Team getWinningTeam() {
		
		int numWolves = 0;
		int numVillagers = 0;
		
		Iterator<Entry<Player, Role>> itr = playerRoles.entrySet().iterator();
		
		while(itr.hasNext()) {
			Entry<Player, Role> entry = itr.next();
			if (entry.getValue() == Role.VILLAGER || entry.getValue() == Role.SEER) {
				numVillagers++;
			}
			
			if (entry.getValue() == Role.WOLF) {
				numWolves++;
			}
		}
		
		if (numWolves >= numVillagers) {
			return Team.WOLVES;
		}
		
		if (numWolves == 0) {
			return Team.VILLAGERS;
		}
		
		return null;
	}
	
	
	private void assignRoles() {
		if (players.size() < MIN_PLAYERS) {
			throw new RuntimeException("not enouph players");
		}
		
		List<Player> playersLeft = new ArrayList<Player>(players);
		Collections.shuffle(playersLeft);
		
		for(int i=0; i<playersLeft.size(); i++) {
			Player p = playersLeft.get(i);
			Role r = roles.get(i);
			
			playerRoles.put(p, r);
		}
		
	}
	
	public void enterVote(Player voter, Player candidate) {
		if (votes.containsKey(voter)) {
			Player oldVote = votes.get(voter);
			Integer oldVoteCount = voteCounts.get(oldVote);
			voteCounts.put(oldVote, oldVoteCount-1);
		}
		
		votes.put(voter, candidate);
		Integer votes = voteCounts.get(candidate);
		if (votes == null) {
			votes = 0;
		}
		
		voteCounts.put(candidate, votes + 1);
		
		System.out.println("[GAME] "+voter+" voted for "+candidate);
		notifyAll();
	}
	
	private boolean didVillagersWin() {
		int numWolves = 0;
		
		Iterator<Entry<Player, Role>> itr = playerRoles.entrySet().iterator();
		
		while(itr.hasNext()) {
			Entry<Player, Role> entry = itr.next();
			
			if (entry.getValue() == Role.WOLF) {
				numWolves++;
			}
		}
		
		return numWolves == 0;
	}
	
	private boolean didWolvesWin() {
		int numWolves = 0;
		int numVillagers = 0;
		
		Iterator<Entry<Player, Role>> itr = playerRoles.entrySet().iterator();
		
		while(itr.hasNext()) {
			Entry<Player, Role> entry = itr.next();
			if (entry.getValue() == Role.VILLAGER || entry.getValue() == Role.SEER) {
				numVillagers++;
			}
			
			if (entry.getValue() == Role.WOLF) {
				numWolves++;
			}
		}
		
		return numWolves > numVillagers;
	}
	

	private Player getHighestVote(int minVotes) {
		Player highest = null;
		Integer count = 0;
		
		System.out.println("minVotes: "+minVotes);
		for (Player player : players) {
			Integer votes = voteCounts.get(player);
			if (votes != null && votes > count) {
				highest = player;
				count = votes;
			}
		}
		
		System.out.println("count: "+count);
		if (count >= minVotes) {
			return highest;
		} else {
			return null;
		}
	}
	
	public Role getPlayerRole(Player player) {
		return playerRoles.get(player);
	}
	
	public synchronized void timeout() {
		
		System.out.println("admin caused timeout");
		
		for (Player p : players) {
			if (!votes.containsKey(p)) {
				votes.put(p, null);
			}
		}
		
		notifyAll();
	}

	public List<Player> getAlivePlayers() {
		return Collections.unmodifiableList(players);
	}

	public Player getPlayerByName(String name) {
		for (Player player : players) {
			if (name.equals(player.getName())) {
				return player;
			}
		}

		return null;
	}

}
