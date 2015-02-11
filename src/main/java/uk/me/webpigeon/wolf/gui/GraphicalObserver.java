package uk.me.webpigeon.wolf.gui;

import uk.me.webpigeon.wolf.GameController;
import uk.me.webpigeon.wolf.PlayerController;
import uk.me.webpigeon.wolf.RoleI;
import uk.me.webpigeon.wolf.newcode.legacy.players.GameObserver;

public class GraphicalObserver implements GameObserver {
	private WolfFrame frame;
	private WolfController wolfController;
	
	public GraphicalObserver(WolfFrame frame, WolfController controller) {
		this.frame = frame;
		this.wolfController = controller;
	}

	@Override
	public void notifyRole(String p, RoleI r) {
		frame.appendText("<i>["+p+" is now a <b>"+r.getName()+"</b>]</i>");
	}

	@Override
	public void notifyDaytime(PlayerController controller) {
		wolfController.setRoundStarted(true);
		frame.appendText("It is now daytime");
		frame.appendText("Alive players are: "+controller.getPlayers());
		frame.setPlayers(controller.getPlayers());
	}

	@Override
	public void notifyNighttime(PlayerController controller) {
		wolfController.setRoundStarted(false);
		frame.appendText("It is now nighttime");
		frame.appendText("Alive players are: "+controller.getPlayers());
		frame.setPlayers(controller.getPlayers());
	}

	@Override
	public void notifyVote(String voter, String votee) {
		frame.appendText("[*] <b>"+voter+"</b> voted for <b>"+votee+"</b>");
	}

	@Override
	public synchronized void notifyMessage(String who, String message) {
		frame.appendText("&lt;<b>"+who+"</b>&gt; "+message);
	}

	@Override
	public void notifyDeath(String name, String cause) {
		if (name == null) {
			return;
		}
		
		switch(cause) {
			case "lynch":
				frame.appendText("[*] the villagers lynch "+name);
				break;
				
			case "timeout":
				frame.appendText("[*] <b>"+name+"</b> suddenly falls down dead");
				break;
				
			case "eaten":
				frame.appendText("[*] <b>"+name+"</b> was eaten by wolves");
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

	@Override
	public void triggerAction() {
		// TODO Auto-generated method stub
		
	}

}
