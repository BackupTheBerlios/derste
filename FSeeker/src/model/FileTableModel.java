/**
 * 
 * Classe qui permet la création d'un modéle de systéme de fichier pour
 * la représentation sous forme d'une vue en table (vue détails)
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
        //on initialise les données
        columnNames = new Vector(0);
        columnNames.addElement("Nom du fichier");
        columnNames.addElement("Taille");
        columnNames.addElement("Type");
        columnNames.addElement("Date de Modif");
        //On crée les colonnes
        createColumn();
        setData();
        
    }

    /**
     * @return Le Méta-model
     */
    public FSeekerModel getModel() {
        return fsm;
    }

    /**
     * Renvoie le modéle de table crée
     * 
     * @return
     */
    public TableModel getTableModel() {
        return model;
    }

    public void update(Observable o, Object arg) {
        System.out.println("Le modéle principal a changé");

        setData();//Mise à jour des données
        setChanged();//Le modèle a changé
        notifyObservers();//On prévient tout les listeners
    }

    /**
     * Récupére la nouvelle URI du méta-modéle et actualise les valeurs
     *  
     */
    public void setData() {
        File [] files = fsm.getURI().listFiles();

        int nbColumns = columnNames.size();
        int nbRows = files.length;

        Vector data = new Vector(0);
        //On récupére colonne par colonne pour chaque ligne 
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
        //La récupération des nouvelles  données est terminée
        model.setDataVector(data, columnNames);
  
    }
    
/**
 * Créee une colonne pour chaque élément de l'attribut columnNames
 *
 */
    public void createColumn() {
        for (int i = 0; i < columnNames.size(); i++)
            model.addColumn(columnNames.get(i));

    }

  

}