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

/**
 * Vote for a random player we know not to be a "safe" role
 */
public class RandomUnsafeLynch implements Behavour {
	private Random random;
	private String choice;
	
	public RandomUnsafeLynch() {
		this.random = new Random();
	}

	@Override
	public boolean canActivate(AbstractPlayer player, BeliefSystem myPlayer, String setBy) {
		if (!player.isState(GameState.DAYTIME)) {
			// we can only lynch during the daytime
			return false;
		}
		
		Collection<String> targets = getTargets(player.getName(), myPlayer);
		
		if (setBy.equals(getID())) {
			//if we set the target, make sure the target is still valid
			return !targets.contains(choice);
		}
		
		return !targets.isEmpty();
	}

	@Override
	public ActionI generateAction(AbstractPlayer player, BeliefSystem myPlayer) {
		
		List<String> targets = getTargets(player.getName(), myPlayer);
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
	
	private List<String> getTargets(String name, BeliefSystem beliefs) {		
		List<String> unsafePlayers = new ArrayList<String>();
		for (String player : beliefs.getPlayers() ) {
			String role = beliefs.getRole(player);
			
			if (isUnsafe(role) && !player.equals(name)) {
				unsafePlayers.add(player);
			}
		}
		return unsafePlayers;
	}

	@Override
	public String getID() {
		return "lynchRandomUnsafe";
	}

}
