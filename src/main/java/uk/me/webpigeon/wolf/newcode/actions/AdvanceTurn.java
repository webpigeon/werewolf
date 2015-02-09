package uk.me.webpigeon.wolf.newcode.actions;

import java.util.Collection;
import java.util.Collections;

import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.VoteService;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;

/**
 * Action to advance the current turn to the next game phase.
 */
public class AdvanceTurn extends NewAction {
	
	public AdvanceTurn() {
		super(GameState.DAYTIME, GameState.NIGHTTIME);
	}

	@Override
	public void execute(WolfController controller, WolfModel model) {
		GameState currentState = controller.getState();
		if (!isPermittedState(currentState)) {
			System.err.println("tried to advance turn when not in main game stage");
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
			
			System.out.println(victim+" ("+victimRole+") is now dead");
			
			// Step 3. tell people who died
			controller.announceRole(victim, victimRole);
			controller.announceDeath(victim, victimRole, currentState==GameState.DAYTIME?"lynch":"eat");
			
			if (model.isGameOver()) {
				controller.addTask(new EndGame());
			}
		}
		
		
		// setup the new voting service
		//Collection<String> voteTokens = (newState == GameState.DAYTIME ? model.getPlayers() : model.getWolves() );
		Collection<String> voteTokens = Collections.emptyList();
		voteService = new VoteService<String>(voteTokens);
		controller.setVoteService(voteService);
		
		// tell the controller it's now tomorrow
		controller.setState(newState);
		controller.announceState(newState);
		
	}


}
