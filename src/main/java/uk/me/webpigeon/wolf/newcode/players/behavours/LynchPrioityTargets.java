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
	public boolean canActivate(BeliefSystem myPlayer, String setBy) {
		
		if (!myPlayer.isInState(GameState.DAYTIME) || !myPlayer.isRole(ourRole) ){
			return false;
		}
		
		buildTargetMap(myPlayer);
		return !targetMap.isEmpty();
	}

	@Override
	public ActionI generateAction(BeliefSystem myPlayer) {
		if (targetMap == null || targetMap.isEmpty()) {
			buildTargetMap(myPlayer);
		}
		
		// I'm hoping the order of entryset is defined for an ordered map, else I'll be sad.
		for (Map.Entry<Integer, List<String>> targetsEntry : targetMap.entrySet()) {
			
			List<String> targets = targetsEntry.getValue();
			if (targets.isEmpty()) {
				continue;
			}
			
			for (String target : targets) {
				if (myPlayer.isAlive(target)) {
					// find the first alive player and return them
					return new LynchAction(target);
				}
			}
			
		}
		
		// all our targets are dead o.O - target map is out dated or we shouldn't have fired
		targetMap = null;
		return null;
	}
	
	private void buildTargetMap(BeliefSystem beliefs) {
		targetMap = new TreeMap<>();
		
		for (String player : beliefs.getPlayers()) {
			
			String role = beliefs.getRole(player);
			if (role == null) {
				continue;
			}
			
			Integer score = scores.get(role);
			if (score != null ){
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
