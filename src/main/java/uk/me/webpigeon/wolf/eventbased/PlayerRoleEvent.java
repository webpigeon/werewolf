package uk.me.webpigeon.wolf.eventbased;

import uk.me.webpigeon.wolf.RoleI;

public class PlayerRoleEvent implements Event{
	private String username;
	private RoleI role;
	
	public PlayerRoleEvent(String username, RoleI role) {
		this.username = username;
		this.role = role;
	}

	@Override
	public EventType getType() {
		return EventType.PLAYER_ROLE;
	}

	public String getUsername() {
		return username;
	}

	public RoleI getRole() {
		return role;
	}

}
