/**
 * 
 * Classe qui permet la cr�ation d'un mod�le de syst�me de fichier pour
 * la repr�sentation sous forme d'une vue en table (vue d�tails)
 * 
 */
package model;

import javax.swing.table.*;
import java.util.*;
import java.io.*;

/**
 * @author aitelhab
 * 
 *  
 */
public class FileTableModel extends Observable implements Observer {

    private DefaultTableModel model;

    private Vector columnNames;

    protected FSeekerModel fsm = null;


    public FileTableModel(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
        model = new DefaultTableModel();
        //on initialise les donn�es
        columnNames = new Vector(0);
        columnNames.addElement("Nom du fichier");
        columnNames.addElement("Taille");
        columnNames.addElement("Type");
        columnNames.addElement("Date de Modif");
        //On cr�e les colonnes
        createColumn();
        setData();
        
    }

    /**
     * @return Le M�ta-model
     */
    public FSeekerModel getModel() {
        return fsm;
    }

    /**
     * Renvoie le mod�le de table cr�e
     * 
     * @return
     */
    public TableModel getTableModel() {
        return model;
    }

    public void update(Observable o, Object arg) {
        System.out.println("Le mod�le principal a chang�");

        setData();//Mise � jour des donn�es
        setChanged();//Le mod�le a chang�
        notifyObservers();//On pr�vient tout les listeners
    }

    /**
     * R�cup�re la nouvelle URI du m�ta-mod�le et actualise les valeurs
     *  
     */
    public void setData() {
        File [] files = fsm.getURI().listFiles();

        int nbColumns = columnNames.size();
        int nbRows = files.length;

        Vector data = new Vector(0);
        //On r�cup�re colonne par colonne pour chaque ligne 
        for (int rows = 0; rows < nbRows; rows++) {
            Vector rowData = new Vector(nbColumns);
            for (int cols = 0; cols < nbColumns; cols++) {
                switch (cols) {
                case 0:
                    rowData.addElement(files[rows]);

                    break;

                case 1:
                    rowData.addElement(files[rows].length() + " octets");
                    break;

                case 2:
                    String type = files[rows].isDirectory() ? "Dossier"
                            : "Fichier";
                    rowData.addElement(type);
                    break;

                case 3:
                    Date lastModif = new Date(files[rows].lastModified());
                    rowData.addElement(lastModif);

                    break;

                }
            }            
            data.add(rowData.clone());
            rowData.clear();
        }
        //La r�cup�ration des nouvelles  donn�es est termin�e
        model.setDataVector(data, columnNames);
  
    }
    
/**
 * Cr�ee une colonne pour chaque �l�ment de l'attribut columnNames
 *
 */
    public void createColumn() {
        for (int i = 0; i < columnNames.size(); i++)
            model.addColumn(columnNames.get(i));

    }

  

}