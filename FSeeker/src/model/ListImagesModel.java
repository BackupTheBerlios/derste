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

    public FSeekerModel getModel() {
        return fsm;
    }

    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }

    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier + 1 (parent).
     */
    public int getSize() {
        if (fsm.getURI().list() == null) {
            // TODO a résoudre, à cause de l'événement fantome (clic dans le
            // jtree) qui fait n'imp (renvoie un file zarb O_o)
            System.out.println("getSize() : " + fsm.getURI().getAbsolutePath());
            return 1;
        }
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