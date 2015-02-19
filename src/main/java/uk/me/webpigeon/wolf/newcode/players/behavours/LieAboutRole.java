package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.FactBase;
import uk.me.webpigeon.wolf.newcode.players.PlayerUtils;

public class LieAboutRole implements ProductionRule {
	private String currentGame;
	private boolean roleAnnounced;

	@Override
	public String getID() {
		return "LieAboutRole";
	}

	@Override
	public boolean canActivate(FactBase beliefs, String setByBehavour) {		
		String gameID = beliefs.getValue(Facts.GAME_ID);
		if (gameID == null) {
			return false;
		}
		
		if (!gameID.equals(currentGame)) {
			roleAnnounced = false;
			currentGame = gameID;
		}
		
		return !roleAnnounced && beliefs.hasFact(Facts.AGENT_ROLE, "wolf");
	}

	@Override
	public ActionI generateAction(FactBase beliefs) {
		roleAnnounced = true;
		return new TalkAction(null, PlayerUtils.toTripple(beliefs.getValue(Facts.AGENT_NAME), "role", "villager"));
	}

}
