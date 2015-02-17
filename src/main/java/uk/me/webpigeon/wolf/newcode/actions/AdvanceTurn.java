package uk.me.webpigeon.wolf.newcode.actions;

import java.util.Collection;
import java.util.Collections;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.events.PlayerDeath;

/**
 * Action to advance the current turn to the next game phase.
 */
public class AdvanceTurn extends NewAction {
	private int turnCount;
	
	public AdvanceTurn(int turnCount) {
		super(GameState.DAYTIME, GameState.NIGHTTIME);
		this.turnCount = turnCount;
	}
	
	@Override
	public void execute(String name, WolfController controller, WolfModel model) {
		GameState currentState = controller.getState();
		if (!isPermittedState(currentState)) {
			System.err.println("tried to advance turn when not in main game stage");
			return;
		}
		
		int currentTurn = controller.getTurnCounter();
		if (currentTurn+1 != turnCount) {
			// turn advanced already
			return;
		}
		
		GameState newState = (currentState == GameState.NIGHTTIME ? GameState.DAYTIME : GameState.NIGHTTIME);
		
		// Step 1. find out who is going to die
		
		// tell the voting service that it's phase is over
		VoteService<String> voteService = controller.getVoteService();
		if (!voteService.isFinished()) {
			voteService.setSuddenDeath(true);
		}
		
		// find out who the victim is
		String victim = voteService.getResult();
		if (victim != null) {
			// Step 2. kill them
			assert model.isAlivePlayer(victim);
			RoleI victimRole = model.getRole(victim);
			model.removePlayer(victim);
			
			// Step 3. tell people who died
			String cause = currentState==GameState.DAYTIME?"lynch":"eat";
			controller.broadcast(new PlayerDeath(victim, victimRole.getName(), cause));
			
			if (model.isGameOver()) {
				controller.addTask(null, new EndGame());
				return;
			}
		}
		
		
		// setup the new voting service
		Collection<String> voteTokens = (newState == GameState.DAYTIME ? model.getPlayers() : model.getWolves() );
		//Collection<String> voteTokens = Collections.emptyList();
		voteService = new VoteService<String>(voteTokens);
		controller.setVoteService(voteService);
		
		// tell the controller it's now tomorrow
		controller.setState(newState);
	}

	@Override
	public boolean isEqual(ActionI action) {
		return (action != null) && action instanceof AdvanceTurn;
	}
	
}
