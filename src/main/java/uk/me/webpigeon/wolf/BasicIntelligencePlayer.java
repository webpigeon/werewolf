package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import uk.me.webpigeon.wolf.action.ActionI;

public class BasicIntelligencePlayer extends AbstractPlayer {
	private Map<String, Integer> baises;
	
	public BasicIntelligencePlayer() {
		this.baises = new TreeMap<String, Integer>();
	}
	
	public void notifyVote(String voter, String votee) {
		if (votee.equals(getName())) {
			Integer bias = baises.get(voter);
			if (bias == null) {
				bias = 0;
			}
			baises.put(voter, bias+1);
			think(voter +" voted for me :( ");
		}
		
		/*if (getRole() == Role.SEER) {
			Role voteeRole = roles.get(votee);
			if (voteeRole == Role.VILLAGER) {
				controller.talk(voter+": "+votee+" is a villager");
			}
		}*/
		
	}
	
	public void notifyMessage(String who, String message){
		
	}
	
	@Override
	protected void takeAction(Collection<ActionI> legalActions) {		
		List<String> players = controller.getAlivePlayers();
		String selected = selectPlayer(getScores(players), players);
		ActionI selectedAction = null;
		
		for (ActionI action : legalActions) {
			if (action.isTarget(selected)) {
				selectedAction = action;
				break;
			}
		}
		
		if (selectedAction != null) {
			think("I want to "+selectedAction);
			controller.act(selectedAction);
		}

	}
	
	private String selectPlayer(Map<String, Integer> scores, List<String> players) {
		String minPlayer = null;
		int minScore = Integer.MIN_VALUE;
		
		String maxPlayer = null;
		int maxScore = Integer.MAX_VALUE;
		
		for (String player : players) {
			Integer score = scores.get(player);
			if (score == null) {
				score = 1; //we don't know about this player
			}
			
			if (minScore < score) {
				minPlayer = player;
				minScore = score;
			}
			
			if (maxScore > score) {
				maxPlayer = player;
				maxScore = score;
			}
		}
		
		if ("wolf".equals(getRole().getName())) {
			return maxPlayer;
		} else {
			return minPlayer;
		}
	}
	
	private Map<String, Integer> getScores(List<String> players) {
		
		Map<String, Integer> scores = new TreeMap<String, Integer>();
		for (String player : players) {
			RoleI beliefRole = roles.get(player);
			Integer bias = baises.get(player);
			if (bias == null) {
				bias = 0;
			}
			
			if (beliefRole != null) {
				if ("seer".equals(beliefRole.getName())) {
					scores.put(player, -10 + bias);
				}
				
				if ("villager".equals(beliefRole.getName())) {
					scores.put(player, -5 + bias);
				}
				
				if ("wolf".equals(beliefRole.getName())) {
					scores.put(player, 10 + bias);
				}
			} else {
				scores.put(player, -5 + bias);
			}
			
		}
		
		return scores;
	}

}
