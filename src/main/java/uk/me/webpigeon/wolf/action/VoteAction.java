package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.WolfGame;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.actions.AdvanceTurn;

public abstract class VoteAction implements ActionI {
	
	private String name;
	private String candidate;
	private boolean publicVote;
	
	public VoteAction(String candidate, boolean publicVote) {
		this.candidate = candidate;
		this.publicVote = publicVote;
	}
	
	public VoteAction(String voter, String candidate, boolean publicVote) {
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
		return "lynch "+candidate;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		VoteService<String> service = controller.getVoteService();
		if (service != null) {
			service.vote(name, candidate);
			
			if (service.isFinished()) {
				controller.addTask(new AdvanceTurn());
			}
		}
	}
}
