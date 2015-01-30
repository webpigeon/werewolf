package uk.me.webpigeon.wolf;

import java.util.Collections;
import java.util.List;

public class ReadOnlyController implements PlayerController {
	private final WolfGame game;

	public ReadOnlyController(WolfGame game) {
		this.game = game;
	}

	public void takeAction(String action) {
		//you're an observer, you're not allowed.
	}

	public List<String> getActions() {		
		return Collections.emptyList();
	}

	@Override
	public List<String> getPlayers() {
		return game.getAlivePlayers();
	}

	@Override
	public GameState getStage() {
		return null;
	}
	
	public void talk(String message) {
		//you're a spectator, you don't get to do anything.
	}

}
