package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class WolfGame implements Runnable {
	private final Integer MIN_PLAYERS = 5;
	
	private Map<GameObserver, GameController> controllers;
	private List<GameObserver> observers;
	private List<RoleI> roles;
	private List<GameObserver> players;
	private Map<GameObserver, RoleI> playerRoles;
	
	private GameState state;
	
	private VoteService<GameObserver> voteService;

	public WolfGame(List<RoleI> roles) {
		this.controllers = new HashMap<GameObserver, GameController>();
		this.observers = new ArrayList<GameObserver>();
		this.roles = roles;
		this.players = new Vector<GameObserver>();
		this.playerRoles = new HashMap<GameObserver, RoleI>();

		this.voteService = new VoteService<GameObserver>(players);
		this.state = GameState.STARTING;
	}
	
	public void add(String name, GameObserver player) {
		if (state != GameState.STARTING) {
			throw new RuntimeException("The game has already started");
		}
		
		GameController controller = new SimpleGameController(name, player, this);
		players.add(player);
		player.bind(controller);
		controllers.put(player, controller);
	}
	
	/**
	 * A spectator with perfect knowledge
	 * 
	 * @param observer the observer callback
	 */
	public void addObserver(GameObserver observer) {
		if (state != GameState.STARTING) {
			throw new RuntimeException("The game has already started");
		}
		
		observers.add(observer);
	}
	
	public void run() {
		
		state = GameState.INIT;
			
		System.out.println("Welcome to AI warewolf");
		System.out.println("Players are: "+players);
		
		assignRoles();
		notifyRoles();
		
		doDaytime();
	}
	
	private void processVotes() {
		String voteType = state==GameState.DAYTIME? "lynch" : "eaten";
		
		if (voteService.isFinished()) {
			GameObserver observer = voteService.getResult();
			
			if (observer != null) {
				players.remove(observer);
				notifyDeath(observer.getName(), voteType);
			} else {
				notifyDeath(null, voteType);
			}
			
			switchState();
		}
		
	}
	
	public GameObserver doDaytime() {
		state = GameState.DAYTIME;
		voteService = new VoteService<GameObserver>(players);
		
		notifyGameStateChange(GameState.DAYTIME);
		System.out.println("[GAME] It is now daytime");
		System.out.println("[GAME] alive players are: "+players);
		
		ReadOnlyController controller = new ReadOnlyController(this);
		for (GameObserver observer : observers) {
			observer.notifyDaytime(controller);
		}
		
		for (GameObserver player : players) {
			player.notifyDaytime(controller);
		}
		
		return null;
	}
	
	public GameObserver doNighttime() {
		state = GameState.NIGHTTIME;
		voteService = new VoteService<GameObserver>(getWolves());
		
		notifyGameStateChange(GameState.NIGHTTIME);
		
		System.out.println("[GAME] It is now night time");
		System.out.println("[GAME] alive players are: "+players);
		
		ReadOnlyController controller = new ReadOnlyController(this);
		for (GameObserver observer : observers) {
			observer.notifyNighttime(controller);
		}
		
		for (GameObserver player : players) {
			player.notifyNighttime(controller);
		}
		
		return null;
	}
	
	private void switchState() {
		if (didVillagersWin() || didWolvesWin()) {
			state = GameState.GAMEOVER;
			return;
		}
		
		if (state == GameState.DAYTIME) {
			doNighttime();
		} else {
			doDaytime();
		}
	}
	
	private void notifyGameStateChange(GameState newState) {
		for (GameController controller : controllers.values()) {
			controller.setState(newState);
		}
	}
	
	private List<GameObserver> getWolves() {
		List<GameObserver> wolves = new ArrayList<GameObserver>();
		for (GameObserver player : players) {
			RoleI role = playerRoles.get(player);
			
			if (role.isOnTeam(Team.WOLVES)) {
				wolves.add(player);
			}
		}
		return wolves;
	}
	
	public void notifyRoles() {
		Iterator<Entry<GameObserver, RoleI>> itr = playerRoles.entrySet().iterator();
		
		while(itr.hasNext()) {
			Entry<GameObserver, RoleI> entry = itr.next();
			GameObserver p = entry.getKey();
			RoleI r = entry.getValue();
			
			p.notifyRole(p.getName(), r);
			GameController c = controllers.get(p);
			c.setRole(r);
			
			for (GameObserver observer : observers) {
				observer.notifyRole(p.getName(), r);
			}
		}
	}
	
	private void notifyDeath(String who, String how) {
		for (GameObserver player : players) {
			player.notifyDeath(who, how);
		}
		
		for (GameObserver player : observers) {
			player.notifyDeath(who, how);
		}
	}
	
	private void notifyPublicVote(GameObserver voter, GameObserver votee) {
		notifyVote(voter, votee, players);
	}
	
	private void notifyVote(GameObserver voter, GameObserver votee, List<GameObserver> voters) {
		for (GameObserver p : voters) {
			if (votee != null && voter != null) {
				p.notifyVote(voter.getName(), votee.getName());
			}
		}
		
		for (GameObserver o : observers) {
			if (votee != null && voter != null) {
				o.notifyVote(voter.getName(), votee.getName());
			}
		}
	}
	
	public Team getWinningTeam() {
		
		if (didVillagersWin()) {
			return Team.VILLAGERS;
		}
		
		if (didWolvesWin()) {
			return Team.WOLVES;
		}
		
		return null;
	}
	
	
	private void assignRoles() {
		if (players.size() < MIN_PLAYERS) {
			throw new RuntimeException("not enouph players");
		}
		
		List<GameObserver> playersLeft = new ArrayList<GameObserver>(players);
		Collections.shuffle(playersLeft);
		
		for(int i=0; i<playersLeft.size(); i++) {
			RoleI r = WolfUtils.getDefaultRole();
			GameObserver p = playersLeft.get(i);
			if (roles.size() > i) {
				r = roles.get(i);
			}
			
			playerRoles.put(p, r);
		}
		
	}
	
	public synchronized void enterVote(String voterName, String candidateName, boolean isPublic) {
		GameObserver voter = getPlayer(voterName);
		GameObserver candidate = getPlayer(candidateName);
		enterVote(voter, candidate, isPublic);
	}
	
	private void enterVote(GameObserver voter, GameObserver candidate, boolean isPublic) {		
		voteService.vote(voter, candidate);
		
		if (isPublic) {
			notifyPublicVote(voter, candidate);
		} else {
			notifyVote(voter, candidate, getWolves());
		}
		
		processVotes();
	}
	
	private boolean didVillagersWin() {
		List<GameObserver> wolves = getWolves();
		return wolves.isEmpty();
	}
	
	private boolean didWolvesWin() {
		List<GameObserver> wolves = getWolves();
		int villagers = players.size() - wolves.size();
		return villagers <= wolves.size();
	}
	
	
	public RoleI getPlayerRole(GameObserver player) {
		return playerRoles.get(player);
	}
	
	public synchronized void timeout() {
		
		System.out.println("admin caused timeout");
		voteService.setSuddenDeath(true);
		processVotes();
	}

	public List<String> getAlivePlayers() {
		List<String> playerNames = new ArrayList<String>();
		for (GameObserver p : players) {
			playerNames.add(p.getName());
		}
		
		return playerNames;
	}

	public GameObserver getPlayerByName(String name) {
		for (GameObserver player : players) {
			if (name.equals(player.getName())) {
				return player;
			}
		}

		return null;
	}

	public synchronized void sendMessage(String name, String message) {
		for (GameObserver player : players) {
			player.notifyMessage(name, message);
		}
		
		for (GameObserver observer : observers) {
			observer.notifyMessage(name, message);
		}
		
	}

	private GameObserver getPlayer(String name) {
		for (GameObserver p : players) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public GameState getState() {
		return state;
	}
	
}
