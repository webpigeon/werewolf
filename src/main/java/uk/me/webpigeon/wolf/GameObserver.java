package uk.me.webpigeon.wolf;

public interface GameObserver {
	
	public void notifyRole(Player p, Role r);

	public void notifyDaytime(PlayerController controller);
	public void notifyNighttime(PlayerController controller);

}
