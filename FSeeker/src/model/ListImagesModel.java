/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

/**
 * Repr�sente le mod�le d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends AbstractListModel implements Observer {

    /** L'ensemble tri� contenant les fichiers du r�pertoire courant */
    protected File[] filesList = null;

    /** Le supra-mod�le */
    protected FSeekerModel fsm = null;

    /**
     * Construit un mod�le de liste de fichiers.
     * 
     * @param fsm
     *            le supra-mod�le
     */
    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
        filesList = getModel().getFilesList();
    }


    /**
     * Renvoie l'�l�ment � l'index <code>index</code> dans la filesList.
     * 
     * @param index
     *            l'index de l'objet d�sir�
     * @return l'objet � l'index d�sir�
     */
    public Object getElementAt(int index) {
    	if (filesList == null)
    		return null;
        return filesList[index];
    }

    /**
     * Retourne le supra-mod�le.
     * 
     * @return supra-mod�le
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
     * Met � jour la liste des fichiers, et pr�vient les vues du changement.
     */
    public void update(Observable o, Object caller) {
        int lastSize = getSize();
    	filesList = getModel().getFilesList();
        fireContentsChanged(this, 0, (lastSize > 0 ? lastSize - 1 : 0));
    }

}