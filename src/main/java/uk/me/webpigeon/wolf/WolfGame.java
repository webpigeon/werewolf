package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WolfGame implements Runnable {
	private final Integer MIN_PLAYERS = 5;
	
	private List<GameObserver> observers;
	private List<Role> roles;
	private List<Player> players;
	private Map<Player, Role> playerRoles;
	
	private GameState state;
	
	private VoteService<Player> voteService;

	public WolfGame(List<Role> roles) {
		this.observers = new ArrayList<GameObserver>();
		this.roles = roles;
		this.players = new ArrayList<Player>();
		this.playerRoles = new HashMap<Player, Role>();

		this.voteService = new VoteService<Player>(players);
		this.state = GameState.STARTING;
	}
	
	public void add(Player player) {
		if (state != GameState.STARTING) {
			throw new RuntimeException("The game has already started");
		}
			
		players.add(player);
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
		state = GameState.DAYTIME;
		
		while(state != GameState.GAMEOVER) {
			
			if (didVillagersWin() || didWolvesWin()) {
				state = GameState.GAMEOVER;
				break;
			}
			
			Player lynch = doDaytime();
			if (lynch == null) {
				System.out.println("The villagers decide not to lynch anyone today");
			} else {
				System.out.println("The villages decide to lynch "+lynch+" who turns out to be a "+playerRoles.get(lynch));
				players.remove(lynch);
			}

			if (didVillagersWin() || didWolvesWin()) {
				state = GameState.GAMEOVER;
				break;
			}

			Player eaten = doNighttime();
			if (eaten == null) {
				System.out.println("The villagers wake and find no one was eaten");
			} else {
				System.out.println("The villages wake to find "+eaten+" dead, who turns out to be a "+playerRoles.get(eaten));
				players.remove(eaten);
			}
			
		}
		
		System.out.println("Game over, winners are: "+getWinningTeam());
	}
	
	public synchronized Player doDaytime() {
		state = GameState.DAYTIME;
		voteService = new VoteService<Player>(players);
		
		System.out.println("[GAME] It is now daytime");
		System.out.println("[GAME] alive players are: "+players);
		
		ReadOnlyController controller = new ReadOnlyController(this);
		for (GameObserver observer : observers) {
			observer.notifyDaytime(controller);
		}
		
		for (Player player : players) {
			player.notifyDaytime(new DaytimePlayerController(player, this));
		}
		
		while (!voteService.isFinished()) {
			System.out.println("vote not finished");
			try {
				wait();
			} catch (InterruptedException ex) {}
		}
		
		return voteService.getResult();
	}
	
	public synchronized Player doNighttime() {
		state = GameState.NIGHTTIME;
		voteService = new VoteService<Player>(getWolves());
		
		System.out.println("[GAME] It is now night time");
		System.out.println("[GAME] alive players are: "+players);
		
		ReadOnlyController controller = new ReadOnlyController(this);
		for (GameObserver observer : observers) {
			observer.notifyNighttime(controller);
		}
		
		for (Player player : players) {
			player.notifyNighttime(new NighttimePlayerController(player, this));
		}
		
		//TODO block until vote
		while (!voteService.isFinished()) {
			try {
				wait();
			} catch (InterruptedException ex) {}
		}
		
		return voteService.getResult();
	}
	
	private List<Player> getWolves() {
		List<Player> wolves = new ArrayList<Player>();
		for (Player player : players) {
			Role role = playerRoles.get(player);
			
			if (role == Role.WOLF) {
				wolves.add(player);
			}
		}
		return wolves;
	}
	
	public void notifyRoles() {
		Iterator<Entry<Player, Role>> itr = playerRoles.entrySet().iterator();
		
		while(itr.hasNext()) {
			Entry<Player, Role> entry = itr.next();
			Player p = entry.getKey();
			Role r = entry.getValue();
			
			p.notifyRole(p, r);
			
			for (GameObserver observer : observers) {
				observer.notifyRole(p, r);
			}
		}
	}
	
	private void notifyPublicVote(Player voter, Player votee) {
		notifyVote(voter, votee, players);
	}
	
	private void notifyVote(Player voter, Player votee, List<Player> voters) {
		for (Player p : voters) {
			p.notifyVote(voter.getName(), votee.getName());
		}
		
		for (GameObserver o : observers) {
			o.notifyVote(voter.getName(), votee.getName());
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
		
		List<Player> playersLeft = new ArrayList<Player>(players);
		Collections.shuffle(playersLeft);
		
		for(int i=0; i<playersLeft.size(); i++) {
			Player p = playersLeft.get(i);
			Role r = roles.get(i);
			
			playerRoles.put(p, r);
		}
		
	}
	
	public synchronized void enterVote(Player voter, Player candidate, boolean isPublic) {
		voteService.vote(voter, candidate);
		
		if (isPublic) {
			notifyPublicVote(voter, candidate);
		} else {
			notifyVote(voter, candidate, getWolves());
		}
		
		System.out.println("[GAME] "+voter+" voted for "+candidate);
		notifyAll();
	}
	
	private boolean didVillagersWin() {
		List<Player> wolves = getWolves();
		return wolves.isEmpty();
	}
	
	private boolean didWolvesWin() {
		List<Player> wolves = getWolves();
		int villagers = players.size() - wolves.size();
		return villagers <= wolves.size();
	}
	
	
	public Role getPlayerRole(Player player) {
		return playerRoles.get(player);
	}
	
	public synchronized void timeout() {
		
		System.out.println("admin caused timeout");
		voteService.setSuddenDeath(true);
		
		notifyAll();
	}

	public List<Player> getAlivePlayers() {
		return Collections.unmodifiableList(players);
	}

	public Player getPlayerByName(String name) {
		for (Player player : players) {
			if (name.equals(player.getName())) {
				return player;
			}
		}

		return null;
	}

}
