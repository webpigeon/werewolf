package uk.me.webpigeon.wolf.newcode.legacy.players;

import java.util.Collection;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;

public class NoopPlayer extends AbstractPlayer {
	
	public NoopPlayer() {	
	}

	@Override
	protected void takeAction(Collection<ActionI> legalActions) {
		think("I am not taking any actions");
	}

	@Override
	protected void clearTurnLocks() {
		// TODO Auto-generated method stub
		
	}

}
