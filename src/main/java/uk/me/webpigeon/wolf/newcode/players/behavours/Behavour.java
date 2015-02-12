package uk.me.webpigeon.wolf.newcode.players.behavours;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;

public interface Behavour {

	boolean canActivate(AbstractPlayer player, BeliefSystem beliefs, ActionI currentAction);

	ActionI generateAction(AbstractPlayer player, BeliefSystem beliefs);

}
