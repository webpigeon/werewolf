package uk.me.webpigeon.wolf;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.legacy.players.GameObserver;

public class SimpleGameController implements GameController {
	private String name;
	private GameObserver player;
	private WolfGame game;
	private GameState state;
	private RoleI role;

	public SimpleGameController(String name, GameObserver player, WolfGame game) {
		this.name = name;
		this.player = player;
		this.game = game;
		this.state = GameState.INIT;
	}
	
	@Override
	public void talk(String message) {
	}

	@Override
	public void act(ActionI action) {
		action.execute(game, player.getName());
	}

	@Override
	public Collection<ActionI> getLegalActions() {
		if (role == null) {
			return Collections.emptySet();
		}
		
		return role.getLegalActions(name, game.getState(), game.getAlivePlayers());
	}

	public void setState(GameState state) {
		this.state = state;
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
	public void setRole(RoleI role) {
		this.role = role;
	}

	@Override
	public String getName() {
		return name;
	}

}
