/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author brahim
 * @author Sted
 */
public class ListImagesModel extends Observable implements ListModel {

	private File dir = null;

	private ArrayList listeners = new ArrayList();

	public ListImagesModel(File dir) {
		this.dir = dir;
	}

	/**
	 * Renvoie le nombre de fichiers dans le dossier
	 */
	public int getSize() {
		return dir.list().length + 1;
	}

	/**
	 * the file at the specified index in the directory
	 */
	public Object getElementAt(int index) {
		if (index == 0) {
			if (dir.getParentFile() == null)
				return dir;
			return dir.getParentFile();
		}
		File[] files = dir.listFiles();
		return files[index-1];
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
		setChanged();
		notifyObservers();
	}

	//	Indique à tous les écouteurs, qu'il y a eut un changement
	private void fire(ListDataEvent l) {
		Iterator it = listeners.iterator();
		while (it.hasNext())
			((ListDataListener) it.next()).contentsChanged(l);
	}
}