/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

    protected FSeekerModel fsm = null;

    protected File parent = null;

    private Comparator currentComparator = new CMP_BY_NAME();
    
    protected Set liste = new TreeSet(currentComparator);

    private class CMP_BY_NAME implements Comparator {
        public int compare(Object o1, Object o2) {
            if (o1 == null)
                return 1;
            if (o2 == null)
                return -1;

            File f1 = (File) o1;
            File f2 = (File) o2;

            if (f1 == parent)
                return -1;
            if (f2 == parent)
                return 1;

            if (f1.isDirectory() && f2.isFile())
                return -1;
            if (f1.isFile() && f2.isDirectory())
                return 1;

            return Collator.getInstance().compare(f1.getName(), f2.getName());
        }
    }

    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        this.parent = fsm.getURI();
        fsm.addObserver(this);
        refreshListFiles();
    }

    public FSeekerModel getModel() {
        return fsm;
    }

    public File getParent() {
        return parent;
    }

    public void setParent(File parent) {
        if (parent == null)
            parent = getModel().getURI();
        this.parent = parent;
    }

    public void update(Observable o, Object caller) {
        // Le modèle peut s'auto changer, il est à la fois modèle et contrôleur
        // !
        setParent(getModel().getURI().getParentFile());
        refreshListFiles();
        setChanged();
        notifyObservers(caller);
    }

    protected void refreshListFiles() {
        liste.clear();
        liste.add(parent);
        File[] files = fsm.getURI().listFiles();
        if (files != null)
            for (int i = 0; i < files.length; i++)
                liste.add(files[i]);
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier.
     */
    public int getSize() {
        return liste.size();
    }

    public Object getElementAt(int index) {
        return liste.toArray()[index];
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }
   
}