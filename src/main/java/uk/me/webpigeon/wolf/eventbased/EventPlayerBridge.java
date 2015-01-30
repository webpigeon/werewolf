package uk.me.webpigeon.wolf.eventbased;

import java.util.concurrent.BlockingQueue;

import uk.me.webpigeon.wolf.GameObserver;
import uk.me.webpigeon.wolf.RoleI;

/**
 * Bridges the event system and the (old) player class
 *
 */
public class EventPlayerBridge implements Runnable, EventPlayer {
	private GameObserver player;
	private BlockingQueue<Event> events;

	public EventPlayerBridge(GameObserver p) {
		this.player = p;
	}

	@Override
	public void run() {
		
		Event event = events.poll();
		while (event != null) {
			
			switch (event.getType()) {
				case PLAYER_ROLE:
					PlayerRoleEvent e = (PlayerRoleEvent)event;
					String username = e.getUsername();
					RoleI role = e.getRole();
					player.notifyRole(username, role);
					
				case PLAYER_DEATH:
					//player.notifyDeath(p, h);
					
				case STATE_CHANGE:
					//if state == day || if state == night
			}
			
			event = events.poll();
		}
	}

	@Override
	public void notifyEvent(Event event) {
		events.add(event);
	}

	@Override
	public String getName() {
		return player.getName();
	}
	
}
