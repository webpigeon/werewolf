package uk.me.webpigeon.wolf;

public class WolfUtils {

	public static Player buildSmartPlayer(String name) {
		return buildRunnablePlayer(new BasicIntelligencePlayer(name));
	}
	
	public static Player buildRandomPlayer(String name) {
		return buildRunnablePlayer(new RandomPlayer(name));
	}
	
	public static Player buildNoopPlayer(String name) {
		return buildRunnablePlayer(new NoopPlayer(name));
	}
	
	public static Player buildRunnablePlayer(AbstractPlayer p) {
		Thread pThread = new Thread(p);
		pThread.start();
		return p;
	}
	
	
	
}
