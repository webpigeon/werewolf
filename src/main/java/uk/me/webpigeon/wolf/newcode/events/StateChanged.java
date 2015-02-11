package uk.me.webpigeon.wolf.newcode.events;

import uk.me.webpigeon.wolf.GameState;


public class StateChanged implements EventI {
	public final GameState newState;
	
	public StateChanged(GameState newState) {
		this.newState = newState;
	}
	
	public String getType() {
		return "stateChange";
	}
	
	public String toString() {
		return "stateChange("+newState+")";
	}

}
