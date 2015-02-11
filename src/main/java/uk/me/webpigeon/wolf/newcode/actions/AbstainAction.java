package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class AbstainAction implements ActionI {

	@Override
	public boolean isTarget(String selected) {
		return false;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		VoteService service = controller.getVoteService();
		//service.vote(voter, null);
	}

}
