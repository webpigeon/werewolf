package uk.me.webpigeon.wolf;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public abstract class AbstractPlayer implements Player, Runnable {
	private static final Integer MAX_THINK_TIME = 500;
	
	private Random random;
	private String name;
	private Role role;
	protected Map<String, Role> roles;
	protected PlayerController controller;
	private long nextActionTime;
	
	public AbstractPlayer(String name) {
		this.name = name;
		this.role = null;
		this.roles = new TreeMap<String, Role>();
		this.controller = null;
		this.random = new Random();
		this.nextActionTime = Long.MAX_VALUE;
	}
	
	public Role getRole() {
		return role;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public void notifyRole(Player player, Role role) {
		if (player.equals(this)) {
			this.role = role;
		}
		
		roles.put(player.getName(), role);
		think("I think that "+player.getName()+" is a "+role);
	}

	public synchronized void notifyDaytime(PlayerController controller) {
		this.controller = controller;
		nextActionTime = System.currentTimeMillis() + random.nextInt(MAX_THINK_TIME);
		think("It's daytime");
		notifyAll();
	}

	public synchronized void notifyNighttime(PlayerController controller) {
		this.controller = controller;
		nextActionTime = System.currentTimeMillis() + random.nextInt(MAX_THINK_TIME);
		think("It's nighttime");
		notifyAll();
	}
	
	protected void think(String thought) {
		System.out.println("*"+name+"* "+thought);
	}

	@Override
	public void notifyVote(String voter, String votee) {
	}

	@Override
	public synchronized void run() {
		
		Random r = new Random();
		
		while(!Thread.interrupted()) {
			
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			long currentTime = System.currentTimeMillis();
			if (currentTime <= nextActionTime) {
				takeAction(controller);
			} else {
				try {
					Thread.sleep(currentTime - nextActionTime);
				} catch (InterruptedException e) {
				}
			}
			
		}
		
	}
	
	protected abstract void takeAction(PlayerController controller);

}
