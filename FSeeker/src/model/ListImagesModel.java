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
 * Repr�sente le mod�le d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

    /** L'ensemble tri� contenant les fichiers du r�pertoire courant */
    protected File[] filesList = null;

    /** Le supra-mod�le */
    protected FSeekerModel fsm = null;

    /** La liste des listeners */
    protected List listeners = new ArrayList();

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
     * Ajoute un listener.
     * 
     * @param l
     *            listener � ajouter
     */
    public void addListDataListener(ListDataListener l) {
        if (l != null && !listeners.contains(l))
            listeners.add(l);
    }

    /**
     * Renvoie l'�l�ment � l'index <code>index</code> dans la filesList.
     * 
     * @param index
     *            l'index de l'objet d�sir�
     * @return l'objet � l'index d�sir�
     */
    public Object getElementAt(int index) {
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
        return filesList.length;
    }

    /**
     * Supprime un listener.
     * 
     * @param l
     *            listener � supprimer
     */
    public void removeListDataListener(ListDataListener l) {
        if (l != null)
            listeners.remove(l);
    }

    /**
     * Met � jour la liste des fichiers, et pr�vient les vues du changement.
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