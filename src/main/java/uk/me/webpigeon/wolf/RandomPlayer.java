package uk.me.webpigeon.wolf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.me.webpigeon.wolf.action.ActionI;

public class RandomPlayer extends AbstractPlayer {

	public RandomPlayer(String name) {
		super(name);
	}

	@Override
	protected void takeAction(Collection<ActionI> legalActions) {
		List<ActionI> actionList = new ArrayList<ActionI>(legalActions);
		
		if (!legalActions.isEmpty()){
			Collections.shuffle(actionList);
			
			ActionI myAction = actionList.get(0);
			controller.talk("I think that we should "+myAction);
			controller.act(myAction);
		}
	}

}
