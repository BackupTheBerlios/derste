package gui;

import javax.swing.JTextField;

import model.URIModel;
import controler.URIControler;

/**
 * Une vue d'URIModel.
 * 
 * @author Sted
 */
public class URIGUI extends JTextField {

	/** Le mod�le que la vue affiche */
	protected URIModel m = null;

	/**
	 * Construit une vue d'uri � partir d'un mod�le.
	 * 
	 * @param m
	 *            un mod�le d'uri avec lequelle associer la vue
	 */
	public URIGUI(URIModel m) {
		super(m.getModel().getURI().getAbsolutePath());
		this.m = m;
		
		URIControler uc = new URIControler(m, this);
		addActionListener(uc);
		setColumns(20);
		m.addURIChangedListener(uc);
	}

}