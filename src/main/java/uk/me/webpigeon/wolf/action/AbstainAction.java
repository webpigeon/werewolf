package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.WolfGame;

public class AbstainAction implements ActionI {

	@Override
	public boolean isTarget(String selected) {
		return false;
	}

	@Override
	public void execute(WolfGame game, String player) {
		// TODO Auto-generated method stub	
	}

}
