package uk.me.webpigeon.wolf.newcode.legacy;

import uk.me.webpigeon.wolf.CraftyWolfPlayer;
import uk.me.webpigeon.wolf.GameObserver;
import uk.me.webpigeon.wolf.WolfUtils;
import uk.me.webpigeon.wolf.newcode.GameListener;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class LegacyUtils {
	
	public static GameListener buildSmartPlayer(WolfController controller, WolfModel model) {
		
		GameObserver oldPlayer = WolfUtils.buildSmartPlayer();
		LegacyController playerController = new LegacyController(controller, model);
		return new LegacyWrapper(oldPlayer, playerController);
		
	}
	
	public static GameListener buildCraftyPlayer(WolfController controller, WolfModel model) {
		
		GameObserver oldPlayer = new CraftyWolfPlayer();
		LegacyController playerController = new LegacyController(controller, model);
		return new LegacyWrapper(oldPlayer, playerController);
		
	}
	
	public static GameListener buildRandomPlayer(WolfController controller, WolfModel model) {
		
		GameObserver oldPlayer = WolfUtils.buildSmartPlayer();
		LegacyController playerController = new LegacyController(controller, model);
		return new LegacyWrapper(oldPlayer, playerController);
		
	}

}
