package uk.me.webpigeon.wolf.newcode;

import uk.me.webpigeon.wolf.newcode.legacy.LegacyUtils;

public class WolfFactory {
	
	private WolfFactory() {
		
	}
	
	public static WolfController buildGame() {
		WolfModel model = new WolfModel();
		WolfController controller = new WolfController(model);

		controller.addPlayer("Fred", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("John", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Bob", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Wolfgang", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Pebbles", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Jackie", LegacyUtils.buildSmartPlayer(controller, model));
		controller.addPlayer("Sarah", LegacyUtils.buildSmartPlayer(controller, model));
		
		return controller;
	}

}
