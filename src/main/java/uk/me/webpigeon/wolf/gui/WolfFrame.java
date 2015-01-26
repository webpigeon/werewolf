package uk.me.webpigeon.wolf.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import uk.me.webpigeon.wolf.Player;

public class WolfFrame {
	private JFrame frame;
	private JPanel cards;
	private JPanel dayScreen;
	private JPanel nightScreen;
	private CardLayout layout;
	private DefaultListModel<Player> players;
	
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
		
		layout = new CardLayout();
		cards = new JPanel(layout);
		
		dayScreen.add(new JLabel("daytime"));
		dayScreen.setBackground(Color.CYAN);
		cards.add(dayScreen, "day");
		nightScreen.add(new JLabel("night time"));
		nightScreen.setBackground(Color.DARK_GRAY);
		cards.add(nightScreen, "night");
		
		frame.add(cards, BorderLayout.NORTH);
		
		players = new DefaultListModel<Player>();
		JList<Player> playerList = new JList<Player>(players);
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

	public void setPlayers(Collection<Player> alivePlayers) {
		players.clear();
		for (Player player : alivePlayers) {
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
	
	public void setDaytime(boolean tf) {
		if (tf) {
			layout.show(cards, "day");
		} else {
			layout.show(cards, "night");
		}
	}
}
