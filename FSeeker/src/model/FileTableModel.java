/*
 * Created on 29 oct. 2004
 *
 *
 */
package model;

import java.io.File;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import java.util.*;
import gui.FileTableGUI;
import javax.swing.JTable;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Classe qui permet la cr�ation d'un mod�le de syst�me de fichier pour la
 * repr�sentation sous forme d'une vue en table (vue d�tails)
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableModel extends DefaultTableModel implements Observer {

    public static int SIMPLE_MODE = 0;

    public static int SPECIAL_MODE = 1;

    private int MODE = -1;

    /* La derni�re colonne selectionn�e */
    private int lastSelectedColumn = -1;

    protected Vector colNames = new Vector(4);

    {
        colNames.addElement("Nom du fichier");
        colNames.addElement("Taille");
        colNames.addElement("Type");
        colNames.addElement("Date de Modification");
    }

    protected FSeekerModel fsm = null;

    /**
     * Contructeur d'un model
     * 
     * @param fsm
     *            Le supra-mod�le associ�
     * @param mode
     *            Le mode choix entre FileTableModel.SPECIAL_MODE et
     *            FileTableModel.SIMPLE_MODE
     */
    public FileTableModel(FSeekerModel fsm, int mode) {
        this.fsm = fsm;
        fsm.addObserver(this);
        this.MODE = mode;

        if (MODE == SPECIAL_MODE)
            setDataForSpecialView();
        else {
            setColumnIdentifiers(colNames);
            setDataForSimpleView();
        }

    }

    public FSeekerModel getModel() {
        return fsm;
    }

    public void update(Observable o, Object arg) {
        System.out.println("MODEL PRINCIPAL A CHANGE");

        //Mise � jour des donn�es
        if (MODE == SPECIAL_MODE)
            setDataForSpecialView();
        else
            setDataForSimpleView();
        //fireTableStructureChanged();
        //fireTableDataChanged();

    }

    /***************************************************************************
     * public Class getColumnClass(int col) { int rowIndex = 0; Object o =
     * getValueAt(rowIndex, col); if (o == null) { return Object.class; } else {
     * return o.getClass(); } }
     **************************************************************************/

    /**
     * R�cup�re la nouvelle URI du m�ta-mod�le et actualise les valeurs Apell�
     * quand le MODE vaut FileTableModel.SIMPLE_MODE
     */
    public void setDataForSimpleView() {
        File[] files = fsm.getFilesList();

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
                    rowData.addElement(new Long(files[rows].length()));
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

    /**
     * Mise � jour de la liste des fichiers pour la vue special (style MacOs)
     *  
     */
    public void setDataForSpecialView() {

        //Quand clique sur un dossier on retire tout element(s) et on met les
        //fils de celui sur lequel on vient de cliquer
        
        File[] files = fsm.getFilesList();

        //TODO A TESTER DANS MODERATION PEUT ETRE DES BUGS
        //Condition pour le cas ou la table ne contient aucune colonne
        if (!(getColumnCount() == 0))
            for (int i = lastSelectedColumn + 1; i <= getColumnCount() - 1;)
                removeColumn(i);

        //On ajute la colonne contenant les fichiers � explorer
        addColumn("", files);

    }

    /**
     * 
     * On fixe la valeur de la d�rni�re colonne selectionn�e
     */
    public void setSelectedColumn(int column) {
        lastSelectedColumn = column;
    }

    /**
     * Renvoie le mode s�lectionn�
     */
    public int getMode() {
        return MODE;
    }

    public void insertcolumn(JTable table2) {
        //TableModel1 = (DefaultTableModel) table2.getModel();
        TableColumn col = new TableColumn();
        col.setHeaderValue(" ");
        table2.addColumn(col);
    }

    /**
     * Surcharge car la super-m�thode remplit les lignes vides par des valeurs
     * copi�es d'une colonne pr�cedente
     */
    public void addColumn(Object columnName, Vector columnData) {
        columnIdentifiers.addElement(columnName);

        if (columnData != null) {

            int columnSize = columnData.size();
            if (columnSize > getRowCount()) {
                dataVector.setSize(columnSize);//Nombre de ligne + gd
            }
            justify(0, getRowCount());
            int newColumn = getColumnCount() - 1;
            for (int i = 0; i < getRowCount()/* columnSize */; i++) {
                Vector row = (Vector) dataVector.elementAt(i);
                // System.out.println("row = " + row);
                if (i < columnSize) {
                    row.setElementAt(columnData.elementAt(i), newColumn);
                } else if (i >= columnSize) {
                    row.setElementAt(new String(""), newColumn);
                }

            }
        } else {
            justify(0, getRowCount());
        }

        fireTableStructureChanged();

    }

    /**
     * Pomp� de la superclasse j'avoue j'ai honte !
     * 
     * @param from
     * @param to
     */
    private void justify(int from, int to) {
        // Sometimes the DefaultTableModel is subclassed
        // instead of the AbstractTableModel by mistake.
        // Set the number of rows for the case when getRowCount
        // is overridden.
        dataVector.setSize(getRowCount());

        for (int i = from; i < to; i++) {
            if (dataVector.elementAt(i) == null) {
                dataVector.setElementAt(new Vector(), i);
            }
            ((Vector) dataVector.elementAt(i)).setSize(getColumnCount());
        }
    }

    /**
     * Retire une colonne du mod�le
     * 
     * @param columnIndex
     *            L'index de la colonne � retirer
     */
    public void removeColumn(int columnIndex) {

        int oldColumnCount = getColumnCount();
        //On retire toutes les valeurs de la colonne columnIndex
        for (int row = 0; row < getColumnCount(); row++) {
            Vector rowVector = (Vector) dataVector.elementAt(row);
            rowVector.removeElementAt(columnIndex);
        }
        this.setColumnCount(oldColumnCount - 1);
    }

    /**
     * @inheritDoc
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}