package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.LynchAction;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;

/**
 * Vote for a random player we know not to be a "safe" role
 */
public class LynchPrioityTargets implements Behavour {
	private static final String[] UNSAFE_ROLES = {"wolf"};
	private Random random;
	
	public LynchPrioityTargets() {
		this.random = new Random();
	}

	@Override
	public boolean canActivate(BeliefSystem myPlayer, ActionI a) {
		
		List<String> targets = new ArrayList<String>();
		for (String player : myPlayer.getPlayers()) {
			String role = myPlayer.getRole(player);
			if (isTarget(role)) {
				targets.add(player);
			}
		}
		
		if (a != null && a instanceof LynchAction) {
			LynchAction act = (LynchAction)a;
			if (targets.contains(act.getTarget())) {
				return false;
			}
		}
		
		return !targets.isEmpty();
	}

	@Override
	public ActionI generateAction(BeliefSystem myPlayer) {
		
		List<String> targets = new ArrayList<String>();
		for (String player : myPlayer.getPlayers()) {
			String role = myPlayer.getRole(player);
			if (isTarget(role)) {
				targets.add(player);
			}
		}
		
		if (targets.isEmpty()) {
			return null;
		}
		
		String choice = targets.get(random.nextInt(targets.size()));
		return new LynchAction(choice);
	}
	
	/**
	 * We consider a role unsafe if the role is unknown or in UNSAFE_ROLES.
	 * 
	 * @param role the role we are checking
	 * @return true if the role is unsafe, false otherwise
	 */
	private boolean isTarget(String role) {
		if (role == null) {
			return true;
		}
		
		for (String unsafeRole : UNSAFE_ROLES) {
			if (unsafeRole.equals(role)) {
				return true;
			}
		}
		
		return false;
	}

}
