package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;
import uk.me.webpigeon.wolf.newcode.players.FactBase;

public class DetectLiers implements ProductionRule {
	private String untrustyCall;

	@Override
	public String getID() {
		return "detectLiers";
	}

	@Override
	public boolean canActivate(FactBase facts, String setByBehavour) {
		List<String> alivePlayers = new ArrayList<>(facts.getValues(Facts.ALIVE_PLAYERS));
		alivePlayers.removeAll(facts.getValues(Facts.UNTRUSTWORTHY_PLAYERS));
		
		if (alivePlayers.isEmpty()) {
			return false;
		}
		
		for (String alivePlayer : alivePlayers) {
			String messageTag = String.format(Facts.PLAYER_MESSAGES, alivePlayer);
			List<String> messages = facts.getValues(messageTag);
			if (messageTag.isEmpty()) {
				System.out.println("no messages from "+alivePlayer);
				continue;
			}
			
			List<String> deadPlayers = facts.getValues(Facts.DEAD_PLAYERS);
			if (deadPlayers.isEmpty()) {
				return false;
			}
			
			for (String message : messages) {
				String[] args = message.split(" ");
				System.out.println("message: "+Arrays.toString(args));
				if ("role".equals(args[1])) {
					
					//check the players role calls against what we know
					if (deadPlayers.contains(args[0])){
						String deathRoleTag = String.format(Facts.PLAYER_DEATH_ROLE, args[0]);
						String role = facts.getValue(deathRoleTag);
						if (!role.equals(args[2])) {
							//We have detected a fake call
							facts.storeFact(Facts.UNTRUSTWORTHY_PLAYERS, alivePlayer);
							untrustyCall = alivePlayer;
							return true;
						}
					}
					
				}
			}
		}
		
		return false;
	}

	@Override
	public ActionI generateAction(FactBase beliefs) {
		String CALL_FORMAT = "(%s,%s,%s)";
		String call = String.format(CALL_FORMAT, untrustyCall, "trust", "untrustworthy");
		untrustyCall = null;
		
		return new TalkAction(null, call);
	}

}
