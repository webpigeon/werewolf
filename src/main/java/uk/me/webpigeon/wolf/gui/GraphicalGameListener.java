package uk.me.webpigeon.wolf.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.newcode.GameListener;

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
	public void onJoin(String name, Queue<ActionI> actionQueue) {
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
	public void onDeath(String player, String cause) {
		playerList.remove(player);
		controller.setPlayers(playerList);
		
		if ("eat".equals(cause)) {
			controller.appendText("The villagers discover the remains of <b>" +player+"</b> who has been eaten by a wolf. The villagers morn the loss.");
			return;
		}
		
		if ("lynch".equals(cause)) {
			controller.appendText("Against "+player+"'s protests, the villagers string them up and lynch them.");
			return;
		}
	}

}
