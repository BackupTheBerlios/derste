/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

/**
 * Représente le modèle d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends AbstractListModel implements Observer {

    /** L'ensemble trié contenant les fichiers du répertoire courant */
    protected File[] filesList = null;

    /** Le supra-modèle */
    protected FSeekerModel fsm = null;

    /**
     * Construit un modèle de liste de fichiers.
     * 
     * @param fsm
     *            le supra-modèle
     */
    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
        filesList = getModel().getFilesList();
    }


    /**
     * Renvoie l'élément à l'index <code>index</code> dans la filesList.
     * 
     * @param index
     *            l'index de l'objet désiré
     * @return l'objet à l'index désiré
     */
    public Object getElementAt(int index) {
    	if (filesList == null)
    		return null;
        return filesList[index];
    }

    /**
     * Retourne le supra-modèle.
     * 
     * @return supra-modèle
     */
    public FSeekerModel getModel() {
        return fsm;
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier courant.
     * 
     * @return nombre de fichiers dans le dossier courant
     */
    public int getSize() {
        return (filesList == null ? 0 : filesList.length);
    }

    /**
     * Met à jour la liste des fichiers, et prévient les vues du changement.
     */
    public void update(Observable o, Object caller) {
        int lastSize = getSize();
    	filesList = getModel().getFilesList();
        fireContentsChanged(this, 0, (lastSize > 0 ? lastSize - 1 : 0));
    }

}