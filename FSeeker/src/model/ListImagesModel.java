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
 * Repr�sente le mod�le d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

	/** Le supra-mod�le */
	protected FSeekerModel fsm = null;

	/** L'ensemble tri� contenant les fichiers du r�pertoire courant */
	protected File[] filesList = null;

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
	 * Retourne le supra-mod�le.
	 * 
	 * @return supra-mod�le
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	public void update(Observable o, Object caller) {
		// Le mod�le peut s'auto changer, il est � la fois mod�le et contr�leur
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
	 * Renvoie l'�l�ment � l'index <code>index</code> dans la filesList.
	 * 
	 * @param index
	 *            l'index de l'objet d�sir�
	 * @return l'objet � l'index d�sir�
	 */
	public Object getElementAt(int index) {
		return filesList[index];
	}
	

	public void addListDataListener(ListDataListener l) {
	}

	public void removeListDataListener(ListDataListener l) {
	}

}