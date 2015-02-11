package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.me.webpigeon.wolf.newcode.actions.AbstainAction;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.EatAction;

public class WolfRole extends AbstractRole {

	public WolfRole() {
		super("wolf", Team.WOLVES);
	}

	@Override
	public Collection<ActionI> getLegalActions(String name, GameState state, Collection<String> players) {
		
		List<ActionI> actionList = new ArrayList<ActionI>();
		actionList.addAll(super.getLegalActions(name, state, players));
		
		switch (state) {
			case NIGHTTIME:
				permuteEating(name, actionList, players);
				break;
						
			default:
		}
		
		return actionList;
	}

	private Collection<ActionI> permuteEating(String name, List<ActionI> legalActions, Collection<String> players) {	
		legalActions.add(new AbstainAction(name));
		permute(legalActions, name, players, EatAction.class);
		return legalActions;
	}

	

}
