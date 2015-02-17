package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;

public interface Behavour {

	public String getID();
	
	boolean canActivate(AbstractPlayer player, BeliefSystem beliefs, String setByBehavour);

	ActionI generateAction(AbstractPlayer player, BeliefSystem beliefs);

}
