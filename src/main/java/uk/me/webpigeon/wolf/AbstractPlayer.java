package uk.me.webpigeon.wolf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.Timer;

import uk.me.webpigeon.wolf.action.ActionI;
public abstract class AbstractPlayer implements ActionListener, GameObserver, Runnable {
	private static final Integer MAX_THINK_DELTA = 500;
	private static final Integer MIN_THINK_TIME = 5000;
	
	private Timer timer;
	private Random random;
	private String name;
	protected Map<String, String> roles;
	protected GameController controller;
	private boolean alive;
	
	public AbstractPlayer() {
		this.timer = new Timer(MIN_THINK_TIME, this);
		this.roles = new TreeMap<String, String>();
		this.controller = null;
		this.random = new Random();
		this.alive = true;
	}
	
	public void bind(GameController controller) {
		this.controller = controller;
		this.name = controller.getName();
		timer.start();
	}
	
	public String getRole() {
		return roles.get(name);
	}
	
	public final String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	@Override
	public void notifyDaytime(PlayerController controller) {
		timer.restart();
		clearTurnLocks();
	}

	@Override
	public void notifyNighttime(PlayerController controller) {
		timer.restart();
		clearTurnLocks();
	}
	
	@Override
	public void notifyRole(String player, RoleI role) {		
		roles.put(player, role.getName());
		think("I think that "+player+" is a "+role);
	}
	
	protected void think(String thought) {
		System.out.println("*"+name+"* "+thought);
	}

	@Override
	public void notifyVote(String voter, String votee) {
	}

	@Override
	public void notifyDeath(String who, String how) {
		if (who.equals(name)) {
			alive = false;
			timer.stop();
		}
	}
	
	@Override
	public void notifyMessage(String who, String what) {
		
	}
	
	@Override
	public void run() {
		
		while (!Thread.interrupted() && alive) {
			if (controller != null) {
				GameState state = controller.getStage();
				if (state == GameState.DAYTIME || state == GameState.NIGHTTIME) {
					takeAction(controller.getLegalActions());
				} else if (state == GameState.GAMEOVER) {
					think("The game is over");
					alive = false;
				}
			}
			
			try{
				Thread.sleep(MIN_THINK_TIME + random.nextInt(MAX_THINK_DELTA));
			} catch (InterruptedException ex) {
				
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		triggerAction();
	}
	
	protected void triggerAction() {
		if (!alive) {
			return;
		}
		
		GameState state = controller.getStage();
		
		if (state == GameState.DAYTIME) {
			takeAction(controller.getLegalActions());
			timer.restart();
		} else if (state == GameState.NIGHTTIME) {
			takeAction(controller.getLegalActions());
			timer.restart();
		} else if (state == GameState.GAMEOVER) {
			think("the game is over");
			timer.stop();
		}
	}

	protected abstract void takeAction(Collection<ActionI> collection);
	protected abstract void clearTurnLocks();

}
