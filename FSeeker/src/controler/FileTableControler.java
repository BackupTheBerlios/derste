/*
 * Created on 27 oct. 2004
 */
package controler;

import gui.FileTableGUI;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import misc.PopupManager;
import model.FileTableModel;
import model.SelectionChangedEvent;
import model.SelectionChangedListener;

/**
 * Récupère les événements d'une vue de table, et les associe à un modèle de
 * table.
 * 
 * @author brahim
 * @author Sted
 */
public class FileTableControler extends MouseAdapter implements KeyListener,
		SelectionChangedListener {

	/** Le modèle associé */
	protected FileTableModel m = null;

	protected FileTableGUI gui = null;

	/**
	 * Construit un contrôleur à partir d'un modèle.
	 * 
	 * @param m
	 *            un modèle de table
	 */
	public FileTableControler(FileTableModel m, FileTableGUI gui) {
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
			Object o = getSelectedObject(e);
			if (o != null) {
				File f = (File) o;
				PopupManager.showPopup(e, PopupManager.getDefaultPopupIn(f, m
						.getModel()));
			} else {
				// Le popup à l'extérieur des éléments
				PopupManager.showPopup(e, PopupManager.getDefaultPopupOut(m
						.getModel()));
			}
		}

		// Sinon, si c'est un gauche > ouverture
		else if (SwingUtilities.isLeftMouseButton(e)
				&& e.getClickCount() == m.getModel().getClickCount()) {
			Object o = getSelectedObject(e);
			if (((File) o).isDirectory())
				m.getModel().setURI((File) o);
		}
	}

	/**
	 * Retourne l'élément sélectionné (le fichier pére), et si le modèle est en
	 * mode spécial, il met à jour la dernière colonne sélectionnée.
	 * 
	 * @param e
	 *            l'événement associé
	 * @return l'objet sélectionné ou null si aucun
	 */
	private Object getSelectedObject(InputEvent e) {
		JTable source = (JTable) e.getSource();
		int lg = source.getSelectedRow();
		int col = source.getSelectedColumn();
		if (lg == -1 || col == -1)
			return null;

		// Si on est en mode spécial on fixe la valeur de la colonne
		// sélectionnée
		if (m.getMode() == FileTableModel.SPECIAL_MODE)
			m.setSelectedColumn(((JTable) e.getSource()).getSelectedColumn());

		return source.getValueAt(lg, col);
	}

	/**
	 * Si une touche a été appuyé.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void keyPressed(KeyEvent e) {
		Object o = getSelectedObject(e);
		if (o instanceof File)
			GeneralControler.keyPressed(e, (File) o, m.getModel());
	}

	public void selectionChanged(SelectionChangedEvent e) {
		int lg = gui.getSelectedRow();
		int col = gui.getSelectedColumn();
		if (lg == -1 || col == -1) {
			// Si on est en mode spécial on fixe la valeur de la colonne
			// sélectionnée
			FileTableModel m = (FileTableModel) e.getSource();
			if (m.getMode() == FileTableModel.SIMPLE_MODE) {
				if (!gui.getValueAt(gui.getSelectedRow(), gui.getSelectedColumn()).equals(e.getSelection())) {
					int i = m.getIndexOf(e.getSelection(), 0);
					System.out.println("i > " + i);
					gui.getSelectionModel().setSelectionInterval(i, i);
				}
			}

		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}