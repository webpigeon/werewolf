package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.FactBase;

public interface ProductionRule {

	public String getID();
	
	boolean canActivate(FactBase facts, String setByBehavour);

	ActionI generateAction(FactBase beliefs);

}
