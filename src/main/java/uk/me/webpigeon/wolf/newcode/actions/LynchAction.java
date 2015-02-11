package uk.me.webpigeon.wolf.newcode.actions;


public class LynchAction extends VoteAction {

	public LynchAction(String name, String candidate) {
		super("lynch", name, candidate, true);
	}


}
