package uk.me.webpigeon.wolf.newcode.players;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;

public interface SelectionStrategy {
	
	ActionI selectAction(FactBase beliefs);
	void announceNewTurn();

}
