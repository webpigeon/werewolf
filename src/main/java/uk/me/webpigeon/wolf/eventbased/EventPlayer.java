package uk.me.webpigeon.wolf.eventbased;

public interface EventPlayer {
	
	public void notifyEvent(Event event);

	public String getName();

}
