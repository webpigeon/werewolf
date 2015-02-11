package uk.me.webpigeon.wolf.gui;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.newcode.GameListener;
import uk.me.webpigeon.wolf.newcode.SessionManager;
import uk.me.webpigeon.wolf.newcode.events.ChatMessage;
import uk.me.webpigeon.wolf.newcode.events.EventI;
import uk.me.webpigeon.wolf.newcode.events.GameStarted;
import uk.me.webpigeon.wolf.newcode.events.PlayerDeath;
import uk.me.webpigeon.wolf.newcode.events.PlayerRole;
import uk.me.webpigeon.wolf.newcode.events.PlayerVote;
import uk.me.webpigeon.wolf.newcode.events.StateChanged;

/**
 * Converts and event stream into method calls
 *
 */
public class Event2Listener implements Runnable, SessionManager {
	private BlockingQueue<EventI> eventQueue;
	private GameListener listener;
	
	public Event2Listener(GameListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			processEvents();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processEvents() throws InterruptedException {
		
		do {
			EventI event = eventQueue.take();
			
			switch(event.getType()) {
				case "gameStarted":
					GameStarted gs = (GameStarted)event;
					listener.onGameStart(gs.players);
					break;
			
				case "role":
					PlayerRole role = (PlayerRole)event;
					listener.onDiscoverRole(role.name, role.role);
					break;
					
				case "stateChange":
					StateChanged sc = (StateChanged)event;
					listener.onStateChange(sc.newState);
					break;

				case "death":
					PlayerDeath pd = (PlayerDeath)event;
					listener.onDeath(pd.player, pd.cause);
					break;
					
				case "vote":
					PlayerVote pv = (PlayerVote)event;
					listener.onVoteEntered(pv.player, pv.vote);
					break;
					
				case "chat":
					ChatMessage cm = (ChatMessage)event;
					listener.onMessage(cm.player, cm.message, cm.channel);
					break;
					
				default:
					System.err.println("unknown event type "+event.getType());
			}
			
		} while(!Thread.interrupted());
	}

	@Override
	public void bind(String name, Queue<ActionI> actionQueue, BlockingQueue<EventI> eventQueue) {
		this.eventQueue = eventQueue;
		new Thread(this).start();
	}

}
