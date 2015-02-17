package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.PlayerUtils;

public class SeerSavingAnnounce implements Behavour {
	private final static Double PANIC_TRESHOLD = 0.45;
	private int playerCount;
	private boolean roleAnnounced;
	
	public String getID(){
		return "SeerAnnounceRole";
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
		
		double votes = beliefs.getVotesFor(player.getName());
		double voteRatio = votes / beliefs.getPlayers().size();
		
		if (voteRatio > PANIC_TRESHOLD) {
			return !roleAnnounced;
		}
		
		return false;
	}

	@Override
	public ActionI generateAction(AbstractPlayer player, BeliefSystem beliefs) {
		String myRole = beliefs.getRole(beliefs.getMyName());
		
		roleAnnounced = true;
		String message = PlayerUtils.toTripple(beliefs.getMyName(), "role", myRole);
		if ("seer".equals(myRole)) {
			//seers will announce to no one
			return new TalkAction(null, message);
		}

		return null;
	}

}
