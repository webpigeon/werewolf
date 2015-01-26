package uk.me.webpigeon.wolf;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BasicIntelligencePlayer extends RandomPlayer {

	public BasicIntelligencePlayer(String name) {
		super(name);
	}
	
	public void notifyDaytime(PlayerController controller) {	
		super.notifyNighttime(controller);
	}

	public void notifyNighttime(PlayerController controller) {
		super.notifyNighttime(controller);
		
	}
	
	@Override
	protected void takeAction(PlayerController controller) {
		Role myRole = getRole();
		if (myRole == Role.VILLAGER) {
			super.takeAction(controller);
			return;
		}
		
		String selected = selectPlayer(getScores(), controller.getPlayers());
		if (controller.getStage() == GameState.DAYTIME) {
			if (myRole == Role.SEER) {
				controller.takeAction("lynch "+selected);
			}
			
			if (myRole == Role.WOLF) {
				controller.takeAction("lynch "+selected);
			}
		} else {
			if (myRole == Role.SEER) {
				controller.takeAction("see "+selected);
			}
			
			if (myRole == Role.WOLF) {
				controller.takeAction("eat "+selected);
			}
		}
	}

	private String selectPlayer(Map<String, Integer> scores, List<Player> players) {
		Player bestPlayer = null;
		int bestScore = Integer.MIN_VALUE;
		
		for (Player player : players) {
			Integer score = scores.get(player.getName());
			if (score == null) {
				score = 0; //we don't know about this player
			}
			
			if (bestScore < score) {
				bestPlayer = player;
				bestScore = score;
			}
		}
		
		return bestPlayer.getName();
	}
	
	private Map<String, Integer> getScores() {
		Role myRole = getRole();
		Map<String, Integer> scores = new TreeMap<String, Integer>();
		
		for (Map.Entry<String, Role> entry : roles.entrySet()) {
			String name = entry.getKey();
			Role role = entry.getValue();
			
			if (myRole == Role.SEER) {
				scores.put(name, -5);
			}
			
			if (myRole == Role.WOLF) {
				if (role == Role.WOLF) {
					scores.put(name, -10);
				}
				
				if (role == Role.VILLAGER) {
					scores.put(name, 5);
				}
				
				if (role == Role.SEER) {
					scores.put(name, 10);
				}
			}
			
		}
		
		return scores;
	}

}
