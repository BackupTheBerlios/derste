package misc;



import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

import java.net.URL;
import java.io.IOException;

/**
 * @author brahim
 * 
 * Classe qui ouvre une fenêtre qui permet de visualiser des fichiers locaux HTML (ou
 * autre) avec notamment la gestion des liens hypertextes
 *
 */
public class View extends JFrame implements HyperlinkListener {

	private JEditorPane viewer = new JEditorPane();

	private String home;

	private boolean DEBUG = false;

	public View(String home) {
		JScrollPane scrollPane = new JScrollPane(viewer);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		viewer.setEditable(false);
		viewer.setContentType("text/html");
		// Ajout du listener pour les hyperliens notamment
		viewer.addHyperlinkListener(this);
		this.home = home;		
		loadPage(home);
	}

	/*
	 * Méthode appelée après clic sur un hyperlien
	 * 
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	public void hyperlinkUpdate(HyperlinkEvent event) {
		String lien = "";
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			// Chargement de la page
			lien = new String((event.getDescription()).toString());
			loadPage(lien);
		}
	}

	private void loadPage(String urlText) {
		try {
			URL file = View.class.getResource(".." + GU.SEP + "resources"
					+ GU.SEP + urlText);
			if (DEBUG)
				System.out.println("LIEN URL : " + file);
			viewer.setPage(file);

		} catch (IOException ex) {
			GU.message("Accés impossible à " + urlText);
		}
	}

	/**
	 * @param args
	 *            Méthode de test pour la classe View
	 */
	public static void main(String[] args) {
		View v = new View("help.html");
		v.setSize(500, 500);
		GU.center(v);
		v.setVisible(true);
		v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}