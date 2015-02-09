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
	private DefaultBoundedRangeModel progressBarModel;
	
	private HTMLEditorKit kit;
	private HTMLDocument document;
	
	public WolfFrame() {
		frame = new JFrame();
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
		
		kit = new HTMLEditorKit();
		document = (HTMLDocument)kit.createDefaultDocument();
		JTextPane console = new JTextPane();
		console.setEditable(false);
		console.setEditorKit(kit);
		console.setDocument(document);
		
		//console.setEditable(false);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playerList, console);
		splitPane.add(new JScrollPane(console));
		splitPane.setResizeWeight(0.20);
		
		frame.add(splitPane, BorderLayout.CENTER);
	}

	public void setPlayers(Collection<String> alivePlayers) {
		players.clear();
		for (String player : alivePlayers) {
			players.add(0, player);
		}
	}
	
	public void appendText(String text) {
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
	
	public void printContext(String message) {
		appendText("<i>["+message+"]</i>");
	}
	
	public void printNarrative(String message) {
		appendText("<i>"+message+"</i>");
	}

	public void printMessage(String player, String message) {
		appendText("&lt;<b>"+player+"</b>&gt; "+message);
	}
}
