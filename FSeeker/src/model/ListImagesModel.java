/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Représente le modèle d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

    /** L'ensemble trié contenant les fichiers du répertoire courant */
    protected File[] filesList = null;

    /** Le supra-modèle */
    protected FSeekerModel fsm = null;

    /** La liste des listeners */
    protected List listeners = new ArrayList();

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
     * Ajoute un listener.
     * 
     * @param l
     *            listener à ajouter
     */
    public void addListDataListener(ListDataListener l) {
        if (l != null && !listeners.contains(l))
            listeners.add(l);
    }

    /**
     * Renvoie l'élément à l'index <code>index</code> dans la filesList.
     * 
     * @param index
     *            l'index de l'objet désiré
     * @return l'objet à l'index désiré
     */
    public Object getElementAt(int index) {
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
        return filesList.length;
    }

    /**
     * Supprime un listener.
     * 
     * @param l
     *            listener à supprimer
     */
    public void removeListDataListener(ListDataListener l) {
        if (l != null)
            listeners.remove(l);
    }

    /**
     * Met à jour la liste des fichiers, et prévient les vues du changement.
     */
    public void update(Observable o, Object caller) {
        filesList = getModel().getFilesList();
        Iterator it = listeners.iterator();

        while (it.hasNext())
            ((ListDataListener) it.next()).contentsChanged(new ListDataEvent(
                    this, ListDataEvent.CONTENTS_CHANGED, 0, getSize() - 1));

        setChanged();
        notifyObservers(caller);
    }

}