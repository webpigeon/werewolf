package uk.me.webpigeon.wolf.newcode.players;

import java.util.List;

public interface FactBase {
	
	public void storeFact(String fact, String value);
	
	public void removeFact(String gameState);
	public void removeFact(String fact, String value);
	
	public boolean hasFact(String fact, String value);
	
	public String getValue(String fact);
	public List<String> getValues(String fact);
	
	
	public void reset();
}
