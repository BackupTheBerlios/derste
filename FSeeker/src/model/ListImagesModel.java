/*
 * Created on 16 oct. 2004
 * 
 * TODO: euh, changes tes noms de méthodes ("changement" et "fire".... no comment) (anglais !!)
 * et clarifie le tout, c'est franchement le boxon, j'ai mis un temps
 * avant de comprendre son utilité. Et javadoc quoi ! 
 * 
 * Et PS lollant :
 * private void fire(ListDataListener l, ListDataEvent lde) {
 *	l.contentsChanged(lde);
 *	}
 * Pourquoi une méthode pour ça ? Sachant que tu l'appelle une seule fois. :D
 * A part rendre encore moins incompréhensible, je vois pas pourquoi.
 */
package model;

import javax.swing.ListModel;
import javax.swing.event.*;
import java.io.File;
import java.util.*;

/**
 * @author brahim
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

	public void setDir(File dir) {
		this.dir = dir;
		changement();

	}

	public void removeListDataListener(ListDataListener l) {
		if (l != null)
			listeners.remove(l);
	}

	//Indique à un écouteur, qu'il y a eut un changement
	private void fire(ListDataListener l, ListDataEvent lde) {
		l.contentsChanged(lde);
	}

	//	Indique à tous les écouteurs, qu'il y a eut un changement
	private void fire(ListDataEvent l) {
		int nb = listeners.size();
		for (int i = 0; i < nb; i++)
			fire((ListDataListener) listeners.get(i), l);
	}

	//	A appelée pour signaler les changements
	private void changement() {
		fire(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0,
				getSize() - 1));
	}

}