package uk.me.webpigeon.wolf;

public interface GameObserver {
	
	public void notifyRole(Player p, Role r);
	public void notifyVote(String voter, String votee);
	
	public void notifyDaytime(PlayerController controller);
	public void notifyNighttime(PlayerController controller);


}
