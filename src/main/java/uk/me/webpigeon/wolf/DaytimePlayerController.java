package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DaytimePlayerController implements PlayerController {
	private final Player player;
	private final WolfGame game;

	public DaytimePlayerController(Player player, WolfGame game) {
		this.player = player;
		this.game = game;
	}

	public void takeAction(String action) {
		String[] args = action.split(" ");
		
		if ("lynch".equalsIgnoreCase(args[0])) {
			Player lynchee = game.getPlayerByName(args[1]);
			game.enterVote(player, lynchee, true);
		}
		
	}

	public List<String> getActions() {
		
		List<String> actions = new ArrayList<String>();
		
		List<Player> players = game.getAlivePlayers();
		for (Player player : players) {
			actions.add("lynch "+player);
		}
		
		return actions;
	}

	@Override
	public List<Player> getPlayers() {
		return game.getAlivePlayers();
	}

	@Override
	public GameState getStage() {
		return GameState.DAYTIME;
	}
	
}
