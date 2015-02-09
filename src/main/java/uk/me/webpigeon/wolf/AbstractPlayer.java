package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import uk.me.webpigeon.wolf.action.ActionI;
public abstract class AbstractPlayer implements GameObserver, Runnable {
	private static final Integer MAX_THINK_DELTA = 500;
	private static final Integer MIN_THINK_TIME = 5000;
	
	private Random random;
	private String name;
	protected Map<String, RoleI> roles;
	protected GameController controller;
	private boolean alive;
	
	public AbstractPlayer() {
		this.roles = new TreeMap<String, RoleI>();
		this.controller = null;
		this.random = new Random();
		this.alive = true;
	}
	
	public void bind(GameController controller) {
		this.controller = controller;
		this.name = controller.getName();
	}
	
	public RoleI getRole() {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyNighttime(PlayerController controller) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void notifyRole(String player, RoleI role) {		
		roles.put(player, role);
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

	protected abstract void takeAction(Collection<ActionI> collection);

}
