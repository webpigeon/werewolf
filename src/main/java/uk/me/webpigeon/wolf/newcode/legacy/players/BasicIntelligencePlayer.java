package uk.me.webpigeon.wolf.newcode.legacy.players;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.actions.AbstainAction;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.EatAction;
import uk.me.webpigeon.wolf.newcode.actions.LynchAction;
import uk.me.webpigeon.wolf.newcode.actions.SeeAction;

public class BasicIntelligencePlayer extends AbstractPlayer {
	private Pattern messageRegex;
	private Map<String, Integer> baises;
	private ActionI currentAction;
	private Set<String> sharedInfomation;
	
	private Random r;
	
	public BasicIntelligencePlayer() {
		baises = new TreeMap<String, Integer>();
		messageRegex = Pattern.compile("\\((\\w+),(\\w+),(\\w+)\\)");
		sharedInfomation = new HashSet<String>();
		r = new Random();
	}
	
	public void recordBias(String player, int biasDelta) {
		Integer voterBias = baises.get(player);
		if (voterBias == null) {
			voterBias = 0;
		}
		baises.put(player, voterBias + biasDelta);
	}
	
	public int getBias(String player) {
		Integer voterBias = baises.get(player);
		if (voterBias == null) {
			voterBias = 0;
		}
		return voterBias;
	}
	
	public void notifyVote(String voter, String votee) {
		Integer voterBias = baises.get(voter);
		if (voterBias == null) {
			voterBias = 0;
		}
		
		//if they vote for me I don't like them
		if (votee.equals(getName())) {
			baises.put(voter, voterBias+1);
		}
		
		// If I'm the seer and I know that the person is not a wolf, I tell people
		if ("seer".equals(getRole())) {
			String voteeRole = roles.get(votee);
			
			if (voteeRole != null) {
				String infomation = "("+votee+","+"role,"+voteeRole+")";
				if (!sharedInfomation.contains(infomation)) {
					controller.talk(infomation);
					sharedInfomation.add(infomation);
				}
			}
		}
		
		String voteeRole = roles.get(votee);
		if (voteeRole != null) {
			if ("wolf".equals(voteeRole)) {
				// the person voted for the wolf
				baises.put(voter, voterBias-1);
			} else if ("seer".equals(voteeRole)) {
				// if they vote for the seer, They're probably a moron or the wolf
				baises.put(voter, voterBias+10);
			}
		}
	}
	
	public void notifyMessage(String who, String message){
		
		Matcher m = messageRegex.matcher(message);
		if (m.matches()) {
			
			String g1 = m.group(1);
			String g2 = m.group(2);
			String g3 = m.group(3);
			
			if ("role".equals(g2)) {
				roles.put(g1, g3);
			}
			
		}
		
	}
	
	@Override
	protected void takeAction(Collection<ActionI> legalActions) {	
		if (legalActions.isEmpty()) {
			return;
		}
		
		String myRole = getRole();
		GameState state = controller.getStage();
		
		// if it's night time and we're a villager, the only thing we can do is sleep
		if (state == GameState.NIGHTTIME && "villager".equals(myRole)) {
			takeAction(new AbstainAction(getName()));
			return;
		}
		
		// if it's night time and we're the wolf, we eat someone
		if (state == GameState.NIGHTTIME && "wolf".equals(myRole)) {
			List<String> players = controller.getAlivePlayers();
			String selected = selectPlayer(getScores(players, myRole, state), players, state);
			
			takeAction(new EatAction(getName(), selected));
			return;
		}
		
		// if it's night time and we're the seer, we see someone
		if (state == GameState.NIGHTTIME && "seer".equals(myRole)) {
			if (currentAction != null) {
				return;
			}
			
			List<String> players = controller.getAlivePlayers();
			String selected = selectPlayer(getScores(players, myRole, state), players, state);
			
			takeAction(new SeeAction(getName(), selected));
			return;
		}
		
		// we need to pick someone to lynch
		if (state == GameState.DAYTIME) {
			List<String> players = controller.getAlivePlayers();
			String selected = selectPlayer(getScores(players, myRole, state), players, state);
			
			takeAction(new LynchAction(getName(), selected));
			return;
		}	

	}
	
	protected void takeAction(ActionI action) {
		if (action.equals(currentAction)) {
			return;
		} else {
			if (currentAction != null) {
				think("I changed my mind from "+currentAction+" to "+action+" "+controller.getStage());
			}
			controller.act(action);
			currentAction = action;
		}
	}
	
	public void shareInfomation(String node, String prop, String subject) {
		String infomation = "("+node+","+prop+","+subject+")";
		if (!sharedInfomation.contains(infomation)) {
			controller.talk(infomation);
			sharedInfomation.add(infomation);
		}
	}
	
	protected String selectPlayer(Map<String, Integer> scores, List<String> players, GameState state) {
		
		String maxPlayer = null;
		int maxScore = Integer.MIN_VALUE;
		
		for (String player : players) {
			Integer score = scores.get(player);
			if (score == null) {
				score = 1; //we don't know about this player
			}
			
			if (maxScore < score) {
				maxPlayer = player;
				maxScore = score;
			}
		}
		
		return maxPlayer;
	}
	
	protected Map<String, Integer> getScores(List<String> players, String ourRole, GameState currentState) {
		
		Map<String, Integer> scores = new TreeMap<String, Integer>();
		
		for (String player : players) {
			String beliefRole = roles.get(player);
			Integer bias = baises.get(player);
			if (bias == null) {
				bias = 0;
			}
			
			//we don't want to select ourselves
			if (player.equals(getName())) {
				scores.put(player, Integer.MIN_VALUE);
				continue;
			}
		
			if (beliefRole != null) {
				if ("seer".equals(ourRole) && currentState == GameState.NIGHTTIME) {
					scores.put(player, -1000 + bias);
					continue;
				}
				
				if ("seer".equals(beliefRole)) {
					if ("wolf".equals(ourRole)) {
						if (currentState == GameState.DAYTIME) {
							scores.put(player, -10 + bias);
						} else {
							System.out.println(getName()+" targeting seer "+player);
							scores.put(player, 1000 + bias);
						}
					} else {
						scores.put(player, -100 + bias);
					}
				}
				
				if ("villager".equals(beliefRole)) {
					if ("wolf".equals(ourRole)) {
						if (currentState == GameState.DAYTIME) {
							scores.put(player, 5 + bias);
						} else {
							scores.put(player, 100 + bias);
						}
					} else {
						scores.put(player, -10 + bias);
					}
				}
				
				if ("wolf".equals(beliefRole)) {
					if ("wolf".equals(ourRole)) {
						if (currentState == GameState.DAYTIME) {
							scores.put(player, -10 + bias);
						} else {
							scores.put(player, -1000 + bias);
						}
					} else {
						scores.put(player, 1000);
					}
				}
			} else {
				if ("seer".equals(ourRole) && currentState == GameState.NIGHTTIME) {
					scores.put(player, 1000 + bias);
					continue;
				} else {
					scores.put(player, 0 + bias);
				}
			}
			
		}
		
		return scores;
	}

	@Override
	protected void clearTurnLocks() {
		System.out.println(getName()+" cleaned current action "+controller.getStage());
		currentAction = null;
	}
	
}
