package uk.me.webpigeon.wolf.graphs;

import uk.me.webpigeon.wolf.gui.GameTimer;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfFactory;

public class StatsRunner {


	public static void main(String[] args) {
		LifetimeStats stats = new LifetimeStats();
		StatsTracker tracker = new StatsTracker(stats);
		
		WolfController controller = WolfFactory.buildGame();
		controller.addListener(WolfFactory.wrap("game-stats", tracker));
		controller.addListener(WolfFactory.wrap("game-timer", new GameTimer(controller)));
		
		Thread gameThread = new Thread(controller);
		gameThread.start();
	}

}
