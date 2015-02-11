package uk.me.webpigeon.wolf.newcode.legacy.players;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;

public class CraftyWolfPlayer extends BasicIntelligencePlayer {
	
	public void notifyVote(String voter, String votee) {		
		if (votee == null) {
			return;
		}
		
		//if they vote for me I don't like them
		if (votee.equals(getName())) {
			recordBias(voter, 1);
			
			// if someone votes for me then pretend to be the seer
			if ("wolf".equals(getRole())) {
				shareInfomation(getName(), "role", "seer");
			}
			
			// tell them we're a villager
			if ("villager".equals(getRole())) {
				shareInfomation(getName(), "role", "villager");
			}
		}
		
		// If I'm the seer and I know that the person is not a wolf, I tell people
		if ("seer".equals(getRole())) {
			String voteeRole = roles.get(votee);
			
			if (voteeRole != null) {
				shareInfomation(votee, "role", voteeRole);
			}
		}
		
		String voteeRole = roles.get(votee);
		if (voteeRole != null) {
			if ("wolf".equals(voteeRole)) {
				// the person voted for the wolf
				recordBias(voter, -1);
			} else if ("seer".equals(voteeRole)) {
				// if they vote for the seer, They're probably a moron or the wolf
				recordBias(voter, 10);
			}
		}
	}
	
}
