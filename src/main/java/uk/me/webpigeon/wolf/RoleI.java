package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.List;

import uk.me.webpigeon.wolf.action.ActionI;

public interface RoleI {
	
	public boolean isOnTeam(Team wolves);
	Collection<ActionI> getLegalActions(String name, GameState state, Collection<String> players);
	public String getName();
	public Team[] getTeams();

}
