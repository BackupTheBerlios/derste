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
 * R�cup�re les �v�nements d'une vue de table, et les associe � un mod�le de
 * table.
 * 
 * @author brahim
 * @author Sted
 */
public class FileTableControler extends MouseAdapter implements KeyListener {

	/** Le mod�le associ� */
	protected FileTableModel m = null;

	/**
	 * Construit un contr�leur � partir d'un mod�le.
	 * 
	 * @param m
	 *            un mod�le de table
	 */
	public FileTableControler(FileTableModel m) {
		this.m = m;
	}

	/**
	 * Quand on clique, ca ouvre le dossier si on est positionn� sur un dossier.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1) {
			Object o = getSelectedObject(e);
			GeneralControler.mouseSimpleClick((File) o, m.getModel());
		}
	}

	/**
	 * Retourne l'�l�ment s�lectionn�, et si le mod�le est en mode sp�cial, il
	 * met � jour la derni�re colonne s�lectionn�e.
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

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}