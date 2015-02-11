package uk.me.webpigeon.wolf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.Timer;

import uk.me.webpigeon.wolf.action.ActionI;
public abstract class AbstractPlayer implements ActionListener, GameObserver {
	private static final Integer THINK_MULTIPLIER = 20;
	private static final Integer MIN_THINK_TIME = 500;
	
	private Random random;
	private String name;
	protected Map<String, String> roles;
	protected GameController controller;
	private boolean alive;
	
	public AbstractPlayer() {
		this.roles = new TreeMap<String, String>();
		this.controller = null;
		this.random = new Random();
		this.alive = true;
	}
	
	public void bind(GameController controller) {
		this.controller = controller;
		this.name = controller.getName();
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
		clearTurnLocks();
	}

	@Override
	public void notifyNighttime(PlayerController controller) {
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
		}
	}
	
	@Override
	public void notifyMessage(String who, String what) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		triggerAction();
	}
	
	@Override
	public final void triggerAction() {
		if (!alive) {
			return;
		}
		
		GameState state = controller.getStage();
		
		if (state == GameState.DAYTIME) {
			takeAction(controller.getLegalActions());
		} else if (state == GameState.NIGHTTIME) {
			takeAction(controller.getLegalActions());
		} else if (state == GameState.GAMEOVER) {
			think("the game is over");
		}
	}

	protected abstract void takeAction(Collection<ActionI> collection);
	protected abstract void clearTurnLocks();

}
