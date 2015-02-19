package uk.me.webpigeon.wolf.newcode.players.behavours;

import java.util.ArrayList;
import java.util.List;

import uk.me.webpigeon.wolf.newcode.actions.ActionI;
import uk.me.webpigeon.wolf.newcode.players.AbstractPlayer;
import uk.me.webpigeon.wolf.newcode.players.BeliefSystem;
import uk.me.webpigeon.wolf.newcode.players.FactBase;
import uk.me.webpigeon.wolf.newcode.players.SelectionStrategy;

public class RuleBasedStrategy implements SelectionStrategy {
	private List<ProductionRule> behavours;
	private String setByBehavour;
	
	public RuleBasedStrategy() {
		this.behavours = new ArrayList<ProductionRule>();
	}

	public void addRule(ProductionRule b) {
		behavours.add(b);
	}
	
	@Override
	public ActionI selectAction(FactBase facts) {
		
		for (ProductionRule behavour : behavours) {
		
			if (behavour.canActivate(facts, setByBehavour)) {
				System.out.println("executing: "+behavour.getID());
				setByBehavour = behavour.getID();
				return behavour.generateAction(facts);
			}
		}
		
		System.out.println("I didn't do anything... "+facts);
		return null;
	}

	@Override
	public void announceNewTurn() {
		setByBehavour = null;
	}
	
}
