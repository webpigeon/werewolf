package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.WolfGame;

public abstract class VoteAction implements ActionI {
	
	private String candidate;
	private boolean publicVote;
	
	public VoteAction(String candidate, boolean publicVote) {
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

}
