package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.LynchAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.FactBase;

/**
 * Vote for a random player we know not to be a "safe" role
 */
public class LynchPrioityTargets implements ProductionRule {
	private String ourRole;
	
	private Map<String, Integer> scores;
	private Map<Integer, List<String>> targetMap;
	
	public LynchPrioityTargets(String ourRole, String ... targetRoles) {
		
		this.scores = new TreeMap<>();
		this.targetMap = null;
		
		int scoreCount = 0;
		for (String targetRole : targetRoles) {
			scores.put(targetRole, scoreCount++);
		}
		
		this.ourRole = ourRole;
	}

	public String getID(){
		return "LynchPrioityTargets";
	}
	
	@Override
	public boolean canActivate(FactBase beliefs, String setBy) {
		
		if (!beliefs.hasFact(Facts.GAME_STATE, "DAYTIME") || !beliefs.hasFact(Facts.AGENT_ROLE, ourRole) ){
			System.out.println("it is not daytime or I am not a "+ourRole);
			return false;
		}
		
		buildTargetMap(beliefs);
		return !targetMap.isEmpty();
	}

	@Override
	public ActionI generateAction(FactBase beliefs) {
		if (targetMap == null || targetMap.isEmpty()) {
			buildTargetMap(beliefs);
		}
		
		List<String> alivePlayers = beliefs.getValues(Facts.ALIVE_PLAYERS);
		
		// I'm hoping the order of entryset is defined for an ordered map, else I'll be sad.
		for (Map.Entry<Integer, List<String>> targetsEntry : targetMap.entrySet()) {
			
			List<String> targets = targetsEntry.getValue();
			if (targets.isEmpty()) {
				continue;
			}
			
			for (String target : targets) {
				if (alivePlayers.contains(target)) {
					// find the first alive player and return them
					return new LynchAction(target);
				}
			}
			
		}
		
		// all our targets are dead o.O - target map is out dated or we shouldn't have fired
		targetMap = null;
		return null;
	}
	
	private void buildTargetMap(FactBase beliefs) {
		targetMap = new TreeMap<>();
		
		List<String> alivePlayers = beliefs.getValues(Facts.ALIVE_PLAYERS);
		for (String player : alivePlayers) {
			
			String roleFactName = String.format(Facts.PLAYER_ROLE, player);
			List<String> possibleRoles = beliefs.getValues(roleFactName);
			
			if (possibleRoles.isEmpty()) {
				continue;
			}
			
			for (String role : possibleRoles) {
				Integer score = scores.get(role);
				if ( score != null ){
					List<String> playerList = targetMap.get(score);
					if (playerList == null) {
						playerList = new ArrayList<String>();
						targetMap.put(score, playerList);
					}
					playerList.add(player);
				}
			}
			
		}
	}

}
