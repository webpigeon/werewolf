package uk.me.webpigeon.wolf;

public interface Player {
	
	public String getName();

	public void notifyRole(Player p, Role r);

	public void notifyDaytime(PlayerController controller);
	public void notifyNighttime(PlayerController controller);
	
}
