package uk.me.webpigeon.wolf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import uk.me.webpigeon.wolf.WolfGame;

public class WolfController implements ActionListener {
	private static final Integer TICKS_PER_ROUND = 300;
	
	private WolfFrame frame;
	private Timer timer;
	private WolfGame game;
	
	private int roundTicksLeft;
	
	public WolfController(WolfFrame frame, WolfGame game) {
		this.frame = frame;
		this.game = game;
		this.timer = new Timer(50, this);
		timer.start();
	}
	
	public void setRoundStarted(boolean daytime) {
		roundTicksLeft = TICKS_PER_ROUND;
		timer.restart();
		frame.notifyTicks(TICKS_PER_ROUND, TICKS_PER_ROUND);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (roundTicksLeft <= 0) {
			game.timeout();
			timer.stop();
			return;
		}
		
		roundTicksLeft--;
		frame.notifyTicks(roundTicksLeft, TICKS_PER_ROUND);
	}
	

}
