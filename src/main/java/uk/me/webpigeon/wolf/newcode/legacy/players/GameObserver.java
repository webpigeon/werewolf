package uk.me.webpigeon.wolf.newcode.legacy.players;

import uk.me.webpigeon.wolf.GameController;
import uk.me.webpigeon.wolf.PlayerController;
import uk.me.webpigeon.wolf.RoleI;

public interface GameObserver {
	
	public void bind(GameController c);
	
	public void notifyRole(String name, RoleI r);
	public void notifyVote(String voter, String votee);
	public void notifyMessage(String who, String message);
	public void notifyDeath(String name, String cause);
	
	public void notifyDaytime(PlayerController controller);
	public void notifyNighttime(PlayerController controller);
	
	public String getName();

	public void triggerAction();


}
