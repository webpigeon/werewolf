package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class EndGame extends NewAction {

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		controller.setState(GameState.GAMEOVER);
	}

	@Override
	public boolean isEqual(ActionI action) {
		return false;
	}


}
