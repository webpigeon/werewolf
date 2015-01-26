package uk.me.webpigeon.wolf.gui;

import java.util.Arrays;
import java.util.List;

import uk.me.webpigeon.wolf.BasicIntelligencePlayer;
import uk.me.webpigeon.wolf.NoopPlayer;
import uk.me.webpigeon.wolf.Role;
import uk.me.webpigeon.wolf.WolfGame;

public class GraphicalWolf {
	
	public static void main(String[] args) {
		WolfGame game = buildGame();
		
		WolfFrame frame = new WolfFrame();
		frame.start();
		GraphicalObserver observer = new GraphicalObserver(frame, game);
		game.addObserver(observer);
		
		Thread gameThread = new Thread(game);
		gameThread.start();
	}
	
	private static WolfGame buildGame() {
		List<Role> roles = Arrays.asList(Role.WOLF, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.SEER);
		WolfGame game = new WolfGame(roles);
		
		game.add(new NoopPlayer("Fred"));
		game.add(new BasicIntelligencePlayer("John"));
		game.add(new BasicIntelligencePlayer("Bob"));
		game.add(new BasicIntelligencePlayer("Wolfgang"));
		game.add(new BasicIntelligencePlayer("Pebbles"));
		game.add(new BasicIntelligencePlayer("Jackie"));
		game.add(new BasicIntelligencePlayer("Sarah"));
		
		return game;
	}

}
