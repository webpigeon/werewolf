package uk.me.webpigeon.wolf.gui;

import uk.me.webpigeon.wolf.newcode.Event2Listener;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfFactory;
import uk.me.webpigeon.wolf.newcode.players.HumanPlayer;

public class GraphicalGameRunner {

	public static void main(String[] args) {
				
		// build the game
		WolfController gameController = WolfFactory.buildGame();
		gameController.addListener(new Event2Listener(new GameTimer(gameController)));

		// build the player
		HumanPlayer player = new HumanPlayer();
		InteractivePanel playerPanel = new InteractivePanel(player);
		gameController.addPlayer("webpigeon", WolfFactory.wrap("webpigeon", player));
		
		// build the GUI
		WolfFrame gui = new WolfFrame(playerPanel);
		gameController.addListener(new Event2Listener(new GraphicalGameListener(gui)));
		gui.start();
		
		// start the game
		Thread gameThread = new Thread(gameController);
		gameThread.start();
	}

}
