package uk.me.webpigeon.wolf.newcode.players.behavours;

/**
 * Details of facts which are important for wolf.
 * 
 * This exists so we don't have typos or the like in our fact names (and it's easy to change if they're wrong).
 */
public interface Facts {
	
	public static final String ALIVE_PLAYERS = "alivePlayers";
	public static final String DEAD_PLAYERS = "deadPlayers";
	public static final String UNTRUSTWORTHY_PLAYERS = "lierPlayers";
	
	public static final String CURRENT_VOTES = "currentVotes";
	
	public static final String AGENT_NAME = "myAgentName";
	public static final String AGENT_ROLE = "myAgentRole";
	
	public static final String GAME_ID = "GameID";
	public static final String GAME_STATE = "gameState";
	
	public static final String PLAYER_ROLE = "%s-role";
	public static final String PLAYER_MESSAGES = "%s-messages";
	public static final String PLAYER_DEATH_ROLE = "%s-death-role";
}
