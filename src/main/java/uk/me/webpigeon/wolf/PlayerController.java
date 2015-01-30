package uk.me.webpigeon.wolf;

import java.util.List;

public interface PlayerController {
	
	public void talk(String message);
	public void takeAction(String action);
	public List<String> getActions();
	public List<String> getPlayers();
	public GameState getStage();
	
}
