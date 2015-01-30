package uk.me.webpigeon.wolf;

public interface GameObserver {
	
	public void bind(GameController c);
	
	public void notifyRole(String name, RoleI r);
	public void notifyVote(String voter, String votee);
	public void notifyMessage(String who, String message);
	public void notifyDeath(String name, String cause);
	
	public void notifyDaytime(PlayerController controller);
	public void notifyNighttime(PlayerController controller);
	
	public String getName();


}
