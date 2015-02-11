package uk.me.webpigeon.wolf;

import java.util.Arrays;
import java.util.List;

public class WolfUtils {

	public static GameObserver buildSmartPlayer() {
		return new BasicIntelligencePlayer();
	}
	
	public static GameObserver buildRandomPlayer() {
		return new RandomPlayer();
	}
	
	public static GameObserver buildNoopPlayer() {
		return new NoopPlayer();
	}
	
	public static List<RoleI> buildRoleList() {
		RoleI wolf = new WolfRole();
		RoleI seer = new SeerRole();
		
		return Arrays.asList(wolf, seer);
	}

	public static RoleI getDefaultRole() {
		return new AbstractRole("villager", Team.VILLAGERS);
	}
	
	
}
