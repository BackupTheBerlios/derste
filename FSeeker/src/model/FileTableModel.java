package model;

import java.io.File;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Classe qui permet la création d'un modèle de système de fichier pour la
 * représentation sous forme d'une vue en table (vue détails)
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableModel extends DefaultTableModel implements Observer {

	protected Vector colNames = new Vector(4);

	{
		colNames.addElement("Nom du fichier");
		colNames.addElement("Taille");
		colNames.addElement("Type");
		colNames.addElement("Date de Modification");
	}

	protected FSeekerModel fsm = null;

	public FileTableModel(FSeekerModel fsm) {
		this.fsm = fsm;
		fsm.addObserver(this);
		
		setColumnIdentifiers(colNames);
		setData();
	}

	/**
	 * @return Le Méta-model
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	public void update(Observable o, Object arg) {
		setData();
		fireTableDataChanged();
	}

	public Class getColumnClass(int col) {
        int rowIndex = 0;
        Object o = getValueAt(rowIndex, col);
        if (o == null) {
            return Object.class;
        } else {
            return o.getClass();
        }
    }
	
	/**
	 * Récupére la nouvelle URI du méta-modéle et actualise les valeurs
	 */
	public void setData() {
		File[] files = fsm.getURI().listFiles();

		int nbColumns = colNames.size();
		int nbRows = files.length;

		Vector data = new Vector(nbRows);
		Vector rowData = null;

		for (int rows = 0; rows < nbRows; rows++) {
			
			rowData = new Vector(nbColumns);
			
			for (int cols = 0; cols < nbColumns; cols++) {
				switch (cols) {
				case 0:
					rowData.addElement(files[rows]);
					break;

				case 1:
					rowData.addElement(files[rows].length() + " octets");
					break;

				case 2:
					rowData.addElement(files[rows].isDirectory() ? "Dossier"
							: "Fichier");
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
}