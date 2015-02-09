package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class EndGame extends NewAction {

	@Override
	public void execute(WolfController controller, WolfModel model) {
		controller.setState(GameState.GAMEOVER);
		controller.announceState(GameState.GAMEOVER);
	}


}