/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import misc.file.CompareByType;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

    /** Le supra-modèle */
    protected FSeekerModel fsm = null;

    /** Le comparateur utilisé */
    private Comparator currentComparator = CompareByType.get();

    /** L'ensemble trié contenant les fichiers du répertoire courant */
    protected Set liste = new TreeSet(currentComparator);

    /**
     * Construit un modèle de liste de fichiers.
     * 
     * @param fsm
     *            le supra-modèle
     */
    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
        refreshListFiles();
    }

    /**
     * Retourne le supra-modèle.
     * 
     * @return supra-modèle
     */
    public FSeekerModel getModel() {
        return fsm;
    }

    public void update(Observable o, Object caller) {
        // Le modèle peut s'auto changer, il est à la fois modèle et contrôleur
        // !
        refreshListFiles();
        setChanged();
        notifyObservers(caller);
    }

    /**
     * Met à jour l'attribut <code>liste</code> (contenant les noms de
     * fichiers du répertoire courant).
     */
    protected void refreshListFiles() {
        liste.clear();
        File[] files = fsm.getURI().listFiles();
        if (files != null)
            for (int i = 0; i < files.length; i++)
            	if (!files[i].isHidden() || getModel().showHidden())
            		liste.add(files[i]);
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier courant.
     * 
     * @return nombre de fichiers dans le dossier courant
     */
    public int getSize() {
        return liste.size();
    }

    /**
     * Renvoie l'élément à l'index <code>index</code> dans la liste.
     * 
     * @param index l'index de l'objet désiré
     * @return l'objet à l'index désiré
     */
    public Object getElementAt(int index) {
        return liste.toArray()[index];
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }

}