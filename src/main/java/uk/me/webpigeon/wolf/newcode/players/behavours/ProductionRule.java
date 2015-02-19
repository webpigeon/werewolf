package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;

public interface ProductionRule {

	public String getID();
	
	boolean canActivate(BeliefSystem beliefs, String setByBehavour);

	ActionI generateAction(BeliefSystem beliefs);

}
