package uk.me.webpigeon.wolf.newcode.events;


public class PlayerDeath implements EventI {
	public final String player;
	public final String cause;
	public final String role;
	
	public PlayerDeath(String player, String role, String cause) {
		this.player = player;
		this.role = role;
		this.cause = cause;
	}
	
	public String getType() {
		return "death";
	}

}
