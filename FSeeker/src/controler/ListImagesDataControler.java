package controler;

import gui.ListImagesGUI;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import misc.PopupManager;
import model.ListImagesModel;
import model.SelectionChangedEvent;
import model.SelectionChangedListener;

/**
 * G�re les mod�les et les vues ListImages*.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesDataControler extends MouseAdapter implements
		KeyListener, ListDataListener, SelectionChangedListener {

	/** Le mod�le de liste � contr�ler */
	protected ListImagesModel m = null;

	/** La vue associ�e */
	protected ListImagesGUI gui = null;

	/**
	 * Construit le contr�leur associ� au mod�le <code>m</code>.
	 * 
	 * @param m
	 *            le mod�le
	 */
	public ListImagesDataControler(ListImagesModel m, ListImagesGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Retourne le popup d'un fichier s�lectionn� ou null si ce fichier est
	 * null.
	 * 
	 * @param f
	 *            un fichier (normalement, celui en s�lection)
	 * @return le popup
	 */
	public JPopupMenu getPopupIn(final File f) {
		if (f == null)
			return null;

		// Le popup
		JPopupMenu popup = new JPopupMenu();

		// Pour cr�er plus facilement et clairement des popups
		PopupManager pm = new PopupManager(f, m.getModel());

		popup.add(pm.getFileName());
		popup.addSeparator();
		popup.add(pm.getOpen());
		popup.add(pm.getCreateDirectory());
		popup.add(pm.getCreateFile());
		popup.addSeparator();
		popup.add(pm.getCut());
		popup.add(pm.getCopy());
		popup.add(pm.getPaste());
		popup.add(pm.getRename());
		popup.addSeparator();
		popup.add(pm.getDelete());
		popup.add(pm.getRefresh());
		popup.addSeparator();
		popup.add(pm.getDisplay());
		popup.addSeparator();
		popup.add(pm.getProperties());

		return popup;
	}

	/**
	 * Retourne le popup situ� en dehors de la liste des fichiers.
	 * 
	 * @return le popup
	 */
	public JPopupMenu getPopupOut() {
		// Le popup
		JPopupMenu popup = new JPopupMenu();

		// Pour cr�er plus facilement et clairement des popups
		PopupManager pm = new PopupManager(m.getModel().getURI(), m.getModel());

		popup.add(pm.getFileName());
		popup.addSeparator();
		popup.add(pm.getCreateDirectory());
		popup.add(pm.getCreateFile());
		popup.addSeparator();
		popup.add(pm.getPaste());
		popup.add(pm.getRefresh());
		popup.addSeparator();
		popup.add(pm.getDisplay());
		popup.addSeparator();
		popup.add(pm.getProperties());

		return popup;
	}
	
	/**
	 * Quand on clique, ca ouvre le dossier si on est positionn� sur un dossier.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void mouseClicked(MouseEvent e) {
		// Si on a un clic droit
		if (SwingUtilities.isRightMouseButton(e)) {

			JList list = (JList) e.getSource();

			Point clic = e.getPoint();
			int index = list.locationToIndex(clic);
			Rectangle r = list.getCellBounds(index, index);
			if (r.contains(clic)) {
				list.setSelectedIndex(index);
				File f = (File) m.getElementAt(index);
				PopupManager.showPopup(e, getPopupIn(f));
			} else {
				// Le popup � l'ext�rieur des �l�ments
				PopupManager.showPopup(e, getPopupOut());
			}
		}

		// Sinon, si c'est un gauche seul
		else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == m.getModel().getClickCount()) {
			File f = (File) ((JList) e.getSource()).getSelectedValue();
			m.getModel().setURI(f);
		}

	}

	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Si une touche a �t� appuy�.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void keyPressed(KeyEvent e) {
		File f = (File) ((JList) e.getSource()).getSelectedValue();
		GeneralControler.keyPressed(e, f, m.getModel());
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void contentsChanged(ListDataEvent e) {
		gui.ensureIndexIsVisible(0);
		gui.setSelectedIndex(0);
	}

	public void intervalAdded(ListDataEvent e) {
	}

	public void intervalRemoved(ListDataEvent e) {
	}

	public void selectionChanged(SelectionChangedEvent e) {
		gui.setSelectedValue(e.getSelection(), true);
	}
}