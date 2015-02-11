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

public class CraftyWolfPlayer extends BasicIntelligencePlayer {
	private Pattern messageRegex;
	private ActionI currentAction;
	
	public CraftyWolfPlayer() {
		messageRegex = Pattern.compile("\\((\\w+),(\\w+),(\\w+)\\)");
	}
	
	public void notifyVote(String voter, String votee) {		
		//if they vote for me I don't like them
		if (votee.equals(getName())) {
			recordBias(voter, 1);
			
			// if someone votes for me then pretend to be the seer
			if ("wolf".equals(getRole())) {
				shareInfomation(getName(), "role", "seer");
			}
			
			// tell them we're a villager
			if ("villager".equals(getRole())) {
				shareInfomation(getName(), "role", "villager");
			}
		}
		
		// If I'm the seer and I know that the person is not a wolf, I tell people
		if ("seer".equals(getRole())) {
			String voteeRole = roles.get(votee);
			
			if (voteeRole != null) {
				shareInfomation(votee, "role", voteeRole);
			}
		}
		
		String voteeRole = roles.get(votee);
		if (voteeRole != null) {
			if ("wolf".equals(voteeRole)) {
				// the person voted for the wolf
				recordBias(voter, -1);
			} else if ("seer".equals(voteeRole)) {
				// if they vote for the seer, They're probably a moron or the wolf
				recordBias(voter, 10);
			}
		}
	}
	
	@Override
	protected void takeAction(Collection<ActionI> legalActions) {	
		if (legalActions.isEmpty()) {
			return;
		}
		
		List<String> players = controller.getAlivePlayers();
		String selected = selectPlayer(getScores(players), players, controller.getStage());
		ActionI selectedAction = null;
		
		
		for (ActionI action : legalActions) {
			if (selectedAction == null) {
				selectedAction = action;
			}
			
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
	
	private String selectPlayer(Map<String, Integer> scores, List<String> players, GameState state) {
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
		
		if ("wolf".equals(getRole()) && state == GameState.NIGHTTIME) {
			return maxPlayer;
		} else {
			return minPlayer;
		}
	}

	@Override
	protected void clearTurnLocks() {
		currentAction = null;
	}
	
}
