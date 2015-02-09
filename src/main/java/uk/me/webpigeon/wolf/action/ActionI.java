package uk.me.webpigeon.wolf.action;

import uk.me.webpigeon.wolf.WolfGame;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public interface ActionI {
	
	public void execute(WolfGame game, String player);
	public void execute(WolfController controller, WolfModel model);

	public boolean isTarget(String selected);

}
