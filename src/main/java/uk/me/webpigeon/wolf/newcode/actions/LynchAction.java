package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;


public class LynchAction extends VoteAction {

	public LynchAction(String name, String candidate) {
		super("lynch", name, candidate, true);
	}

	@Override
	protected boolean isValid(WolfController controller, WolfModel model) {
		GameState state = controller.getState();
		if (state != GameState.DAYTIME) {
			return false;
		}
		
		return model.isAlivePlayer(name);
	}


}
