package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.LynchAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.FactBase;

/**
 * Vote for a random player we know not to be a "safe" role
 */
public class RandomUnsafeLynch implements ProductionRule {
	private Random random;
	private String choice;
	
	public RandomUnsafeLynch() {
		this.random = new Random();
	}

	@Override
	public boolean canActivate(FactBase facts, String setBy) {
		// we can only lynch during the daytime
		if (!facts.hasFact(Facts.GAME_STATE, GameState.DAYTIME.name())) {
			return false;
		}
		
		Collection<String> targets = getTargets(facts);
		
		if (setBy != null && setBy.equals(getID())) {
			//if we set the target, make sure the target is still valid
			return !targets.contains(choice);
		}
		
		System.out.println("targets: "+targets);
		
		
		return !targets.isEmpty();
	}

	@Override
	public ActionI generateAction(FactBase facts) {
		
		List<String> targets = getTargets(facts);
		if (targets.isEmpty()) {
			return null;
		}
		
		choice = targets.get(random.nextInt(targets.size()));
		return new LynchAction(choice);
	}
	
	/**
	 * We consider a role unsafe if the role is unknown
	 * 
	 * @param role the role we are checking
	 * @return true if the role is unsafe, false otherwise
	 */
	private boolean isUnsafe(String role) {
		return role == null;
	}
	
	private List<String> getTargets(FactBase facts) {		
		List<String> alivePlayers = facts.getValues(Facts.ALIVE_PLAYERS);
		
		List<String> unsafePlayers = new ArrayList<String>();
		for (String player : alivePlayers) {
			String roleFactName = String.format(Facts.PLAYER_ROLE, player);
			Collection<String> possibleRoles = facts.getValues(roleFactName);
			
			if (possibleRoles.isEmpty()) {
				unsafePlayers.add(player);
			} else {		
				for (String possibleRole : possibleRoles) {
					if (isUnsafe(possibleRole)) {
						unsafePlayers.add(player);
					}
				}
			}
		}
		
		return unsafePlayers;
	}

	@Override
	public String getID() {
		return "lynchRandomUnsafe";
	}

}
