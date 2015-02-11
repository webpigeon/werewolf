package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.me.webpigeon.wolf.action.ActionI;

public class BasicIntelligencePlayer extends AbstractPlayer {
	private Pattern messageRegex;
	private Map<String, Integer> baises;
	private ActionI currentAction;
	private Set<String> sharedInfomation;
	
	public BasicIntelligencePlayer() {
		baises = new TreeMap<String, Integer>();
		messageRegex = Pattern.compile("\\((\\w+),(\\w+),(\\w+)\\)");
		sharedInfomation = new HashSet<String>();
	}
	
	public void notifyVote(String voter, String votee) {
		Integer voterBias = baises.get(voter);
		think("I heard that "+voter+" voted for "+votee);
		think("my biases are "+baises);
		if (voterBias == null) {
			voterBias = 0;
		}
		
		//if they vote for me I don't like them
		if (votee.equals(getName())) {
			baises.put(voter, voterBias+1);
		}
		
		// If I'm the seer and I know that the person is not a wolf, I tell people
		if ("seer".equals(getRole())) {
			String voteeRole = roles.get(votee);
			
			if (voteeRole != null) {
				String infomation = "("+votee+","+"role,"+voteeRole+")";
				if (!sharedInfomation.contains(infomation)) {
					controller.talk(infomation);
					sharedInfomation.add(infomation);
				}
			}
		}
		
		String voteeRole = roles.get(votee);
		if (voteeRole != null) {
			if ("wolf".equals(voteeRole)) {
				// the person voted for the wolf
				baises.put(voter, voterBias-1);
			} else if ("seer".equals(voteeRole)) {
				// if they vote for the seer, They're probably a moron or the wolf
				baises.put(voter, voterBias+10);
			}
		}
		
		think("my biases are now "+baises);
	}
	
	public void notifyMessage(String who, String message){
		
		Matcher m = messageRegex.matcher(message);
		if (m.matches()) {
			
			String g1 = m.group(1);
			String g2 = m.group(2);
			String g3 = m.group(3);
			
			if ("role".equals(g2)) {
				think(who+" told me that "+g1+" is a "+g3);
				roles.put(g1, g3);
				triggerAction();
			}
			
		}
		
	}
	
	@Override
	protected void takeAction(Collection<ActionI> legalActions) {	
		if (legalActions.isEmpty()) {
			return;
		}
		
		List<String> players = controller.getAlivePlayers();
		String selected = selectPlayer(getScores(players), players);
		ActionI selectedAction = null;
		
		think("My target is "+selected);
		
		for (ActionI action : legalActions) {
			if (action.isTarget(selected)) {
				selectedAction = action;
				break;
			}
		}
		
		if (selectedAction == null && !legalActions.isEmpty()) {
			think("I didn't select any action to perform");
			return;
		}
		
		
		if (currentAction == null || !selectedAction.equals(currentAction)) {
			if (currentAction != null) {
				think("I changed my mind, I want to "+selectedAction+" rather than "+currentAction);
			}
			controller.act(selectedAction);
			currentAction = selectedAction;
		} else {
			think("I already want to "+currentAction);
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
		
		if ("wolf".equals(getRole())) {
			return maxPlayer;
		} else {
			return minPlayer;
		}
	}
	
	private Map<String, Integer> getScores(List<String> players) {
		
		Map<String, Integer> scores = new TreeMap<String, Integer>();
		for (String player : players) {
			String beliefRole = roles.get(player);
			Integer bias = baises.get(player);
			if (bias == null) {
				bias = 0;
			}
			
			if (beliefRole != null) {
				if ("seer".equals(beliefRole)) {
					scores.put(player, -10 + bias);
				}
				
				if ("villager".equals(beliefRole)) {
					scores.put(player, -5 + bias);
				}
				
				if ("wolf".equals(beliefRole)) {
					scores.put(player, 10 + bias);
				}
			} else {
				scores.put(player, -5 + bias);
			}
			
		}
		
		return scores;
	}

	@Override
	protected void clearTurnLocks() {
		currentAction = null;
	}
	
}
