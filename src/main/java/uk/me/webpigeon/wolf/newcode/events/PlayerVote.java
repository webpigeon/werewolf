package uk.me.webpigeon.wolf.newcode.events;


public class PlayerVote implements EventI {
	public final String player;
	public final String vote;
	
	public PlayerVote(String player, String vote) {
		this.player = player;
		this.vote = vote;
	}
	
	public String getType() {
		return "vote";
	}

}
