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
 * Représente le modèle d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

	/** L'ensemble trié contenant les fichiers du répertoire courant */
	protected File[] filesList = null;

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
	 * Retourne le supra-modèle.
	 * 
	 * @return supra-modèle
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	public void update(Observable o, Object caller) {
		// Le modèle peut s'auto changer, il est à la fois modèle et contrôleur
		filesList = getModel().getFilesList();
		setChanged();
		notifyObservers(caller);
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
	 * Renvoie l'élément à l'index <code>index</code> dans la filesList.
	 * 
	 * @param index
	 *            l'index de l'objet désiré
	 * @return l'objet à l'index désiré
	 */
	public Object getElementAt(int index) {
		return filesList[index];
	}
	

	public void addListDataListener(ListDataListener l) {
	}

	public void removeListDataListener(ListDataListener l) {
	}

}