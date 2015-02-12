package uk.me.webpigeon.wolf.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LifetimeStats {
	private Map<String, List<Integer>> deathTurn;
	private List<String> alivePlayers;
	
	private int turnID;
	
	public LifetimeStats() {
		this.deathTurn = new TreeMap<>();
		this.alivePlayers = new ArrayList<>();
		this.turnID = 0;
	}

	public void setDead(String player) {
		List<Integer> deathTurns = deathTurn.get(player);
		if (deathTurns == null) {
			deathTurns = new ArrayList<Integer>();
			deathTurn.put(player, deathTurns);
		}
		
		deathTurns.add(turnID);
		alivePlayers.remove(player);
	}
	
	public void resetGame(Collection<String> players) {
		alivePlayers.clear();
		alivePlayers.addAll(players);
		turnID = 0;
	}
	
	public void incrementTurn() {
		turnID++;
	}

	public Map<String, List<Integer>> getScores() {
		return Collections.unmodifiableMap(deathTurn);
	}
	
}
