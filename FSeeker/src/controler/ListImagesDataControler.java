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
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import misc.PopupManager;
import model.ListImagesModel;
import model.SelectionChangedEvent;
import model.SelectionChangedListener;

/**
 * Gère les modèles et les vues ListImages*.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesDataControler extends MouseAdapter implements
		KeyListener, ListDataListener, SelectionChangedListener,
		ListSelectionListener {

	/** Le modèle de liste à contrôler */
	protected ListImagesModel m = null;

	/** La vue associée */
	protected ListImagesGUI gui = null;

	/**
	 * Construit le contrôleur associé au modèle <code>m</code>.
	 * 
	 * @param m
	 *            le modèle
	 */
	public ListImagesDataControler(ListImagesModel m, ListImagesGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Quand on clique, ca ouvre le dossier si on est positionné sur un dossier.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void mouseClicked(MouseEvent e) {
		// Si on a un clic droit > popup
		if (SwingUtilities.isRightMouseButton(e)) {

			JList list = (JList) e.getSource();

			Point clic = e.getPoint();
			int index = list.locationToIndex(clic);
			Rectangle r = list.getCellBounds(index, index);
			if (r != null && r.contains(clic)) {
				list.setSelectedIndex(index);
				File f = (File) m.getElementAt(index);
				PopupManager.showPopup(e, PopupManager.getDefaultPopupIn(f, m.getModel()));
			} else {
				// Le popup à l'extérieur des éléments
				PopupManager.showPopup(e, PopupManager.getDefaultPopupOut(m.getModel()));
			}
		}

		// Sinon, si c'est un gauche > ouverture
		else if (SwingUtilities.isLeftMouseButton(e)
				&& e.getClickCount() == m.getModel().getClickCount()) {
			File f = (File) ((JList) e.getSource()).getSelectedValue();
			if (f.isDirectory())
				m.getModel().setURI(f);
		}

	}

	/**
	 * Si une touche a été appuyé.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void keyPressed(KeyEvent e) {
		File f = (File) ((JList) e.getSource()).getSelectedValue();
		GeneralControler.keyPressed(e, f, m.getModel());
	}

	/**
	 * Le contenu du modèle a été modifié, on s'assure de revenir au début de la
	 * liste.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void contentsChanged(ListDataEvent e) {
		gui.ensureIndexIsVisible(0);

		// Ne pas mettre de sélection sinon la sélection du supra-modèle ne sera
		// pas concordante
		if (m.getSize() > 0)
			gui.clearSelection();
	}

	/**
	 * La sélection du modèle a été modifié, on met à jour la vue.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void selectionChanged(SelectionChangedEvent e) {
		if (gui.getSelectedValue() == null || !gui.getSelectedValue().equals(e.getSelection()))
			gui.setSelectedValue(e.getSelection(), true);
	}

	/**
	 * La sélection de la vue a été modifié, on met à jour le modèle.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			JList source = (JList) e.getSource();
			Object o = source.getSelectedValue();
			if (o instanceof File) {
				if (!m.getModel().getSelection().equals(o))
					m.getModel().setSelection((File) o, source);
			}
		}
	}

	public void intervalAdded(ListDataEvent e) {
	}

	public void intervalRemoved(ListDataEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}
}