package uk.me.webpigeon.wolf;

import java.util.Collections;
import java.util.List;

import uk.me.webpigeon.wolf.action.ActionI;

public class ObserverController implements GameController {
	private WolfGame game;
	private GameState state;
	
	public ObserverController(WolfGame game) {
		this.game = game;
	}

	@Override
	public void talk(String message) {
		//observers are not allowed to talk
	}

	@Override
	public void act(ActionI action) {
		//observers are not allowed to act
	}

	@Override
	public List<ActionI> getLegalActions() {
		return Collections.emptyList();
	}

	@Override
	public GameState getStage() {
		return state;
	}

	@Override
	public List<String> getAlivePlayers() {
		return game.getAlivePlayers();
	}

	@Override
	public void setState(GameState newState) {
		this.state = newState;
	}

	@Override
	public void setRole(RoleI role) {
		//observers have no role
	}

	@Override
	public String getName() {
		return null;
	}

}
