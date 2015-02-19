package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.Collection;
import java.util.List;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.FactBase;
import uk.me.webpigeon.wolf.newcode.players.PlayerUtils;

public class DebugAnnounceRole implements ProductionRule {
	private boolean roleAnnounced;
	
	private String currentGame;
	
	public String getID(){
		return "DebugAnnounceRole";
	}

	@Override
	public boolean canActivate(FactBase facts, String setBy) {
		String gameID = facts.getValue(Facts.GAME_ID);
		String myRole = facts.getValue(Facts.AGENT_ROLE);
		if (gameID == null || myRole == null) {
			return false;
		}
		
		if (!gameID.equals(currentGame)) {
			roleAnnounced = false;
			currentGame = gameID;
		}
		
		return !roleAnnounced && myRole != null;
	}

	@Override
	public ActionI generateAction(FactBase beliefs) {
		System.out.println(beliefs);
		String myRole = beliefs.getValue(Facts.AGENT_ROLE);
		
		roleAnnounced = true;
		String message = PlayerUtils.toTripple(beliefs.getValue(Facts.AGENT_NAME), "role", myRole);
		if ("wolf".equals(myRole)) {
			//wolves will only announce to the wolf team
			return new TalkAction(null, message, "wolf");
		}
		
		if ("seer".equals(myRole)) {
			//seers will announce to no one
			return null;
		}

		return new TalkAction(null, message);
	}

}
