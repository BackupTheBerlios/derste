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
 * Classe qui permet la création d'un modèle de système de fichier pour la
 * représentation sous forme d'une vue en table (vue détails) ou vue MacOS.
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableModel extends DefaultTableModel implements Observer {

	/** Mode simple détails */
	public static int SIMPLE_MODE = 0;

	/** Mode à la MacOS */
	public static int SPECIAL_MODE = 1;

	/** Mode recherche */
	public static int SEARCH_MODE = 2;
	
	/** Mode par défaut */
	protected int MODE = SIMPLE_MODE;

	/** La dernière colonne sélectionnée */
	protected int lastSelectedColumn = -1;

	/** Les colonnes */
	protected Vector colNames = new Vector();

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

	/**
	 * Constructeur d'un modèle de table.
	 * 
	 * @param fsm
	 *            Le supra-modèle associé
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
	 * Retourne le supra-modèle associé.
	 * 
	 * @return supra-modèle associé
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	/**
	 * Appelée quand le supra-modèle est modifiée.
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
	 * Cette méthode est appelé uniquement quand le MODE vaut
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
	 * Renvoie le mode du modèle.
	 * 
	 * @return SPECIAL_MODE ou SIMPLE_MODE
	 */
	public int getMode() {
		return MODE;
	}

	/**
	 * Actualise la table. <br>
	 * Cette méthode est appelé uniquement quand le MODE vaut
	 * FileTableModel.SPECIAL_MODE.
	 */
	public void setDataForSpecialView() {
		// On récupère les fichiers du répertoire dans lequel on vient d'entrer
		File[] files = fsm.getFilesList();

		// On enlève les colonnes obsolètes, qui ne sont plus dans la bonne
		// arborescence
		if (getColumnCount() > 0)
			removeColumns(lastSelectedColumn + 1, getColumnCount() - 1);

		// On ajoute la colonne contenant les fichiers du répertoire dans lequel
		// on vient d'entrer
		addColumn("", files);
	}

	/**
	 * Fixe la dernière colonne sélectionnée.
	 * 
	 * @param column
	 *            numéro de colonne
	 */
	public void setSelectedColumn(int column) {
		lastSelectedColumn = column;
	}

	/**
	 * Retire une colonne du modèle.
	 * 
	 * @param columnIndex
	 *            L'index de la colonne à retirer
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
	 * Renvoie le nombre de ligne réellement utilisées.
	 * 
	 * @return nombre de ligne ayant des valeurs toutes non nulles
	 */
	protected int getNonNullRowCount() {
		int size = super.getRowCount();

		// On parcours les lignes
		for (int i = 0; i < size; i++) {
			Vector colv = (Vector) dataVector.get(i);

			// Quand on trouve un ligne avec que des nulls dans les colonnes, on
			// set le nombre de ligne à cette ligne ci - 1.
			Iterator itc = colv.iterator();
			boolean tousNulls = true;
			while (itc.hasNext())
				if (itc.next() != null) {
					tousNulls = false;
					break;
				}

			// Si on a pas breaké, alors la condition sera vraie, donc que des
			// nulls
			if (tousNulls)
				return i;
		}

		return size;
	}

	/**
	 * Supprime les colonnes de from à to inclus.
	 * 
	 * @param from
	 *            indice de départ
	 * @param to
	 *            indice de fin
	 */
	protected void removeColumns(int from, int to) {
		int oldColumnCount = getColumnCount();
		for (int i = from; i <= to; i++)
			removeColumn(i);
		setColumnCount(oldColumnCount - (to - from + 1));

		// On oublie PAS de mettre à jour le nombre de ligne
		setRowCount(getNonNullRowCount());
	}

	/**
	 * @inheritDoc
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}