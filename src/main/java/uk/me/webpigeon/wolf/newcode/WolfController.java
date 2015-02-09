package uk.me.webpigeon.wolf.newcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.WolfUtils;
import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.AdvanceTurn;
import uk.me.webpigeon.wolf.newcode.actions.StartGame;

public class WolfController implements Runnable {
	
	private WolfModel model;
	private GameState state;
	private VoteService<String> service;
	
	private Collection<GameListener> listeners;
	private Map<String, GameListener> playerListeners;
	private BlockingQueue<ActionI> actions;

	public WolfController(WolfModel model) {
		this.model = model;
		this.state = GameState.INIT;
		
		this.listeners = new ArrayList<GameListener>();
		this.playerListeners = new TreeMap<String, GameListener>();
		this.actions = new LinkedBlockingQueue<ActionI>();
	}
	
	public void setVoteService(VoteService<String> service) {
		this.service = service;
	}
	
	public VoteService<String> getVoteService() {
		return service;
	}
	
	public void addListener(GameListener listener) {
		assert listener != null : "Listener cannot be null";
		assert state == GameState.INIT;
		listeners.add(listener);
	}
	
	public void addPlayer(String name, GameListener listener) {
		assert name != null : "name cannot be null";
		assert listener != null : "listener cannot be null";
		assert !playerListeners.containsKey(name) : "already a player with that name";
		
		model.addPlayer(name);
		listeners.add(listener);
		playerListeners.put(name, listener);
		
		listener.onJoin(name, actions);
	}
	
	public void startGame() {
		assert state == GameState.INIT;
		this.state = GameState.STARTING;
		
		Map<String, RoleI> roles = model.assignRoles(WolfUtils.buildRoleList(), WolfUtils.getDefaultRole());
		
		//notify players of their roles
		for (Map.Entry<String, RoleI> entry : roles.entrySet()) {
			String player = entry.getKey();
			RoleI role = entry.getValue();
			
			GameListener listener = playerListeners.get(player);
			listener.onDiscoverRole(player, role);
		}
		
		actions.add(new StartGame());
	}
	
	@Override
	public void run() {
		System.out.println("[debug] Starting game");
		startGame();
		
		
		System.out.println("[debug] Game started");
		ActionI action = null;
		do {
			if (action != null) {
				action.execute(this, model);
			}
			
			try {
				action = actions.take();
			} catch (InterruptedException ex) {
				System.err.println("interrupted!");
				action = null;
			}
			
		} while (action != null && !Thread.interrupted());
		
		System.out.println("[debug] Game had ended");
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState newState) {
		this.state = newState;
	}

	public void announceStart() {
		for (GameListener listener : listeners) {
			listener.onGameStart(model.getPlayers());
		}
	}
	
	public void announceState(GameState newState) {
		for (GameListener listener : listeners) {
			listener.onStateChange(newState);
		}
	}

	public void addTask(ActionI task) {
		actions.add(task);
	}

	public void announceMessage(String player, String message, String channel) {
		for (GameListener listener : listeners) {
			listener.onMessage(player, message, channel);
		}
	}

	public void announceDeath(String victim, RoleI victimRole, String cause) {
		for (GameListener listener : listeners) {
			listener.onDeath(victim, cause);
		}
	}
	
	public void announceRole(String player, RoleI role) {
		for (GameListener listener : listeners) {
			listener.onDiscoverRole(player, role);
		}
	}

}
