package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.PlayerVote;

public class AbstainAction implements ActionI {

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		//VoteService service = controller.getVoteService();
		//service.vote(voter, null);
		
		controller.broadcast(new PlayerVote(name, null));
	}
	
	public boolean equals(Object other) {
		return (other != null) && other instanceof AbstainAction;
	}

	@Override
	public boolean isEqual(ActionI action) {
		return (action != null) && action instanceof AbstainAction;
	}
	
	public String toString(){
		return "abstain";
	}

}
