package uk.me.webpigeon.wolf.newcode.events;

import uk.me.webpigeon.wolf.RoleI;


public class PlayerRole implements EventI {
	public final String name;
	public final RoleI role;
	
	public PlayerRole(String name, RoleI role) {
		this.name = name;
		this.role = role;
	}
	
	public String getType() {
		return "role";
	}
	
	public String toString() {
		return "role("+name+","+role+")";
	}
}
