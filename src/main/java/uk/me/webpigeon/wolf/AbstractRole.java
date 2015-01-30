package uk.me.webpigeon.wolf;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.me.webpigeon.wolf.action.AbstainAction;
import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.action.LynchAction;

public class AbstractRole implements RoleI {
	private String name;
	private Team team;
	
	public AbstractRole(String name, Team team) {
		this.name = name;
		this.team = team;
	}

	@Override
	public Collection<ActionI> getLegalActions(GameState state, Collection<String> players) {
		
		switch (state) {
			case DAYTIME:
				return permuteLynching(players);
		
			default:
				Collections.emptyList();
		}
		
		//should not be possible
		return Collections.emptyList();
	}
	
	protected Collection<ActionI> permuteLynching(Collection<String> players) {
		List<ActionI> lynchList = new ArrayList<ActionI>(players.size());
		lynchList.add(new AbstainAction());
		permute(lynchList, players, LynchAction.class);
		return lynchList;
	}

	/**
	 * Insanely bad code to permute player actions.
	 * 
	 * This is really bad, shouldn't be done, probably a design flaw somewhere, etc...
	 * 
	 * @param players
	 * @param actionClass
	 * @return
	 */
	protected Collection<ActionI> permute(Collection<ActionI> actionList, Collection<String> players, Class<? extends ActionI> actionClass) {
		try {
			Constructor<? extends ActionI> con = actionClass.getConstructor(String.class);
			
			for (String player : players) {
				actionList.add(con.newInstance(player));
			}
			
			return actionList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return actionList;
		}
	}

	@Override
	public boolean isOnTeam(Team team) {
		return this.team == team;
	}

	@Override
	public Object getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
