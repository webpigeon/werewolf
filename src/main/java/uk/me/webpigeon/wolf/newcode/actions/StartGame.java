package uk.me.webpigeon.wolf.newcode.actions;

import java.util.Collection;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.GameStarted;

public class StartGame extends NewAction {
	
	public StartGame() {
		super(GameState.STARTING);
	}

	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		GameState currentState = controller.getState();
		if (!isPermittedState(currentState)) {
			System.err.println("tried to start game when not ready");
			return;
		}
		
		Collection<String> players = model.getPlayers();
		controller.broadcast(new GameStarted(players));
		
		VoteService<String> voteService = new VoteService<String>(players);
		controller.setVoteService(voteService);
		
		controller.setState(GameState.DAYTIME);
	}

	@Override
	public boolean isEqual(ActionI action) {
		return false;
	}


}
