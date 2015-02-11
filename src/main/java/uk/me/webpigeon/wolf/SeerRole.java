package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.me.webpigeon.wolf.action.AbstainAction;
import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.action.EatAction;
import uk.me.webpigeon.wolf.action.SeeAction;

public class SeerRole extends AbstractRole {

	public SeerRole() {
		super("seer", Team.VILLAGERS);
	}

	@Override
	public Collection<ActionI> getLegalActions(String name, GameState state, Collection<String> players) {
		
		List<ActionI> actionList = new ArrayList<ActionI>();
		actionList.addAll(super.getLegalActions(name, state, players));
		
		switch (state) {
			case NIGHTTIME:
				permuteSeeing(name, actionList, players);
				break;
						
			default:
		}
		
		return actionList;
	}

	private Collection<ActionI> permuteSeeing(String name, List<ActionI> legalActions, Collection<String> players) {	
		legalActions.add(new AbstainAction());
		permute(legalActions, name, players, SeeAction.class);
		return legalActions;
	}


}
