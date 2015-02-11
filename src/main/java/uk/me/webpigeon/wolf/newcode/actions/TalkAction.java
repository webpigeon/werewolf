package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.ChatMessage;

public class TalkAction extends NewAction {
	private String player;
	private String message;
	
	public TalkAction(String player, String message) {
		this.player = player;
		this.message = message;
	}

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		if (model.isAlivePlayer(player)) {
			controller.broadcast(new ChatMessage(name, message, "public"));
		}
	}

	public String toString() {
		return "talk("+player+","+message+")";
	}

	@Override
	public boolean isEqual(ActionI action) {
		return false;
	}
	
}
