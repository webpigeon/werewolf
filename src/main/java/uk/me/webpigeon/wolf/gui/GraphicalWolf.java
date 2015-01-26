package uk.me.webpigeon.wolf.gui;

import java.util.Arrays;
import java.util.List;

import uk.me.webpigeon.wolf.BasicIntelligencePlayer;
import uk.me.webpigeon.wolf.NoopPlayer;
import uk.me.webpigeon.wolf.RandomPlayer;
import uk.me.webpigeon.wolf.Role;
import uk.me.webpigeon.wolf.WolfGame;
import uk.me.webpigeon.wolf.WolfUtils;

public class GraphicalWolf {
	
	public static void main(String[] args) {
		WolfGame game = buildGame();
		
		WolfFrame frame = new WolfFrame();
		frame.start();
		
		WolfController controller = new WolfController(frame, game);
		
		
		GraphicalObserver observer = new GraphicalObserver(frame, controller);
		game.addObserver(observer);
		
		Thread gameThread = new Thread(game);
		gameThread.start();
	}
	
	private static WolfGame buildGame() {
		List<Role> roles = Arrays.asList(Role.WOLF, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.VILLAGER, Role.SEER);
		WolfGame game = new WolfGame(roles);
		
		game.add(WolfUtils.buildRandomPlayer("Fred"));
		game.add(WolfUtils.buildSmartPlayer("John"));
		game.add(WolfUtils.buildSmartPlayer("Bob"));
		game.add(WolfUtils.buildSmartPlayer("Wolfgang"));
		game.add(WolfUtils.buildSmartPlayer("Pebbles"));
		game.add(WolfUtils.buildSmartPlayer("Jackie"));
		game.add(WolfUtils.buildSmartPlayer("Sarah"));
		
		return game;
	}

}
