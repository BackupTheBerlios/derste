/*
 * Created on 21 oct. 2004
 */
package controler;

import gui.URIGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import misc.file.FileUtilities;
import model.URIModel;

/**
 * Le contr�leur qui agit sur un mod�le et une vue d'URI.
 * 
 * @author sted
 */
public class URIControler implements ActionListener, URIChangedListener {

	/** Le mod�le associ� */
	protected URIModel m = null;

	/** La vue associ�e */
	protected URIGUI gui = null;

	/**
	 * Construit un contr�leur � partir d'un mod�le et d'une vue.
	 * 
	 * @param m
	 *            un mod�le d'URI
	 * @param gui
	 *            une vue d'URI
	 */
	public URIControler(URIModel m, URIGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Appel� quand l'utilisateur validera une saisie dans la vue.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void actionPerformed(ActionEvent e) {
		URIGUI ugui = (URIGUI) e.getSource();
		String s = ugui.getText();
		File f = new File(s);
		if (f.isFile()) {
			m.getModel().setURI(f.getParentFile());
			FileUtilities.openFile(f);
		}
		else
			m.getModel().setURI(f);
	}

	/**
	 * @inheritDoc
	 */
	public void URIChanged(URIChangedEvent e) {
		gui.setText(e.getURI().getAbsolutePath());
	}

}