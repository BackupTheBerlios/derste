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
 * R�cup�re les �v�nements d'une vue de table, et les associe � un mod�le de
 * table.
 * 
 * @author brahim
 * @author Sted
 */
public class FileTableControler extends MouseAdapter implements KeyListener,
		SelectionChangedListener {

	/** Le mod�le associ� */
	protected FileTableModel m = null;

	protected FileTableGUI gui = null;

	/**
	 * Construit un contr�leur � partir d'un mod�le.
	 * 
	 * @param m
	 *            un mod�le de table
	 */
	public FileTableControler(FileTableModel m, FileTableGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Quand on clique, ca ouvre le dossier si on est positionn� sur un dossier.
	 * 
	 * @param e
	 *            l'�v�nement associ�
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
				// Le popup � l'ext�rieur des �l�ments
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
	 * Retourne l'�l�ment s�lectionn� (le fichier p�re), et si le mod�le est en
	 * mode sp�cial, il met � jour la derni�re colonne s�lectionn�e.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 * @return l'objet s�lectionn� ou null si aucun
	 */
	private Object getSelectedObject(InputEvent e) {
		JTable source = (JTable) e.getSource();
		int lg = source.getSelectedRow();
		int col = source.getSelectedColumn();
		if (lg == -1 || col == -1)
			return null;

		// Si on est en mode sp�cial on fixe la valeur de la colonne
		// s�lectionn�e
		if (m.getMode() == FileTableModel.SPECIAL_MODE)
			m.setSelectedColumn(((JTable) e.getSource()).getSelectedColumn());

		return source.getValueAt(lg, col);
	}

	/**
	 * Si une touche a �t� appuy�.
	 * 
	 * @param e
	 *            l'�v�nement associ�
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
			// Si on est en mode sp�cial on fixe la valeur de la colonne
			// s�lectionn�e
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