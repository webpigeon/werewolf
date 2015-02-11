package uk.me.webpigeon.wolf.newcode.actions;

import java.util.Arrays;
import java.util.Collection;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public abstract class NewAction implements ActionI {
	private Collection<GameState> permittedStates;
	
	public NewAction(GameState ... permittedStates) {
		this.permittedStates = Arrays.asList(permittedStates);
	}
	
	@Override
	public void execute(WolfController controller, WolfModel model) {
		if (!isPermittedState(controller.getState())){
			throw new RuntimeException("user attempted illegal move");
		}
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
	public final boolean isTarget(String selected) {
		throw new RuntimeException("Incompatable action: new actions are only for new games");
	}

}
