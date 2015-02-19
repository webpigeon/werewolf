package uk.me.webpigeon.wolf.newcode;

import uk.me.webpigeon.wolf.newcode.actions.WolfUtils;
import uk.me.webpigeon.wolf.newcode.legacy.LegacyUtils;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.MultiMapFacts;
import uk.me.webpigeon.wolf.newcode.players.SelectionPlayer;
import uk.me.webpigeon.wolf.newcode.players.SelectionStrategy;
import uk.me.webpigeon.wolf.newcode.players.behavours.DetectLiers;
import uk.me.webpigeon.wolf.newcode.players.behavours.ProductionRule;
import uk.me.webpigeon.wolf.newcode.players.behavours.DebugAnnounceRole;
import uk.me.webpigeon.wolf.newcode.players.behavours.EatSomeone;
import uk.me.webpigeon.wolf.newcode.players.behavours.LieAboutRole;
import uk.me.webpigeon.wolf.newcode.players.behavours.LynchPrioityTargets;
import uk.me.webpigeon.wolf.newcode.players.behavours.RandomUnsafeLynch;
import uk.me.webpigeon.wolf.newcode.players.behavours.RuleBasedStrategy;
import uk.me.webpigeon.wolf.newcode.players.behavours.SeerSavingAnnounce;

public class WolfFactory {
	
	private WolfFactory() {
		
	}
	
	public static WolfController buildGame() {
		WolfModel model = new WolfModel();
		WolfController controller = new WolfController(model);

		controller.addPlayer("Fred", buildRandomPlayer("Fred"));
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
		ProductionRule[] behavours = new ProductionRule[] {
				new DetectLiers(),
				new SeerSavingAnnounce(),
				new DebugAnnounceRole(),
				new LieAboutRole(),
				new EatSomeone(),
				new LynchPrioityTargets("seer", "wolf"),
				new LynchPrioityTargets("wolf", "seer"),
				new LynchPrioityTargets("villager", "wolf"),
				//new RandomUnsafeLynch()
		};
		
		return buildPlayer(name, behavours);
	}
	
	public static SessionManager buildRandomPlayer(String name) {
		ProductionRule[] behavours = new ProductionRule[] {
				new RandomUnsafeLynch()
		};
		
		return buildPlayer(name, behavours);
	}
	
	public static SessionManager buildPlayer(String name, ProductionRule[] behavours) {
		RuleBasedStrategy rbs = new RuleBasedStrategy();
		
		for (ProductionRule b : behavours) {
			rbs.addRule(b);
		}
		
		return buildPlayer(name, rbs);
	}
	
	public static SessionManager buildPlayer(String name, SelectionStrategy strat) {
		SelectionPlayer player = new SelectionPlayer(strat, new MultiMapFacts());
		
		Thread t = new Thread(player);
		t.setName("player-"+name);
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
