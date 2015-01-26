package uk.me.webpigeon.wolf.gui;

import uk.me.webpigeon.wolf.GameObserver;
import uk.me.webpigeon.wolf.Player;
import uk.me.webpigeon.wolf.PlayerController;
import uk.me.webpigeon.wolf.Role;

public class GraphicalObserver implements GameObserver {
	private WolfFrame frame;
	private WolfController wolfController;
	
	public GraphicalObserver(WolfFrame frame, WolfController controller) {
		this.frame = frame;
		this.wolfController = controller;
	}

	@Override
	public void notifyRole(Player p, Role r) {
		frame.apppendText("<i>["+p+" is now a <b>"+r+"</b>]</i>");
	}

	@Override
	public void notifyDaytime(PlayerController controller) {
		wolfController.setRoundStarted(true);
		frame.apppendText("It is now daytime");
		frame.setPlayers(controller.getPlayers());
	}

	@Override
	public void notifyNighttime(PlayerController controller) {
		wolfController.setRoundStarted(false);
		frame.apppendText("It is now nighttime");
		frame.setPlayers(controller.getPlayers());
	}

	@Override
	public void notifyVote(String voter, String votee) {
		frame.apppendText("[*] <b>"+voter+"</b> voted for <b>"+votee+"</b>");
	}

}
