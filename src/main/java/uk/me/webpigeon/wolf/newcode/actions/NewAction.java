package uk.me.webpigeon.wolf.newcode.actions;

import java.util.Arrays;
import java.util.Collection;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.WolfGame;
import uk.me.webpigeon.wolf.action.ActionI;

public abstract class NewAction implements ActionI {
	private Collection<GameState> permittedStates;
	
	public NewAction(GameState ... permittedStates) {
		this.permittedStates = Arrays.asList(permittedStates);
	}
	
	/**
	 * Guard to prevent actions being executed at the wrong time.
	 * 
	 * @param current the state to check
	 * @return true if the action should run, false otherwise
	 */
	protected boolean isPermittedState(GameState current) {
		return permittedStates.contains(current);
	}
	
	@Override
	public final void execute(WolfGame game, String player) {
		throw new RuntimeException("Incompatable action: new actions are only for new games");
	}

	@Override
	public final boolean isTarget(String selected) {
		throw new RuntimeException("Incompatable action: new actions are only for new games");
	}

}
