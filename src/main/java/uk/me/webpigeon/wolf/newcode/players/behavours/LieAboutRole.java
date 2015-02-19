package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.PlayerUtils;

public class LieAboutRole implements ProductionRule {
	private int playerCount;
	private boolean roleAnnounced;

	@Override
	public String getID() {
		return "LieAboutRole";
	}

	@Override
	public boolean canActivate(BeliefSystem beliefs, String setByBehavour) {		
		int currentPlayerCount = beliefs.getPlayers().size();
		String myRole = beliefs.getRole(beliefs.getMyName());
		if (playerCount < currentPlayerCount && myRole != null) {
			// a new game started
			playerCount = currentPlayerCount;
			roleAnnounced = false;
		}
		
		return !roleAnnounced && myRole != null && "wolf".equals(myRole);
	}

	@Override
	public ActionI generateAction(BeliefSystem beliefs) {
		roleAnnounced = true;
		return new TalkAction(null, PlayerUtils.toTripple(beliefs.getMyName(), "role", "villager"));
	}

}
