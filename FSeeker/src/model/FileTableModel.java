/*
 * Created on 29 oct. 2004
 */
package model;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import misc.file.FileUtilities.FileDetails;

/**
 * Classe qui permet la cr�ation d'un mod�le de syst�me de fichier pour la
 * repr�sentation sous forme d'une vue en table (vue d�tails) ou vue MacOS.
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableModel extends DefaultTableModel implements Observer {

	/** Mode simple d�tails */
	public static int SIMPLE_MODE = 0;

	/** Mode � la MacOS */
	public static int SPECIAL_MODE = 1;

	/** Mode recherche */
	public static int SEARCH_MODE = 2;
	
	/** Mode par d�faut */
	protected int MODE = SIMPLE_MODE;

	/** La derni�re colonne s�lectionn�e */
	protected int lastSelectedColumn = -1;

	/** Les colonnes */
	protected Vector colNames = new Vector();

	/** Le supra-mod�le */
	protected FSeekerModel fsm = null;

	/**
	 * Constructeur d'un mod�le de table.
	 * 
	 * @param fsm
	 *            Le supra-mod�le associ�
	 * @param mode
	 *            Le mode choix entre FileTableModel.SPECIAL_MODE et
	 *            FileTableModel.SIMPLE_MODE
	 */
	public FileTableModel(FSeekerModel fsm, int mode) {
		this.fsm = fsm;
		if (fsm != null)
			fsm.addObserver(this);
		this.MODE = mode;

		colNames.addElement("Nom du fichier");
		if (mode == SEARCH_MODE)
			colNames.addElement("Chemin");
		colNames.addElement("Taille");
		colNames.addElement("Type");
		colNames.addElement("Date de Modification");
		
		if (MODE == SPECIAL_MODE)
			setDataForSpecialView();
		else {
			setColumnIdentifiers(colNames);
			if (MODE == SIMPLE_MODE)
				setDataForSimpleView();
		}

	}

	/**
	 * Retourne le supra-mod�le associ�.
	 * 
	 * @return supra-mod�le associ�
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	/**
	 * Appel�e quand le supra-mod�le est modifi�e.
	 */
	public void update(Observable o, Object arg) {
		if (MODE == SPECIAL_MODE)
			setDataForSpecialView();
		else 
			setDataForSimpleView();

		fireTableDataChanged();
	}

	/**
	 * Actualise la table. <br>
	 * Cette m�thode est appel� uniquement quand le MODE vaut
	 * FileTableModel.SIMPLE_MODE.
	 */
	public void setDataForSimpleView() {

		File[] files = fsm.getFilesList();

		if (files == null) {
			setDataVector(null, colNames);
			return;
		}

		int nbColumns = colNames.size();
		int nbRows = files.length;

		Vector data = new Vector(nbRows);
		Vector rowData = null;

		for (int rows = 0; rows < nbRows; rows++) {

			rowData = new Vector(nbColumns);

			FileDetails fd = new FileDetails(files[rows]);

			for (int cols = 0; cols < nbColumns; cols++) {
				switch (cols) {
				case 0:
					rowData.addElement(files[rows]);
					break;
				case 1:
					rowData.addElement(fd.getSize());
					break;

				case 2:
					rowData.addElement(fd.getType());
					break;

				case 3:
					rowData.addElement(new Date(files[rows].lastModified()));
					break;
				}
			}

			data.add(rowData);
		}

		setDataVector(data, colNames);
	}

	/**
	 * Renvoie le mode du mod�le.
	 * 
	 * @return SPECIAL_MODE ou SIMPLE_MODE
	 */
	public int getMode() {
		return MODE;
	}

	/**
	 * Actualise la table. <br>
	 * Cette m�thode est appel� uniquement quand le MODE vaut
	 * FileTableModel.SPECIAL_MODE.
	 */
	public void setDataForSpecialView() {
		// On r�cup�re les fichiers du r�pertoire dans lequel on vient d'entrer
		File[] files = fsm.getFilesList();

		// On enl�ve les colonnes obsol�tes, qui ne sont plus dans la bonne
		// arborescence
		if (getColumnCount() > 0)
			removeColumns(lastSelectedColumn + 1, getColumnCount() - 1);

		// On ajoute la colonne contenant les fichiers du r�pertoire dans lequel
		// on vient d'entrer
		addColumn("", files);
	}

	/**
	 * Fixe la derni�re colonne s�lectionn�e.
	 * 
	 * @param column
	 *            num�ro de colonne
	 */
	public void setSelectedColumn(int column) {
		lastSelectedColumn = column;
	}

	/**
	 * Retire une colonne du mod�le.
	 * 
	 * @param columnIndex
	 *            L'index de la colonne � retirer
	 */
	private void removeColumn(int columnIndex) {
		for (int row = 0; row < getRowCount(); row++) {
			Vector rowVector = (Vector) dataVector.elementAt(row);
			if (columnIndex >= rowVector.size())
				break; // On peut breaker directement car y'a pu rien en dessous
			rowVector.removeElementAt(columnIndex);
		}
	}

	/**
	 * Renvoie le nombre de ligne r�ellement utilis�es.
	 * 
	 * @return nombre de ligne ayant des valeurs toutes non nulles
	 */
	protected int getNonNullRowCount() {
		int size = super.getRowCount();

		// On parcours les lignes
		for (int i = 0; i < size; i++) {
			Vector colv = (Vector) dataVector.get(i);

			// Quand on trouve un ligne avec que des nulls dans les colonnes, on
			// set le nombre de ligne � cette ligne ci - 1.
			Iterator itc = colv.iterator();
			boolean tousNulls = true;
			while (itc.hasNext())
				if (itc.next() != null) {
					tousNulls = false;
					break;
				}

			// Si on a pas break�, alors la condition sera vraie, donc que des
			// nulls
			if (tousNulls)
				return i;
		}

		return size;
	}

	/**
	 * Supprime les colonnes de from � to inclus.
	 * 
	 * @param from
	 *            indice de d�part
	 * @param to
	 *            indice de fin
	 */
	protected void removeColumns(int from, int to) {
		int oldColumnCount = getColumnCount();
		for (int i = from; i <= to; i++)
			removeColumn(i);
		setColumnCount(oldColumnCount - (to - from + 1));

		// On oublie PAS de mettre � jour le nombre de ligne
		setRowCount(getNonNullRowCount());
	}

	/**
	 * @inheritDoc
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}