package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.WolfGame;

public interface ActionI {
	
	public void execute(WolfGame game, String player);

	public boolean isTarget(String selected);

}
