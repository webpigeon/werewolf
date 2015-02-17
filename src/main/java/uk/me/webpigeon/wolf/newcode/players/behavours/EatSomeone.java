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

/**
 * Vote for a random player we know not to be a "safe" role
 */
public class EatSomeone implements Behavour {
	private static final String[] SAFE_ROLES = {"wolf"};
	private Random random;
	
	public EatSomeone() {
		this.random = new Random();
	}

	public String getID(){
		return "EatSomeone";
	}
	
	@Override
	public boolean canActivate(AbstractPlayer player, BeliefSystem myPlayer, String setBy) {
		return player.isState(GameState.NIGHTTIME) && player.isRole("wolf") && !"EatSomeone".equals(setBy);
	}

	@Override
	public ActionI generateAction(AbstractPlayer player1, BeliefSystem myPlayer) {
		
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
		return new EatAction(choice);
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
		
		for (String safeRole : SAFE_ROLES) {
			if (safeRole.equals(role)) {
				return false;
			}
		}
		
		return true;
	}

}
