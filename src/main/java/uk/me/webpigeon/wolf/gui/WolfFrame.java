package uk.me.webpigeon.wolf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Collection;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class WolfFrame {
	private JFrame frame;
	private DefaultListModel<String> players;
	private InteractivePanel panel;
	private GameLog log;
	
	
	public WolfFrame(InteractivePanel panel) {
		frame = new JFrame();
		this.panel = panel;
		initGUI();
	}
	
	private void initGUI() {
		
		frame.setTitle("AI Warewolf");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		
		players = new DefaultListModel<String>();
		JList<String> playerList = new JList<String>(players);
		
		log = new GameLog();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(playerList);
		splitPane.setRightComponent(log.getComponent());
		splitPane.setResizeWeight(0.20);
		
		frame.add(splitPane, BorderLayout.CENTER);
		
		frame.add(panel.getComponent(), BorderLayout.SOUTH);
	}

	public void setPlayers(Collection<String> alivePlayers) {
		players.clear();
		for (String player : alivePlayers) {
			players.add(0, player);
		}
	}
	
	public void start() {
		frame.pack();
		frame.setVisible(true);
	}
	
	public void printMessage(String message) {
		log.appendText(message);
	}
	
	public void printContext(String message) {
		log.appendText("<i>["+message+"]</i>");
	}
	
	public void printNarrative(String message) {
		log.appendText("<i>"+message+"</i>");
	}

	public void printMessage(String player, String message) {
		log.appendText("&lt;<b>"+player+"</b>&gt; "+message);
	}
}
