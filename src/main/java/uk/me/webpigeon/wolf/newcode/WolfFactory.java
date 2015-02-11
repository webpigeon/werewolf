package uk.me.webpigeon.wolf.newcode;

import uk.me.webpigeon.wolf.newcode.legacy.LegacyUtils;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.behavours.BehavourPlayer;
import uk.me.webpigeon.wolf.newcode.players.behavours.RandomUnsafeLynch;

public class WolfFactory {
	
	private WolfFactory() {
		
	}
	
	public static WolfController buildGame() {
		WolfModel model = new WolfModel();
		WolfController controller = new WolfController(model);

		controller.addPlayer("Fred", LegacyUtils.buildCraftyPlayer(controller, model));
		controller.addPlayer("John", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Bob", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Wolfgang", LegacyUtils.buildCraftyPlayer(controller, model));
		controller.addPlayer("Pebbles", LegacyUtils.buildRandomPlayer(controller, model));
		controller.addPlayer("Jackie", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Sarah", LegacyUtils.buildSmartPlayer(controller, model));
		
		controller.addPlayer("Rawr", buildBehavourPlayer());
		
		return controller;
	}
	
	public static SessionManager buildBehavourPlayer() {
		BehavourPlayer player = new BehavourPlayer();
		player.addBehavour(new RandomUnsafeLynch());
		
		Thread t = new Thread(player);
		t.start();
		
		return player;
	}

}
