package uk.me.webpigeon.wolf.newcode;

import uk.me.webpigeon.wolf.newcode.actions.WolfUtils;
import uk.me.webpigeon.wolf.newcode.legacy.LegacyUtils;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.behavours.Behavour;
import uk.me.webpigeon.wolf.newcode.players.behavours.BehavourPlayer;
import uk.me.webpigeon.wolf.newcode.players.behavours.DebugAnnounceRole;
import uk.me.webpigeon.wolf.newcode.players.behavours.EatSomeone;
import uk.me.webpigeon.wolf.newcode.players.behavours.LieAboutRole;
import uk.me.webpigeon.wolf.newcode.players.behavours.LynchPrioityTargets;
import uk.me.webpigeon.wolf.newcode.players.behavours.RandomUnsafeLynch;
import uk.me.webpigeon.wolf.newcode.players.behavours.SeerSavingAnnounce;

public class WolfFactory {
	
	private WolfFactory() {
		
	}
	
	public static WolfController buildGame() {
		WolfModel model = new WolfModel();
		WolfController controller = new WolfController(model);

		controller.addPlayer("Fred", buildBehavourPlayer("Fred"));
		controller.addPlayer("John", buildBehavourPlayer("John"));
		controller.addPlayer("Bob", buildBehavourPlayer("Bob"));
		controller.addPlayer("Wolfgang", buildBehavourPlayer("Wolfgang"));
		controller.addPlayer("Pebbles", buildBehavourPlayer("Pebbles"));
		controller.addPlayer("Jackie", buildBehavourPlayer("Jackie"));
		controller.addPlayer("Jess", buildBehavourPlayer("Jess"));
		controller.addPlayer("Sarah", buildBehavourPlayer("Sarah"));
		
		return controller;
	}
	
	public static SessionManager buildBehavourPlayer(String name) {
		Behavour[] behavours = new Behavour[] {
				new SeerSavingAnnounce(),
				new DebugAnnounceRole(),
				new LieAboutRole(),
				new EatSomeone(),
				new LynchPrioityTargets("wolf", "seer"),
				new LynchPrioityTargets("villager", "wolf"),
				new LynchPrioityTargets("seer", "wolf"),
				new RandomUnsafeLynch()
		};
		
		return buildPlayer(name, behavours);
	}
	
	public static SessionManager buildRandomPlayer(String name) {
		Behavour[] behavours = new Behavour[] {
				new RandomUnsafeLynch()
		};
		
		return buildPlayer(name, behavours);
	}
	
	public static SessionManager buildPlayer(String name, Behavour[] behavours) {
		BehavourPlayer player = new BehavourPlayer();
		
		for (Behavour b : behavours) {
			player.addBehavour(b);
		}
		
		Thread t = new Thread(player);
		t.setName("behavour-"+name);
		t.start();
		
		return player;
	}
	
	public static SessionManager wrap(String threadName, GameListener listener) {
		Event2Listener event = new Event2Listener(listener);
		
		Thread t = new Thread(event);
		t.setName(threadName);
		t.start();
		
		return event;
	}

}
