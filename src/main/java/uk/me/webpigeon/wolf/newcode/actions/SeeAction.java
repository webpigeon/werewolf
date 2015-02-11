package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class SeeAction implements ActionI {
	
	private String seer;
	private String seen;
	
	public SeeAction(String seer, String seen) {
		this.seer = seer;
		this.seen = seen;
	}

	@Override
	public boolean isTarget(String selected) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		RoleI seenRole = model.getRole(seer);
		controller.sendRole(seer, seen, seenRole);
	}
	
	public String toString() {
		return "see "+seen;
	}

}
