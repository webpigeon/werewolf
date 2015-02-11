package uk.me.webpigeon.wolf.newcode.legacy;

import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.actions.WolfUtils;
import uk.me.webpigeon.wolf.newcode.legacy.players.CraftyWolfPlayer;
import uk.me.webpigeon.wolf.newcode.legacy.players.GameObserver;

public class LegacyUtils {
	
	public static SessionManager buildSmartPlayer(WolfController controller, WolfModel model) {
		
		GameObserver oldPlayer = WolfUtils.buildSmartPlayer();
		LegacyController playerController = new LegacyController(controller, model);
		return new LegacyWrapper(oldPlayer, playerController);
		
	}
	
	public static SessionManager buildCraftyPlayer(WolfController controller, WolfModel model) {
		
		GameObserver oldPlayer = new CraftyWolfPlayer();
		LegacyController playerController = new LegacyController(controller, model);
		return new LegacyWrapper(oldPlayer, playerController);
		
	}
	
	public static SessionManager buildRandomPlayer(WolfController controller, WolfModel model) {
		
		GameObserver oldPlayer = WolfUtils.buildSmartPlayer();
		LegacyController playerController = new LegacyController(controller, model);
		return new LegacyWrapper(oldPlayer, playerController);
		
	}

}
