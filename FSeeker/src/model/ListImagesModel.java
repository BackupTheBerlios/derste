/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * Représente le modèle d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

	/**
	 * L'ensemble trié contenant les fichiers du répertoire courant
	 */
	protected TreeSet filesList = null;

	/**
	 * Construit un modèle de liste de fichiers.
	 * 
	 * @param fsm
	 *            le supra-modèle
	 */
	public ListImagesModel(FSeekerModel fsm) {
		this.fsm = fsm;
		fsm.addObserver(this);

		updateFilesList();
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

		// Si c'est le comparator qui a changé
		if (fsm.isChanged(FSeekerModel.COMPARATOR)) {
			TreeSet newList = new TreeSet(fsm.getComparator());
			newList.addAll(filesList);
			filesList.clear();
			filesList = newList;
		} else { // Si c'est l'uri, le showHidden ou autre..
			updateFilesList();
		}

		setChanged();
		notifyObservers(caller);
	}

	/**
	 * Met à jour l'attribut <code>filesList</code> (contenant les noms de
	 * fichiers du répertoire courant).
	 */
	protected void updateFilesList() {
		if (filesList == null)
			filesList = new TreeSet(fsm.getComparator());
		else
			filesList.clear();

		File[] files = fsm.getURI().listFiles();
		if (files != null)
			for (int i = 0; i < files.length; i++)
				if (!files[i].isHidden() || getModel().showHidden())
					filesList.add(files[i]);
	}

	/**
	 * Renvoie le nombre de fichiers dans le dossier courant.
	 * 
	 * @return nombre de fichiers dans le dossier courant
	 */
	public int getSize() {
		return filesList.size();
	}

	/**
	 * Renvoie l'élément à l'index <code>index</code> dans la filesList.
	 * 
	 * @param index
	 *            l'index de l'objet désiré
	 * @return l'objet à l'index désiré
	 */
	public Object getElementAt(int index) {
		return filesList.toArray()[index];
	}

	public void addListDataListener(ListDataListener l) {
	}

	public void removeListDataListener(ListDataListener l) {
	}

}