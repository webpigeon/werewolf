package uk.me.webpigeon.wolf.newcode.actions;

import java.util.Arrays;
import java.util.List;

import uk.me.webpigeon.wolf.AbstractRole;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.SeerRole;
import uk.me.webpigeon.wolf.Team;
import uk.me.webpigeon.wolf.WolfRole;
import uk.me.webpigeon.wolf.newcode.legacy.players.BasicIntelligencePlayer;
import uk.me.webpigeon.wolf.newcode.legacy.players.GameObserver;
import uk.me.webpigeon.wolf.newcode.legacy.players.NoopPlayer;
import uk.me.webpigeon.wolf.newcode.legacy.players.RandomPlayer;

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
