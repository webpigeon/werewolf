package uk.me.webpigeon.wolf.gui;

import java.util.List;

import uk.me.webpigeon.wolf.BasicIntelligencePlayer;
import uk.me.webpigeon.wolf.RoleI;
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
		List<RoleI> roles = WolfUtils.buildRoleList();
		WolfGame game = new WolfGame(roles);
		
		game.add("Fred", new BasicIntelligencePlayer());
		game.add("John", new BasicIntelligencePlayer());
		game.add("Bob", new BasicIntelligencePlayer());
		game.add("Wolfgang", new BasicIntelligencePlayer());
		game.add("Pebbles", new BasicIntelligencePlayer());
		game.add("Jackie", new BasicIntelligencePlayer());
		game.add("Sarah", new BasicIntelligencePlayer());
		
		return game;
	}

}
