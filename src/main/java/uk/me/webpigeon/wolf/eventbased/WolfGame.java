package uk.me.webpigeon.wolf.eventbased;

import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import uk.me.webpigeon.wolf.action.ActionI;

public class WolfGame implements Runnable {
	private GameModel model;
	private BlockingQueue<ActionWrapper> actions;
	private Map<String, EventPlayer> players;
	
	public WolfGame(){
		this.model = new GameModel();
		this.actions = new LinkedBlockingQueue<ActionWrapper>();
		this.players = new TreeMap<String, EventPlayer>();
	}
	
	public void run() {
		
		processActions();
		
	}
	
	public void addPlayer(EventPlayer player) {
		players.put(player.getName(), player);
	}
	
	public void addAction(String name, ActionI action) {
		ActionWrapper wrapper = new ActionWrapper();
		wrapper.player = name;
		wrapper.action = action;
		actions.add(wrapper);
	}
	
	private void processActions() {
		ActionWrapper wrapper = actions.poll();
		while (wrapper != null) {
			
			ActionI action = wrapper.action;
			//action.execute(model, wrapper.player);
			
			wrapper = actions.poll();
		}
	}
	
	private void notify(Event event) {
		for (EventPlayer p : players.values()){
			p.notifyEvent(event);
		}
	}
	

	private class ActionWrapper {
		String player;
		ActionI action;
	}
	
}
