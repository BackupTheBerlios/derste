/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author brahim
 * @author Sted
 */
public class ListImagesModel implements ListModel {

    private File dir;

    private ArrayList listeners;

    public ListImagesModel(File dir) {
        this.dir = dir;
        listeners = new ArrayList(0);
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier
     */
    public int getSize() {
        return dir.list().length;
    }

    /**
     * the file at the specified index in the directory
     */
    public Object getElementAt(int index) {
        File[] files = dir.listFiles();
        return files[index];
    }

    public void addListDataListener(ListDataListener l) {
        if (!listeners.contains(l) && l != null)
            listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        if (l != null)
            listeners.remove(l);
    }

    public void setDirectory(File dir) {
        this.dir = dir;
        fire(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0,
                getSize() - 1));
    }

    //	Indique à tous les écouteurs, qu'il y a eut un changement
    private void fire(ListDataEvent l) {
        Iterator it = listeners.iterator();
        while (it.hasNext())
            ((ListDataListener) it.next()).contentsChanged(l);
    }
}