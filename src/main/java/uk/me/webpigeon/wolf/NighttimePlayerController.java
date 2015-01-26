package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NighttimePlayerController implements PlayerController {
	private final Player player;
	private final WolfGame game;
	private boolean hasSeen;

	public NighttimePlayerController(Player player, WolfGame game) {
		this.player = player;
		this.game = game;
		this.hasSeen = false;
	}

	public void takeAction(String action) {
		Role role = game.getPlayerRole(player);
		
		String[] args = action.split(" ");
		
		if (role == Role.VILLAGER) {
			//only legal action is sleep
			return;
		}
		
		if (role == Role.WOLF) {
			//wolf based lynching fun
			
			if ("eat".equalsIgnoreCase(args[0])) {
				Player eatee = game.getPlayerByName(args[1]);
				game.enterVote(player, eatee, false);
			}
			
		}
		
		if (role == Role.SEER) {
			//see 1 player and return role
			
			if ("see".equalsIgnoreCase(args[0]) && !hasSeen) {
				Player seenPlayer = game.getPlayerByName(args[1]);
				if (seenPlayer != null) {
					Role seenRole = game.getPlayerRole(seenPlayer);
					player.notifyRole(seenPlayer, seenRole);
					hasSeen = true;
				}
			}
		}
		
		
	}

	public List<String> getActions() {
		Role role = game.getPlayerRole(player);
		
		List<String> actions = new ArrayList<String>();
		
		if (role == Role.VILLAGER || role == Role.SEER) {
			actions.add("sleep");
		}
		
		if (role == Role.SEER) {
			List<Player> players = game.getAlivePlayers();
			for (Player player : players) {
				actions.add("see "+player);
			}
		}
		
		if (role == Role.WOLF) {
			List<Player> players = game.getAlivePlayers();
			for (Player player : players) {
				actions.add("eat "+player);
			}
		}
		
		return actions;
	}
	
	@Override
	public List<Player> getPlayers() {
		return game.getAlivePlayers();
	}

	@Override
	public GameState getStage() {
		return GameState.NIGHTTIME;
	}

	
}
