package uk.me.webpigeon.wolf;

import java.util.Collection;

import uk.me.webpigeon.wolf.action.ActionI;

public class NoopPlayer extends AbstractPlayer {
	
	public NoopPlayer(String name) {
		super(name);
		
	}

	@Override
	protected void takeAction(Collection<ActionI> legalActions) {
		think("I am not taking any actions");
	}

}
