package uk.me.webpigeon.wolf.newcode.legacy.players;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.Timer;

import uk.me.webpigeon.wolf.GameController;
import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.PlayerController;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
public abstract class AbstractPlayer implements ActionListener, GameObserver {
	private String name;
	protected RoleI myRole;
	protected Map<String, String> roles;
	protected GameController controller;
	private boolean alive;
	
	public AbstractPlayer() {
		this.roles = new TreeMap<String, String>();
		this.controller = null;
		this.alive = true;
	}
	
	public void bind(GameController controller) {
		this.controller = controller;
		this.name = controller.getName();
	}
	
	public String getRole() {
		return myRole.getName();
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
		if (name.equals(player) && myRole == null) {
			this.myRole = role;
		}
		
		roles.put(player, role.getName());
		think("I think that "+player+" is a "+role);
	}
	
	protected void think(String thought) {
		//System.out.println("*"+name+"* "+thought);
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
