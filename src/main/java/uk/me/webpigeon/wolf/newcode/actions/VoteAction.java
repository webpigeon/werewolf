package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.PlayerVote;

public abstract class VoteAction extends NewAction {
	
	private String voteVerb;
	private String candidate;
	private boolean publicVote;
	
	public VoteAction(String candidate, boolean publicVote) {
		super(GameState.DAYTIME, GameState.NIGHTTIME);
		this.voteVerb = "vote for";
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	public VoteAction(String verb, String candidate, boolean publicVote) {
		super(GameState.DAYTIME, GameState.NIGHTTIME);
		this.voteVerb = verb;
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	@Override
	public String toString() {
		return voteVerb+" "+candidate;
	}
	
	protected abstract boolean isValid(String name, WolfController controller, WolfModel model);

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		//super.execute(name, controller, model);
		if (!isPermittedState(controller.getState())){
			return;
		}
		
		if (isValid(name, controller, model)) {
			VoteService<String> service = controller.getVoteService();
			if (service != null) {
				service.vote(name, candidate);
				
				if (publicVote) {
					controller.broadcast(new PlayerVote(name, candidate));
				}
				
				/*if (service.isFinished()) {
					controller.addTask(new AdvanceTurn());
				}*/
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((candidate == null) ? 0 : candidate.hashCode());
		result = prime * result + (publicVote ? 1231 : 1237);
		result = prime * result
				+ ((voteVerb == null) ? 0 : voteVerb.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		try {
			VoteAction other = (VoteAction)obj;
			return voteVerb.equals(other.voteVerb) && candidate.equals(other.candidate);
		} catch (ClassCastException ex) {
			return false;
		}
	}
	
	@Override
	public boolean isEqual(ActionI action) {
		if (action == null) {
			return false;
		}
		
		try {
			VoteAction other = (VoteAction)action;
			return voteVerb.equals(other.voteVerb) && candidate.equals(other.candidate);
		} catch (ClassCastException ex) {
			return false;
		}
	}
	
	public String getTarget() {
		return candidate;
	}
}
