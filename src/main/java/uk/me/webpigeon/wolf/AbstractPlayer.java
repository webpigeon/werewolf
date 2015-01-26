package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class AbstractPlayer implements Player {
	private String name;
	private Role role;
	protected Map<String, Role> roles;
	
	public AbstractPlayer(String name) {
		this.name = name;
		this.role = null;
		this.roles = new TreeMap<String, Role>();
	}
	
	public Role getRole() {
		return role;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public void notifyRole(Player player, Role role) {
		if (player.equals(this)) {
			this.role = role;
		}
		
		roles.put(player.getName(), role);
		think("I think that "+player.getName()+" is a "+role);
	}

	public void notifyDaytime(PlayerController controller) {
		// TODO Auto-generated method stub
		
		Collection<String> actions = controller.getActions();
		
		think("It's daytime");
		think("I can: "+actions);
	}

	public void notifyNighttime(PlayerController controller) {
		// TODO Auto-generated method stub
		
		Collection<String> actions = controller.getActions();
		
		think("It's nighttime");
		think("I can: "+actions);
	}
	
	protected void think(String thought) {
		System.out.println("*"+name+"* "+thought);
	}

}
