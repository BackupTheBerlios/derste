/*
 * Created on 6 nov. 2004
 */
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import misc.ImagesMap;
import model.URIModel;

/**
 * La toolbar.
 * 
 * @author Sted
 */
public class ToolBarGUI extends JToolBar {

	/** L'application FSeeker o� mettre la toolbar */
	protected FSeeker fs = null;

	/**
	 * Construit une toolbar.
	 * 
	 * @param fs
	 *            la classe principale
	 */
	public ToolBarGUI(final FSeeker fs) {
		super(JToolBar.HORIZONTAL);
		this.fs = fs;

		setLayout(new BorderLayout());
		setFloatable(false);

		add(getIcons(), BorderLayout.NORTH);
		add(getAddress(), BorderLayout.SOUTH);
	}

	/**
	 * Retourne la partie contenant l'addresse.
	 */
	protected JPanel getAddress() {
		URIModel uriModel = new URIModel(fs.getModel());
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("Adresse : "), BorderLayout.WEST);
		p.add(new URIGUI(uriModel), BorderLayout.CENTER);

		return p;
	}

	/**
	 * Retourne la partie contenant les ic�nes.
	 */
	protected JPanel getIcons() {
		// La barre d'ic�nes
		JPanel p = null;

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getModel().gotoPrevious();
			}
		}, ImagesMap.get("previous.png"), "Pr�c�dent", p);

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getModel().gotoNext();
			}
		}, ImagesMap.get("next.png"), "Suivant", p);

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getModel().gotoParent();
			}
		}, ImagesMap.get("parent.png"), "R�pertoire parent", p);

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getCB().setSelectedItem("Arborescence");
			}
		}, ImagesMap.get("arborescence.png"),
				"Afficher l'arborescence sous forme d'arbre", p);

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getCB().setSelectedItem("Recherche");
			}
		}, ImagesMap.get("search.png"),
				"Rechercher un fichier ou un r�pertoire", p);

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getCB().setSelectedItem("Favoris");
			}
		}, ImagesMap.get("bookmarks.png"), "Afficher les favoris", p);

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getBookmarksGUI().addBookmark(fs.getModel().getURI());
			}
		}, ImagesMap.get("addbookmark.png"),
				"Ajouter le r�pertoire courant dans les favoris", p);

		createButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getCB().setSelectedItem("Information");
			}
		}, ImagesMap.get("information.png"),
				"Afficher les informations de la s�lection", p);

		return p;
	}

	/**
	 * Ajoute un bouton � un panel. M�thode pour ne pas recopier toujours le
	 * m�me code.
	 * 
	 * @param al
	 *            l'action du bouton
	 * @param icon
	 *            son ic�ne
	 * @param tooltip
	 *            son tooltip
	 * @param p
	 *            le panel o� ajouter
	 */
	private static void createButton(ActionListener al, Icon icon,
			String tooltip, JPanel p) {
		JButton b = new JButton();
		b.addActionListener(al);
		b.setBorder(null);
		b.setIcon(icon);
		b.setToolTipText(tooltip);
		p.add(b);
	}

}

