package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;


public class EatAction extends VoteAction {

	public EatAction(String candidate) {
		super("eat", candidate, false);
	}

	@Override
	protected boolean isValid(String name, WolfController controller, WolfModel model) {
		GameState state = controller.getState();
		if (state != GameState.NIGHTTIME) {
			return false;
		}
		
		RoleI role = model.getRole(name);
		if (!"wolf".equals(role.getName())){
			System.err.println("[BUG] "+name+" has turned into canniabal");
			return false;
		}
		
		return model.isAlivePlayer(name);
	}

}
