package uk.me.webpigeon.wolf.newcode;

import uk.me.webpigeon.wolf.newcode.legacy.LegacyUtils;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.behavours.BehavourPlayer;
import uk.me.webpigeon.wolf.newcode.players.behavours.LynchPrioityTargets;
import uk.me.webpigeon.wolf.newcode.players.behavours.RandomUnsafeLynch;

public class WolfFactory {
	
	private WolfFactory() {
		
	}
	
	public static WolfController buildGame() {
		WolfModel model = new WolfModel();
		WolfController controller = new WolfController(model);

		controller.addPlayer("Fred_c", LegacyUtils.buildCraftyPlayer(controller, model));
		controller.addPlayer("John_s", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Bob_s", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Wolfgang_c", LegacyUtils.buildCraftyPlayer(controller, model));
		controller.addPlayer("Pebbles_r", LegacyUtils.buildRandomPlayer(controller, model));
		controller.addPlayer("Jackie_s", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Sarah_s", LegacyUtils.buildSmartPlayer(controller, model));
		
		controller.addPlayer("Jess_bh", buildBehavourPlayer());
		
		return controller;
	}
	
	public static SessionManager buildBehavourPlayer() {
		BehavourPlayer player = new BehavourPlayer();
		player.addBehavour(new LynchPrioityTargets());
		player.addBehavour(new RandomUnsafeLynch());
		
		Thread t = new Thread(player);
		t.setName("behavour-"+System.currentTimeMillis());
		t.start();
		
		return player;
	}

}
