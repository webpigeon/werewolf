package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.List;

public interface PlayerController {
	
	public void takeAction(String action);
	public List<String> getActions();
	
}
