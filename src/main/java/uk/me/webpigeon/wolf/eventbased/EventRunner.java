package uk.me.webpigeon.wolf.eventbased;

import uk.me.webpigeon.wolf.GameObserver;
import uk.me.webpigeon.wolf.RandomPlayer;

public class EventRunner {
	
	public static void main(String[] args) {
		
		WolfGame game = new WolfGame();
		game.addPlayer(buildPlayer(new RandomPlayer("Bob")));
		
		
		Thread gameThread = new Thread(game);
		gameThread.setName("game-thread");
		gameThread.start();
	}
	
	private static EventPlayerBridge buildPlayer(GameObserver p) {
		EventPlayerBridge player = new EventPlayerBridge(p);
		Thread t = new Thread(player);
		t.setName("player-"+p.getName());
		t.start();
		
		return player;
	}

}
