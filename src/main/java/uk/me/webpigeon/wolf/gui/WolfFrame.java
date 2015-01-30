package uk.me.webpigeon.wolf.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Collection;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class WolfFrame {
	private JFrame frame;
	private JPanel cards;
	private JPanel dayScreen;
	private JPanel nightScreen;
	private JProgressBar progressBar;
	private CardLayout layout;
	private DefaultListModel<String> players;
	private DefaultBoundedRangeModel progressBarModel;
	
	private HTMLEditorKit kit;
	private HTMLDocument document;
	
	public WolfFrame() {
		frame = new JFrame();
		dayScreen = new JPanel();
		nightScreen = new JPanel();
		initGUI();
	}
	
	private void initGUI() {
		
		frame.setTitle("AI Warewolf");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		
		
		progressBarModel = new DefaultBoundedRangeModel();
		JProgressBar progressBar = new JProgressBar(progressBarModel);
		frame.add(progressBar, BorderLayout.NORTH);
		
		players = new DefaultListModel<String>();
		JList<String> playerList = new JList<String>(players);
		frame.add(playerList, BorderLayout.WEST);
		
		
		kit = new HTMLEditorKit();
		document = (HTMLDocument)kit.createDefaultDocument();
		JTextPane console = new JTextPane();
		console.setEditable(false);
		console.setEditorKit(kit);
		console.setDocument(document);
		
		//console.setEditable(false);
		frame.add(console, BorderLayout.CENTER);
	}

	public void setPlayers(Collection<String> alivePlayers) {
		players.clear();
		for (String player : alivePlayers) {
			players.add(0, player);
		}
	}
	
	public void apppendText(String text) {
		try {
			kit.insertHTML(document, document.getLength(), text, 0, 0, null);
		} catch (IOException | BadLocationException ex) {
			System.err.println(ex);
		}
	}
	
	public void start() {
		frame.pack();
		frame.setVisible(true);
	}

	public void notifyTicks(int roundTicksLeft, Integer ticksPerRound) {	
		progressBarModel.setMinimum(0);
		progressBarModel.setMaximum(ticksPerRound);
		progressBarModel.setValue(ticksPerRound - roundTicksLeft);
	}
}
