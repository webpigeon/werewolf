package uk.me.webpigeon.wolf.newcode.events;

import java.util.Collection;


public class GameStarted implements EventI {
	public final Collection<String> players;
	
	public GameStarted(Collection<String> players) {
		this.players = players;
	}
	
	public String getType() {
		return "gameStarted";
	}

}
