package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.List;

import uk.me.webpigeon.wolf.action.ActionI;

public interface GameController {
	
	public void talk(String message);
	public void act(ActionI action);
	public Collection<ActionI> getLegalActions();
	
	public GameState getStage();
	public List<String> getAlivePlayers();
	
	public String getName();
	public void setState(GameState newState);
	public void setRole(RoleI role);

}
