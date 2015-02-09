package uk.me.webpigeon.wolf.newcode.actions;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

public class StartGame extends NewAction {
	
	public StartGame() {
		super(GameState.STARTING);
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		GameState currentState = controller.getState();
		if (!isPermittedState(currentState)) {
			System.err.println("tried to start game when not ready");
			return;
		}
		
		controller.announceStart();
		
		VoteService<String> voteService = new VoteService<String>(model.getPlayers());
		controller.setVoteService(voteService);
		
		controller.setState(GameState.DAYTIME);
		controller.announceState(GameState.DAYTIME);
	}


}
