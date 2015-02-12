package uk.me.webpigeon.wolf.gui;

import uk.me.webpigeon.wolf.newcode.Event2Listener;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfFactory;

public class GraphicalGameRunner {

	public static void main(String[] args) {		
		WolfFrame gui = new WolfFrame();
				
		WolfController gameController = WolfFactory.buildGame();
		gameController.addListener(new Event2Listener(new GraphicalGameListener(gui)));
		gameController.addListener(new Event2Listener(new GameTimer(gameController)));
		
		Thread gameThread = new Thread(gameController);
		gameThread.start();
		
		gui.start();
	}

}
