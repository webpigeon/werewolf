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

	public synchronized void setDead(String player) {
		recordTurn(player, turnID);
		alivePlayers.remove(player);
	}
	
	public synchronized void resetGame(Collection<String> players) {
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
	
	public synchronized Map<String, Double> getAverageScore() {
		
		Map<String, Double> scores = new TreeMap<String, Double>();
		for (String player : deathTurn.keySet()) {
			double totals = 0;
			List<Integer> turnList = deathTurn.get(player);
			
			for (Integer turn : turnList) {
				totals += turn;
			}
			
			scores.put(player, totals/turnList.size());
		}
		
		return scores;
	}

	private void recordTurn(String player, int turn) {
		List<Integer> deathTurns = deathTurn.get(player);
		if (deathTurns == null) {
			deathTurns = new ArrayList<Integer>();
			deathTurn.put(player, deathTurns);
		}
		
		deathTurns.add(turnID);
	}
	
	public synchronized void gameOver(List<String> winners) {
		for (String player : alivePlayers) {
			recordTurn(player, turnID);
		}
		
	}
	
}
