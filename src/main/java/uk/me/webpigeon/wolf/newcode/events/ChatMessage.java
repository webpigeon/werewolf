package uk.me.webpigeon.wolf.newcode.events;


public class ChatMessage implements EventI {
	public final String player;
	public final String message;
	public final String channel;
	
	public ChatMessage(String player, String message) {
		this.player = player;
		this.message = message;
		this.channel = "public";
	}
	
	public ChatMessage(String player, String message, String channel) {
		this.player = player;
		this.message = message;
		this.channel = channel;
	}
	
	public String getType() {
		return "chat";
	}

}
