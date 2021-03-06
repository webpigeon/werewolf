package uk.me.webpigeon.wolf.newcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.StartGame;
import uk.me.webpigeon.wolf.newcode.actions.WolfUtils;
import uk.me.webpigeon.wolf.newcode.events.ChatMessage;
import uk.me.webpigeon.wolf.newcode.events.EventI;
import uk.me.webpigeon.wolf.newcode.events.GameStarted;
import uk.me.webpigeon.wolf.newcode.events.PlayerDeath;
import uk.me.webpigeon.wolf.newcode.events.PlayerRole;
import uk.me.webpigeon.wolf.newcode.events.PlayerVote;
import uk.me.webpigeon.wolf.newcode.events.StateChanged;

public class WolfController implements Runnable {
	
	private WolfModel model;
	private GameState state;
	private VoteService<String> service;
	
	private Collection<SessionManager> listeners;
	
	private Collection<Queue<EventI>> events;
	private Map<String, Queue<EventI>> playerEvents;
	private BlockingQueue<ActionWrapper> actions;
	
	private int currentTurn;

	public WolfController(WolfModel model) {
		this.model = model;
		this.state = GameState.INIT;
		
		this.listeners = new ArrayList<SessionManager>();
		this.events = new ArrayList<>();
		this.playerEvents = new TreeMap<>();
		
		this.actions = new LinkedBlockingQueue<ActionWrapper>();
	}
	
	public void setVoteService(VoteService<String> service) {
		this.service = service;
	}
	
	public VoteService<String> getVoteService() {
		return service;
	}
	
	public void addListener(SessionManager listener) {
		assert listener != null : "Listener cannot be null";
		assert state == GameState.INIT;
		listeners.add(listener);
		
		BlockingQueue<EventI> playerEventQueue = new LinkedBlockingQueue<EventI>();
		events.add(playerEventQueue);
		listener.bind(null, null, playerEventQueue);
	}
	
	public void addPlayer(String name, SessionManager listener) {
		assert name != null : "name cannot be null";
		assert listener != null : "listener cannot be null";
		assert !playerEvents.containsKey(name) : "already a player with that name";
		
		model.addPlayer(name);
		listeners.add(listener);
		
		BlockingQueue<EventI> playerEventQueue = new LinkedBlockingQueue<EventI>();
		events.add(playerEventQueue);
		playerEvents.put(name, playerEventQueue);
		
		listener.bind(name, this, playerEventQueue);
	}
	
	public void startGame() {
		assert state == GameState.INIT;
		this.state = GameState.STARTING;
		
		model.reset();
		
		addTask(new StartGame());
	}
	
	public void unicast(String player, EventI event) {
		Queue<EventI> eventQueue = playerEvents.get(player);
		if (eventQueue != null) {
			eventQueue.add(event);
		}
	}
	
	public void multicast(EventI event, Predicate<RoleI> filter) {
		for (String player : model.getPlayers()) {
			RoleI playerRole = model.getRole(player);
			if (filter.test(playerRole)) {
				unicast(player, event);
			}
		}
	}
	
	public void broadcast(EventI event) {
		for (Queue<EventI> queue : events) {
			queue.add(event);
		}
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			System.out.println("[debug] Starting game");
			startGame();
				
			System.out.println("[debug] Game started");
			ActionWrapper wrapper = null;
			do {				
				if (wrapper != null) {
					ActionI action = wrapper.action;
					action.execute(wrapper.player, this, model);
				}
				
				if (state != GameState.GAMEOVER) {
					try {
						wrapper = actions.take();
					} catch (InterruptedException ex) {
						System.err.println("interrupted!");
						wrapper = null;
					}
				}
				
			} while (wrapper != null && state != GameState.GAMEOVER);
			
			System.out.println("[debug] Game had ended");
		}
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState newState) {
		this.state = newState;
		currentTurn++;
		broadcast(new StateChanged(state));
	}
	
	private void addTask(ActionI task) {
		ActionWrapper wrapper = new ActionWrapper();
		wrapper.player = null;
		wrapper.action = task;
		
		actions.add(wrapper);
	}
	
	public void addTask(String player, ActionI task) {
		ActionWrapper wrapper = new ActionWrapper();
		wrapper.player = player;
		wrapper.action = task;
		
		actions.add(wrapper);
	}

	/*public void announceStart() {
		Collection<String> playerList = model.getPlayers();
		broadcast(new GameStarted(playerList));
	}
	
	public void announceState(GameState newState) {
		broadcast(new StateChanged(newState));
	}

	public void announceMessage(String player, String message, String channel) {
		broadcast(new ChatMessage(player, message, channel));
	}

	public void announceDeath(String victim, RoleI victimRole, String cause) {
		broadcast(new PlayerDeath(victim, victimRole.getName(), cause));
	}
	
	public void announceRole(String player, RoleI role) {
		broadcast(new PlayerRole(player, role));
	}

	public void announceVote(String voter, String candidate) {
		broadcast(new PlayerVote(voter, candidate));
	}

	public void sendRole(String seer, String seen, RoleI seenRole) {
		assert seer != null;
		assert seen != null;
		assert model.isAlivePlayer(seer);
		
		unicast(seer, new PlayerRole(seen, seenRole));
	}*/

	private static class ActionWrapper{
		String player;
		ActionI action;
		
		public String toString() {
			return "("+player+","+action+")";
		}
	}
	
	public int getTurnCounter() {
		return currentTurn;
	}

}
