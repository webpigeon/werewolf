package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.WolfGame;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.actions.AdvanceTurn;

public abstract class VoteAction implements ActionI {
	
	private String voteVerb;
	private String name;
	private String candidate;
	private boolean publicVote;
	
	public VoteAction(String candidate, boolean publicVote) {
		this.voteVerb = "vote for";
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	public VoteAction(String verb, String candidate, boolean publicVote) {
		this.voteVerb = verb;
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	
	public VoteAction(String verb, String voter, String candidate, boolean publicVote) {
		this.voteVerb = verb;
		this.name = voter;
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	public void execute(WolfGame game, String player) {
		game.enterVote(player, candidate, publicVote);
	}
	
	public boolean isTarget(String name) {
		return candidate.equals(name);
	}
	
	@Override
	public String toString() {
		return voteVerb+" "+candidate;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		VoteService<String> service = controller.getVoteService();
		if (service != null) {
			service.vote(name, candidate);
			
			if (publicVote) {
				controller.announceVote(name, candidate);
			}
			
			if (service.isFinished()) {
				controller.addTask(new AdvanceTurn());
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoteAction other = (VoteAction) obj;
		if (candidate == null) {
			if (other.candidate != null)
				return false;
		} else if (!candidate.equals(other.candidate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publicVote != other.publicVote)
			return false;
		if (voteVerb == null) {
			if (other.voteVerb != null)
				return false;
		} else if (!voteVerb.equals(other.voteVerb))
			return false;
		return true;
	}
	
	
}
