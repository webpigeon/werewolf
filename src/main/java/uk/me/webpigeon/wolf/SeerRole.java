package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.me.webpigeon.wolf.newcode.actions.AbstainAction;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.EatAction;
import uk.me.webpigeon.wolf.newcode.actions.SeeAction;

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
		legalActions.add(new AbstainAction(name));
		permute(legalActions, name, players, SeeAction.class);
		return legalActions;
	}


}
