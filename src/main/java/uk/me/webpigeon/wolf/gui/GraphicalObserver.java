package uk.me.webpigeon.wolf.gui;

import uk.me.webpigeon.wolf.GameController;
import uk.me.webpigeon.wolf.GameObserver;
import uk.me.webpigeon.wolf.PlayerController;
import uk.me.webpigeon.wolf.RoleI;

public class GraphicalObserver implements GameObserver {
	private WolfFrame frame;
	private WolfController wolfController;
	
	public GraphicalObserver(WolfFrame frame, WolfController controller) {
		this.frame = frame;
		this.wolfController = controller;
	}

	@Override
	public void notifyRole(String p, RoleI r) {
		frame.apppendText("<i>["+p+" is now a <b>"+r.getName()+"</b>]</i>");
	}

	@Override
	public void notifyDaytime(PlayerController controller) {
		wolfController.setRoundStarted(true);
		frame.apppendText("It is now daytime");
		frame.apppendText("Alive players are: "+controller.getPlayers());
		frame.setPlayers(controller.getPlayers());
	}

	@Override
	public void notifyNighttime(PlayerController controller) {
		wolfController.setRoundStarted(false);
		frame.apppendText("It is now nighttime");
		frame.apppendText("Alive players are: "+controller.getPlayers());
		frame.setPlayers(controller.getPlayers());
	}

	@Override
	public void notifyVote(String voter, String votee) {
		frame.apppendText("[*] <b>"+voter+"</b> voted for <b>"+votee+"</b>");
	}

	@Override
	public synchronized void notifyMessage(String who, String message) {
		frame.apppendText("&lt;<b>"+who+"</b>&gt; "+message);
	}

	@Override
	public void notifyDeath(String name, String cause) {
		if (name == null) {
			return;
		}
		
		switch(cause) {
			case "lynch":
				frame.apppendText("[*] the villagers lynch "+name);
				break;
				
			case "timeout":
				frame.apppendText("[*] <b>"+name+"</b> suddenly falls down dead");
				break;
				
			case "eaten":
				frame.apppendText("[*] <b>"+name+"</b> was eaten by wolves");
				break;
		}
	}

	@Override
	public void bind(GameController c) {
		//We won't be able to do anything
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "gui";
	}

}
