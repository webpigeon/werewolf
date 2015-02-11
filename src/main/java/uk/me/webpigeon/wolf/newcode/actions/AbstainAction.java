package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class AbstainAction implements ActionI {
	private String name;
	
	public AbstainAction(String name) {
		this.name = name;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		//VoteService service = controller.getVoteService();
		//service.vote(voter, null);
		controller.announceVote(name, null);
	}
	
	public boolean equals(Object other) {
		return (other != null) && other instanceof AbstainAction;
	}

	@Override
	public boolean isEqual(ActionI action) {
		return (action != null) && action instanceof AbstainAction;
	}
	
	

}
