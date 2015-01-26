package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PlayerController {
	
	public void takeAction(String action);
	public List<String> getActions();
	public List<Player> getPlayers();
	
}
