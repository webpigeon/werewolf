package uk.me.webpigeon.wolf.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.GameListener;
import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.events.EventI;

public class GraphicalGameListener implements GameListener {
	private WolfFrame controller;
	private List<String> playerList;
		
	public GraphicalGameListener(WolfFrame controller) {
		this.controller = controller;
		this.playerList = new ArrayList<String>();
	}

	@Override
	public void onDiscoverRole(String player, RoleI role) {
		controller.printContext(player+" is a "+role);
	}

	@Override
	public void onJoin(String name, WolfController controller) {
		throw new RuntimeException("Graphical listener joined the game?!");
	}

	@Override
	public void onStateChange(GameState newState) {
		
		switch (newState) {
			case DAYTIME:
				controller.printNarrative("It is now daytime, The villagers awaken from their slumber.");
				break;
				
			case NIGHTTIME:
				controller.printNarrative("It is now nightime, The villagers retire to their beds.");
				break;
				
			default:
				controller.printNarrative("state has changed: "+newState);	
		}

	}

	@Override
	public void onGameStart(Collection<String> players) {
		controller.printContext("Welcome to werewolf - AI only edition ("+players.size()+" players)");
		controller.setPlayers(players);
		
		playerList.clear();
		playerList.addAll(players);
	}

	@Override
	public void onMessage(String player, String message, String channel) {
		controller.printMessage(player, message);
	}

	@Override
	public void onDeath(String player, String cause, String role) {
		playerList.remove(player);
		controller.setPlayers(playerList);
		
		if ("eat".equals(cause)) {
			controller.printMessage("The villagers discover the remains of <b>" +player+"</b> who has been eaten by a wolf. They turn out to be a <b>"+role+"</b>. The villagers morn the loss.");
			return;
		}
		
		if ("lynch".equals(cause)) {
			controller.printMessage("Against "+player+"'s protests, the villagers string them up and lynch them. They turn out to be a <b>"+role+"</b>");
			return;
		}
	}

	@Override
	public void onVoteEntered(String voter, String candidate) {
		if (candidate == null){
			controller.printContext(voter+" has abstained from voting");
			return;
		}
		controller.printContext(voter+" has voted for "+candidate);
	}

}
