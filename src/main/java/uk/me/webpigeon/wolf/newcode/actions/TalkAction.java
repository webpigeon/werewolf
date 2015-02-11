package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class TalkAction extends NewAction {
	private String player;
	private String message;
	
	public TalkAction(String player, String message) {
		this.player = player;
		this.message = message;
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		if (model.isAlivePlayer(player)) {
			controller.announceMessage(player, message, "public");
		}
	}

	public String toString() {
		return "talk("+player+","+message+")";
	}
	
}
