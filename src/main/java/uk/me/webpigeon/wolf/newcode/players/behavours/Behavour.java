package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;

public interface Behavour {

	boolean canActivate(BeliefSystem player, ActionI currentAction);

	ActionI generateAction(BeliefSystem player);

}
