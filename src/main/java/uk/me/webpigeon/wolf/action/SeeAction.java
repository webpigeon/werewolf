package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.WolfGame;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class SeeAction implements ActionI {
	
	private String name;
	
	public SeeAction(String name) {
		this.name = name;
	}

	@Override
	public void execute(WolfGame game, String player) {
		//RoleI role = game.getPlayerRole(name);
		
	}

	@Override
	public boolean isTarget(String selected) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		
	}

}
