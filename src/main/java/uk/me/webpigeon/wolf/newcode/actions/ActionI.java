package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public interface ActionI {
	
	public void execute(String user, WolfController controller, WolfModel model);
	public boolean isEqual(ActionI action);

}
