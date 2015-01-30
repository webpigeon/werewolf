package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.me.webpigeon.wolf.action.AbstainAction;
import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.action.EatAction;

public class WolfRole extends AbstractRole {

	public WolfRole() {
		super("wolf", Team.WOLVES);
	}

	@Override
	public Collection<ActionI> getLegalActions(GameState state, Collection<String> players) {
		
		List<ActionI> actionList = new ArrayList<ActionI>();
		actionList.addAll(super.getLegalActions(state, players));
		
		switch (state) {
			case NIGHTTIME:
				permuteEating(actionList, players);
				break;
						
			default:
		}
		
		return actionList;
	}

	private Collection<ActionI> permuteEating(List<ActionI> legalActions, Collection<String> players) {	
		legalActions.add(new AbstainAction());
		permute(legalActions, players, EatAction.class);
		return legalActions;
	}

	

}
