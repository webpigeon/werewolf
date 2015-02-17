package uk.me.webpigeon.wolf.newcode.players;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.GameListener;
import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.EatAction;
import uk.me.webpigeon.wolf.newcode.actions.LynchAction;
import uk.me.webpigeon.wolf.newcode.actions.SeeAction;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;
import uk.me.webpigeon.wolf.newcode.events.EventI;

public class HumanPlayer implements GameListener {
	private String name;
	private WolfController controller;
	
	public void say(String message){
		controller.addTask(name, new TalkAction(null, message));
	}
	
	public void perform(String action, String[] args){
		
		if ("eat".equals(action) || "e".equals(action)) {
			assert args.length == 2;
			controller.addTask(name, new EatAction(args[1]));
		}
		
		if ("lynch".equals(action) || "l".equals(action)) {
			assert args.length == 2;
			controller.addTask(name, new LynchAction(args[1]));
		}
		
		if ("see".equals(action) || "s".equals(action)) {
			assert args.length == 2;
			controller.addTask(name, new SeeAction(args[1]));
		}
		
	}

	@Override
	public void onDiscoverRole(String player, RoleI role) {
		System.out.println("you are a "+role);
	}

	@Override
	public void onJoin(String name, WolfController controller) {
		this.name = name;
		this.controller = controller;
		System.out.println("you have joined the game as "+name);
	}

	@Override
	public void onStateChange(GameState newState) {
		System.out.println("The game state is now "+newState);
	}

	@Override
	public void onGameStart(Collection<String> players) {
		System.out.println("A new game has started, players are "+players);
	}

	@Override
	public void onMessage(String player, String message, String channel) {
		System.out.println("message from "+player+" "+message);
	}

	@Override
	public void onVoteEntered(String voter, String candidate) {
		System.out.println(voter+" has voted for "+candidate);
	}

	@Override
	public void onDeath(String player, String cause, String role) {
		System.out.println(player+" has died "+cause+" they were "+role);
	}
	
}
