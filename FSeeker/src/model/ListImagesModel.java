/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

    protected FSeekerModel fsm = null;

    protected File parent = null;

    public FSeekerModel getModel() {
        return fsm;
    }

    public File getParent() {
        return parent;
    }

    public void setParent(File parent) {
        this.parent = parent;
    }

    public void update(Observable o, Object caller) {
        // Le modèle peut s'auto changer, il est à la fois modèle et contrôleur
        // !
        setParent(getModel().getURI().getParentFile());
        setChanged();
        notifyObservers(caller);
    }

    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        this.parent = fsm.getURI();
        fsm.addObserver(this);
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier + 1 (parent).
     */
    public int getSize() {
        // Le null ne devrait jamais arriver, mais ça l'était avec un bug, donc
        // laissé..
        if (fsm.getURI().list() == null)
            return 1;
        return fsm.getURI().list().length + 1;
    }

    public Object getElementAt(int index) {
        if (index == 0) {
            if (fsm.getURI().getParentFile() == null)
                return fsm.getURI();
            return fsm.getURI().getParentFile();
        }
        File[] files = fsm.getURI().listFiles();
        return files[index - 1];
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }
}