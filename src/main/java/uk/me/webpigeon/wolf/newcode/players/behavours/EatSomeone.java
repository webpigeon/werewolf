package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.EatAction;
import uk.me.webpigeon.wolf.newcode.actions.LynchAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.FactBase;

/**
 * Vote for a random player we know not to be a "safe" role
 */
public class EatSomeone implements ProductionRule {
	private static final String[] SAFE_ROLES = {"wolf"};
	private Random random;
	
	public EatSomeone() {
		this.random = new Random();
	}

	public String getID(){
		return "EatSomeone";
	}
	
	@Override
	public boolean canActivate(FactBase facts, String setBy) {
		String myID = getID();
		if (myID.equals(setBy)) {
			return false;
		}
		
		return facts.hasFact(Facts.AGENT_ROLE, "wolf") && facts.hasFact(Facts.GAME_STATE, "nighttime");
	}

	@Override
	public ActionI generateAction(FactBase beliefs) {
		
		List<String> targets = new ArrayList<String>();
		for (String player : beliefs.getValues(Facts.AGENT_NAME)) {
			String roleFactName = String.format(Facts.PLAYER_ROLE, player);
			
			//TODO make safe roles a belief
			boolean isSafe = false;
			List<String> possibleRoles = beliefs.getValues(roleFactName);
			for (String safeRole : SAFE_ROLES) {
				
				//we're being cautious, If we think the player *might* be a wolf, don't eat them
				if (possibleRoles.contains(safeRole)){
					isSafe = true;
					break;
				}
			}
			
			if (!isSafe) {
				targets.add(player);
			}
		}
		
		if (targets.isEmpty()) {
			return null;
		}
		
		String choice = targets.get(random.nextInt(targets.size()));
		return new EatAction(choice);
	}

}
