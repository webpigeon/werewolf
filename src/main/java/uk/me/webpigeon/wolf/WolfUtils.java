package uk.me.webpigeon.wolf;

import java.util.Arrays;
import java.util.List;

public class WolfUtils {

	public static GameObserver buildSmartPlayer(String name) {
		return buildRunnablePlayer(new BasicIntelligencePlayer(name));
	}
	
	public static GameObserver buildRandomPlayer(String name) {
		return buildRunnablePlayer(new RandomPlayer(name));
	}
	
	public static GameObserver buildNoopPlayer(String name) {
		return buildRunnablePlayer(new NoopPlayer(name));
	}
	
	public static GameObserver buildRunnablePlayer(AbstractPlayer p) {
		Thread pThread = new Thread(p);
		pThread.setName("player-"+p.getName().toLowerCase());
		pThread.start();
		return p;
	}
	
	public static List<RoleI> buildRoleList() {
		RoleI wolf = new WolfRole();
		RoleI villager = new AbstractRole("villager", Team.VILLAGERS);
		
		return Arrays.asList(wolf, villager, villager, villager, villager, villager, villager);
	}
	
	
}
