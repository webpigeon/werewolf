package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;

public interface Behavour {

	boolean canActivate(BeliefSystem player);

	ActionI generateAction(BeliefSystem player);

}
