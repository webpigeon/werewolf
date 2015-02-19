package uk.me.webpigeon.wolf.newcode.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MultiMapFacts implements FactBase {
	private static final Boolean DEBUG_MODE = false;
	
	private Map<String,List<String>> facts;
	
	public MultiMapFacts() {
		this.facts = new TreeMap<>();
	}

	@Override
	public void storeFact(String fact, String value) {
		List<String> valueList = facts.get(fact);
		if (valueList == null) {
			valueList = new ArrayList<>();
			facts.put(fact, valueList);
		}
		
		valueList.add(value);
	}

	@Override
	public void removeFact(String fact, String value) {
		List<String> valueList = facts.get(fact);
		if (valueList == null) {
			return;
		}
		
		valueList.remove(value);
	}

	@Override
	public boolean hasFact(String fact, String value) {
		List<String> valueList = facts.get(fact);
		if (valueList == null) {
			if (DEBUG_MODE) {
				System.out.println("warning, fact "+fact+" does not exist");
			}
			
			return false;
		}
		
		return valueList.contains(value);
	}

	@Override
	public List<String> getValues(String fact) {
		List<String> valueList = facts.get(fact);
		if (valueList == null) {
			if (DEBUG_MODE) {
				System.out.println("warning, fact "+fact+" does not exist");
			}
			
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableList(valueList);
	}

	@Override
	public void removeFact(String gameState) {
		facts.remove(gameState);
	}

	@Override
	public void reset() {
		facts.clear();
	}

	@Override
	public String getValue(String fact) {
		List<String> values = facts.get(fact);
		if (values == null || values.isEmpty()) {
			return null;
		}
		
		assert values.size() == 1 : "error - called getValue on multivalue";
		
		return values.get(0);
	}
	
	@Override
	public String toString() {
		return facts.toString();
	}

}
