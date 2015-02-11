package uk.me.webpigeon.wolf.newcode.legacy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.me.webpigeon.wolf.GameController;
import uk.me.webpigeon.wolf.GameState;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.WolfModel;
import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.actions.TalkAction;

public class LegacyController implements GameController {
	private String playerName;
	private WolfController controller;
	private WolfModel model;
	
	public LegacyController(WolfController controller, WolfModel model) {
		this.controller = controller;
		this.model = model;
	}

	public void setName(String name) {
		this.playerName = name;
	}
	
	@Override
	public void talk(String message) {
		controller.addTask(playerName, new TalkAction(playerName, message));
	}

	@Override
	public GameState getStage() {
		return controller.getState();
	}

	@Override
	public void act(ActionI action) {
		controller.addTask(playerName, action);
	}

	@Override
	public Collection<ActionI> getLegalActions() {
		if (!model.isAlivePlayer(playerName)) {
			System.err.println("warning: dead player ("+playerName+") reqested moves");
			return Collections.emptyList();
		}
		
		RoleI playerRole = model.getRole(playerName);
		assert playerRole != null;
		return playerRole.getLegalActions(playerName, controller.getState(), model.getPlayers());
	}

	@Override
	public List<String> getAlivePlayers() {
		return new ArrayList<String>(model.getPlayers());
	}

	@Override
	public void setState(GameState newState) {
		throw new IllegalArgumentException("Why are you trying to set the game state?");
	}

	@Override
	public void setRole(RoleI role) {
		throw new IllegalArgumentException("Why are you trying to set your role?");
	}

	@Override
	public String getName() {
		return playerName;
	}

}
