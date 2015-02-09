package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.WolfGame;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class AbstainAction implements ActionI {

	@Override
	public boolean isTarget(String selected) {
		return false;
	}

	@Override
	public void execute(WolfGame game, String player) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		// TODO Auto-generated method stub
		
	}

}
