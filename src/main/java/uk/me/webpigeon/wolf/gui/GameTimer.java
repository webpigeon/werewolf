package uk.me.webpigeon.wolf.gui;

import java.util.Collection;
import java.util.Queue;

import javax.swing.Timer;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.GameListener;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;

public class GameTimer implements GameListener {
	private static final Integer TURN_TIME_LIMIT = 10000;
	private Timer timer;

	public GameTimer(WolfController controller) {
		this.timer = new Timer(TURN_TIME_LIMIT, new TimeoutTask(controller));
	}
	
	@Override
	public void onStateChange(GameState newState) {
		if (newState == GameState.DAYTIME || newState == GameState.NIGHTTIME) {
			timer.restart();
		} else {
			timer.stop();
		}
	}

	@Override
	public void onGameStart(Collection<String> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoin(String name, Queue<ActionI> actionQueue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDiscoverRole(String player, RoleI role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String player, String message, String channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeath(String victim, String cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVoteEntered(String voter, String candidate) {
		// TODO Auto-generated method stub
		
	}

}
