package uk.me.webpigeon.wolf.gui;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class GameLog {
	private HTMLEditorKit kit;
	private HTMLDocument document;
	private JComponent component;
	
	public GameLog() {
		buildUI();
	}
	
	private void buildUI() {
		kit = new HTMLEditorKit();
		document = (HTMLDocument)kit.createDefaultDocument();
		JTextPane console = new JTextPane();
		console.setEditable(false);
		console.setEditorKit(kit);
		console.setDocument(document);
		
		component = new JScrollPane(console);
	}

	public void appendText(String text) {
		try {
			kit.insertHTML(document, document.getLength(), text, 0, 0, null);
		} catch (IOException | BadLocationException ex) {
			System.err.println(ex);
		}
	}
	
	public JComponent getComponent() {
		return component;
	}

}
