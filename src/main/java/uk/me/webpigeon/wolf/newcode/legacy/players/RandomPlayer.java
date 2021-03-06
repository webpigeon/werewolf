package uk.me.webpigeon.wolf.newcode.legacy.players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;

public class RandomPlayer extends AbstractPlayer {
	private boolean madeMove;

	public RandomPlayer() {
	}

	@Override
	protected void takeAction(Collection<ActionI> legalActions) {
		if (madeMove) {
			return;
		}
		
		List<ActionI> actionList = new ArrayList<ActionI>(legalActions);
		
		if (!legalActions.isEmpty()){
			Collections.shuffle(actionList);
			
			ActionI myAction = actionList.get(0);
			controller.talk("I think that we should "+myAction);
			controller.act(myAction);
			madeMove = true;
		}
	}

	@Override
	protected void clearTurnLocks() {
		madeMove = false;
	}

}
