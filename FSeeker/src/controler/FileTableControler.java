/*
 * Created on 27 oct. 2004
 */
package controler;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JTable;

import model.FileTableModel;

/**
 * Récupère les événements d'une vue de table, et les associe à un modèle de
 * table.
 * 
 * @author brahim
 * @author Sted
 */
public class FileTableControler extends MouseAdapter implements KeyListener {

	/** Le modèle associé */
	protected FileTableModel m = null;

	/**
	 * Construit un contrôleur à partir d'un modèle.
	 * 
	 * @param m
	 *            un modèle de table
	 */
	public FileTableControler(FileTableModel m) {
		this.m = m;
	}

	/**
	 * Quand on clique, ca ouvre le dossier si on est positionné sur un dossier.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1) {
			Object o = getSelectedObject(e);
			GeneralControler.mouseSimpleClick((File) o, m.getModel());
		}
	}

	/**
	 * Retourne l'élément sélectionné, et si le modèle est en mode spécial, il
	 * met à jour la dernière colonne sélectionnée.
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

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}