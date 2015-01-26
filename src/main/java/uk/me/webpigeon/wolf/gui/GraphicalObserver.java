package uk.me.webpigeon.wolf.gui;

import uk.me.webpigeon.wolf.GameObserver;
import uk.me.webpigeon.wolf.Player;
import uk.me.webpigeon.wolf.PlayerController;
import uk.me.webpigeon.wolf.Role;
import uk.me.webpigeon.wolf.WolfGame;

public class GraphicalObserver implements GameObserver {
	private WolfFrame frame;
	private WolfGame game;
	
	public GraphicalObserver(WolfFrame frame, WolfGame game) {
		this.frame = frame;
		this.game = game;
	}

	@Override
	public void notifyRole(Player p, Role r) {
		frame.apppendText("<i>["+p+" is now a <b>"+r+"</b>]</i>");
	}

	@Override
	public void notifyDaytime(PlayerController controller) {
		frame.setDaytime(true);
		frame.setPlayers(controller.getPlayers());
		
		frame.apppendText("It is now daytime");
	}

	@Override
	public void notifyNighttime(PlayerController controller) {
		frame.setDaytime(false);
		frame.setPlayers(controller.getPlayers());
	}

}
