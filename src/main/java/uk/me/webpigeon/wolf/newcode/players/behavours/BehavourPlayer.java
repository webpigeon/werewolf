package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.ArrayList;
import java.util.List;

import uk.me.webpigeon.wolf.action.ActionI;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;

public class BehavourPlayer extends AbstractPlayer {
	private List<Behavour> behavours;
	
	public BehavourPlayer() {
		this.behavours = new ArrayList<Behavour>();
	}

	public void addBehavour(Behavour b) {
		behavours.add(b);
	}
	
	@Override
	public ActionI selectAction(BeliefSystem system) {
		
		for (Behavour behavour : behavours) {
			if (behavour.canActivate(system)) {
				return behavour.generateAction(system);
			}
		}
		
		return null;
	}
	
}
