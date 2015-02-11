package uk.me.webpigeon.wolf.action;


public class LynchAction extends VoteAction {

	public LynchAction(String name, String candidate) {
		super("lynch", name, candidate, true);
	}


}
