package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public abstract class VoteAction extends NewAction {
	
	private String voteVerb;
	protected final String name;
	private String candidate;
	private boolean publicVote;
	
	public VoteAction(String candidate, boolean publicVote) {
		super(GameState.DAYTIME, GameState.NIGHTTIME);
		this.name = null;
		this.voteVerb = "vote for";
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	public VoteAction(String verb, String candidate, boolean publicVote) {
		super(GameState.DAYTIME, GameState.NIGHTTIME);
		this.name = null;
		this.voteVerb = verb;
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	
	public VoteAction(String verb, String voter, String candidate, boolean publicVote) {
		super(GameState.DAYTIME, GameState.NIGHTTIME);
		this.voteVerb = verb;
		this.name = voter;
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	@Override
	public String toString() {
		return name+" "+voteVerb+" "+candidate;
	}
	
	protected abstract boolean isValid(WolfController controller, WolfModel model);

	@Override
	public void execute(WolfController controller, WolfModel model) {
		super.execute(controller, model);
		
		if (isValid(controller, model)) {
			VoteService<String> service = controller.getVoteService();
			if (service != null) {
				service.vote(name, candidate);
				
				if (publicVote) {
					controller.announceVote(name, candidate);
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			return voteVerb.equals(other.voteVerb) && candidate.equals(other.candidate) && name.equals(other.name);
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
			return voteVerb.equals(other.voteVerb) && candidate.equals(other.candidate) && name.equals(other.name);
		} catch (ClassCastException ex) {
			return false;
		}
	}
	
	
}
