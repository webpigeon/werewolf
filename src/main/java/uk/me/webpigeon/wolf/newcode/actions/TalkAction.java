package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.ChatMessage;

public class TalkAction extends NewAction {
	private String message;
	
	public TalkAction(String player, String message) {
		this.message = message;
	}

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		if (model.isAlivePlayer(name)) {
			controller.broadcast(new ChatMessage(name, message, "public"));
		}
	}

	public String toString() {
		return "talk("+message+")";
	}

	@Override
	public boolean isEqual(ActionI action) {
		return false;
	}
	
}
