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
 * Le contrôleur qui agit sur un modèle et une vue d'URI.
 * 
 * @author sted
 */
public class URIControler implements ActionListener, URIChangedListener {

	/** Le modèle associé */
	protected URIModel m = null;

	/** La vue associée */
	protected URIGUI gui = null;

	/**
	 * Construit un contrôleur à partir d'un modèle et d'une vue.
	 * 
	 * @param m
	 *            un modèle d'URI
	 * @param gui
	 *            une vue d'URI
	 */
	public URIControler(URIModel m, URIGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Appelé quand l'utilisateur validera une saisie dans la vue.
	 * 
	 * @param e
	 *            l'événement associé
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