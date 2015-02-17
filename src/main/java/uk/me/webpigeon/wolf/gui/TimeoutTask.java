package uk.me.webpigeon.wolf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uk.me.webpigeon.wolf.newcode.WolfController;
import uk.me.webpigeon.wolf.newcode.actions.AdvanceTurn;


public class TimeoutTask implements ActionListener {
	private WolfController controller;
	
	public TimeoutTask(WolfController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addTask(null, new AdvanceTurn(controller.getTurnCounter()+1));
	}

}
