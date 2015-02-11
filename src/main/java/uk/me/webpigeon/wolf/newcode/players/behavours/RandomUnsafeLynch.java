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
public class RandomUnsafeLynch implements Behavour {
	private static final String[] UNSAFE_ROLES = {"villager"};
	private Random random;
	
	public RandomUnsafeLynch() {
		this.random = new Random();
	}

	@Override
	public boolean canActivate(BeliefSystem myPlayer) {
		return true;
	}

	@Override
	public ActionI generateAction(BeliefSystem myPlayer) {
		
		List<String> unsafePlayers = new ArrayList<String>();
		for (String player : myPlayer.getPlayers() ) {
			String role = myPlayer.getRole(player);
			
			if (isUnsafe(role)) {
				unsafePlayers.add(role);
			}
		}
		
		if (unsafePlayers.isEmpty()) {
			return null;
		}
		
		String choice = unsafePlayers.get(random.nextInt(unsafePlayers.size()));
		//return new LynchAction(choice);
		//TODO make actions anonmous
		return null;
	}
	
	/**
	 * We consider a role unsafe if the role is unknown or in UNSAFE_ROLES.
	 * 
	 * @param role the role we are checking
	 * @return true if the role is unsafe, false otherwise
	 */
	private boolean isUnsafe(String role) {
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
