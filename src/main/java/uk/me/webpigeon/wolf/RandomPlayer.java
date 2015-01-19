package uk.me.webpigeon.wolf;

import java.util.Collections;
import java.util.List;

public class RandomPlayer extends AbstractPlayer {

	public RandomPlayer(String name) {
		super(name);
	}
	
	public void notifyDaytime(PlayerController controller) {	
		List<String> actions = controller.getActions();
		
		Collections.shuffle(actions);
		controller.takeAction(actions.get(0));
	}

	public void notifyNighttime(PlayerController controller) {
		List<String> actions = controller.getActions();
		
		Collections.shuffle(actions);
		controller.takeAction(actions.get(0));
	}

}
