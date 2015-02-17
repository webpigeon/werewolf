package uk.me.webpigeon.wolf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.me.webpigeon.wolf.newcode.players.HumanPlayer;

/**
 * An interactive panel to allow players to play with the NPCs
 *
 */
public class InteractivePanel implements ActionListener {
	private HumanPlayer player;
	private JPanel base;
	private JTextField inputBox;
	
	public InteractivePanel(HumanPlayer player) {
		this.player = player;
		buildUI();
	}
	
	private void buildUI(){
		base = new JPanel();
		
		inputBox = new JTextField(60);
		inputBox.addActionListener(this);
		base.add(inputBox);
	}
	
	
	public JComponent getComponent() {
		return base;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String inputMessage = inputBox.getText();
		inputBox.setText("");
		
		// find out if the player typed an action or a message
		String[] args = inputMessage.split(" ");
		if (args[0].startsWith("/")) {
			String action = args[0].substring(1);
			player.perform(action, args);
			return;
		} else {
			player.say(inputMessage);
		}
	}
}
