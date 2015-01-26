package uk.me.webpigeon.wolf;

import java.util.Collections;
import java.util.List;

public class RandomPlayer extends AbstractPlayer {

	public RandomPlayer(String name) {
		super(name);
	}
	
	@Override
	protected void takeAction(PlayerController controller) {
		List<String> actions = controller.getActions();
		
		Collections.shuffle(actions);
		controller.takeAction(actions.get(0));
	}

}
