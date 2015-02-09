package uk.me.webpigeon.wolf;

import java.util.Arrays;
import java.util.List;

public class WolfUtils {

	public static GameObserver buildSmartPlayer() {
		return buildRunnablePlayer(new BasicIntelligencePlayer());
	}
	
	public static GameObserver buildRandomPlayer() {
		return buildRunnablePlayer(new RandomPlayer());
	}
	
	public static GameObserver buildNoopPlayer() {
		return buildRunnablePlayer(new NoopPlayer());
	}
	
	public static GameObserver buildRunnablePlayer(AbstractPlayer p) {
		Thread pThread = new Thread(p);
		pThread.setName("player-thread-"+System.nanoTime());
		pThread.start();
		return p;
	}
	
	public static List<RoleI> buildRoleList() {
		RoleI wolf = new WolfRole();
		
		return Arrays.asList(wolf);
	}

	public static RoleI getDefaultRole() {
		return new AbstractRole("villager", Team.VILLAGERS);
	}
	
	
}
