package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.PlayerUtils;

public class DebugAnnounceRole implements Behavour {
	private int playerCount;
	private boolean roleAnnounced;
	
	public String getID(){
		return "DebugAnnounceRole";
	}

	@Override
	public boolean canActivate(AbstractPlayer player, BeliefSystem beliefs, String setBy) {
		int currentPlayerCount = beliefs.getPlayers().size();
		String myRole = beliefs.getRole(beliefs.getMyName());
		if (playerCount < currentPlayerCount && myRole != null) {
			// a new game started
			playerCount = currentPlayerCount;
			roleAnnounced = false;
		}
		
		return !roleAnnounced && myRole != null;
	}

	@Override
	public ActionI generateAction(AbstractPlayer player, BeliefSystem beliefs) {
		String myRole = beliefs.getRole(beliefs.getMyName());
		
		roleAnnounced = true;
		String message = PlayerUtils.toTripple(beliefs.getMyName(), "role", myRole);
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
