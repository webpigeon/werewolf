package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.Team;
import uk.me.webpigeon.wolf.newcode.TeamFilter;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.ChatMessage;
import uk.me.webpigeon.wolf.newcode.events.EventI;

public class TalkAction extends NewAction {
	private String message;
	private String channel;
	
	public TalkAction(String player, String message) {
		this.message = message;
		this.channel = "public";
	}
	
	public TalkAction(String player, String message, String channel) {
		assert channel == "public" || channel == "wolf";
		this.message = message;
		this.channel = channel;
	}

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		if (!model.isAlivePlayer(name)) {
			//the dead cannot speak
			return;
		}
		
		
		EventI event = new ChatMessage(name, message, channel);
				
		if ("wolf".equals(channel)) {
			RoleI role = model.getRole(name);
			if (!role.isOnTeam(Team.WOLVES)) {
				// non wolf players cannot use wolf-chat
				return;
			}
			
			controller.multicast(event, new TeamFilter(Team.WOLVES));
		} else {
			controller.broadcast(event);
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
