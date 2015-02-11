package uk.me.webpigeon.wolf.newcode.legacy;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.events.*;
import uk.me.webpigeon.wolf.newcode.legacy.players.GameObserver;

public class LegacyWrapper implements Runnable, SessionManager {
	private GameObserver player;
	private LegacyController c;
	private Thread t;
	
	private Queue<ActionI> actionQueue;
	private BlockingQueue<EventI> eventQueue;
	
	public LegacyWrapper(GameObserver player, LegacyController c) {
		this.player = player;
		this.c = c;
	}

	public void processEvents() throws InterruptedException {
		
		do {
			EventI event = eventQueue.take();
			
			switch(event.getType()) {
				case "role":
					PlayerRole role = (PlayerRole)event;
					player.notifyRole(role.name, role.role);
					break;
					
				case "stateChange":
					StateChanged sc = (StateChanged)event;
					onStateChange(sc.newState);
					break;

				case "death":
					PlayerDeath pd = (PlayerDeath)event;
					player.notifyDeath(pd.player, pd.cause);
					break;
					
				case "vote":
					PlayerVote pv = (PlayerVote)event;
					player.notifyVote(pv.player, pv.vote);
					break;
					
				case "gameStarted":
					break;
					
				case "chat":
					ChatMessage cm = (ChatMessage)event;
					if ("public".equals(cm.channel)) {
						player.notifyMessage(cm.player, cm.message);
					}
					break;
					
				default:
					System.err.println("unknown event type "+event.getType());
			}
			
			player.triggerAction();
			
		} while(!Thread.interrupted());
	}

	private void onStateChange(GameState newState) {
		switch (newState) {
			case DAYTIME:
				player.notifyDaytime(null);
			case NIGHTTIME:
				player.notifyNighttime(null);
			default:
				// player does not deal with any other cases
		}
		
	}

	@Override
	public void bind(String name, Queue<ActionI> actionQueue, BlockingQueue<EventI> eventQueue) {
		this.actionQueue = actionQueue;
		this.eventQueue = eventQueue;
		
		c.setName(name);
		player.bind(c);
		
		t = new Thread(this);
		t.setName("legacy-"+name+"-adapter");
		t.start();
	}

	@Override
	public void run() {
		try {
			processEvents();
		} catch (InterruptedException ex ){
			ex.printStackTrace();
		}
	}

}
