/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

/**
 * Repr�sente le mod�le d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends AbstractListModel implements Observer {

	/** L'ensemble tri� contenant les fichiers du r�pertoire courant */
	protected File[] filesList = null;

	/** Le supra-mod�le */
	protected FSeekerModel fsm = null;

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
	 * Renvoie l'�l�ment � l'index <code>index</code> dans la filesList.
	 * 
	 * @param index
	 *            l'index de l'objet d�sir�
	 * @return l'objet � l'index d�sir�
	 */
	public Object getElementAt(int index) {
		if (filesList == null || index > filesList.length)
			return null;
		return filesList[index];
	}

	/**
	 * Retourne le supra-mod�le.
	 * 
	 * @return supra-mod�le
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	/**
	 * Renvoie le nombre de fichiers dans le dossier courant.
	 * 
	 * @return nombre de fichiers dans le dossier courant
	 */
	public int getSize() {
		return (filesList == null ? 0 : filesList.length);
	}

	/**
	 * Met � jour la liste des fichiers, et pr�vient les vues du changement.
	 */
	public void update(Observable o, Object param) {
		int lastSize = getSize();
		filesList = getModel().getFilesList();
		
		// Si l'URI (et donc la s�lection aussi) change
		if (fsm.isChanged(FSeekerModel.URI)) {
			fireContentsChanged(this, 0, getSize() - 1);
		}
		
		// On met car seule la s�lection peut changer aussi
		if (fsm.isChanged(FSeekerModel.SELECTION)) {
			fireSelectionChanged(fsm.getSelection());
		}
	}

	/**
	 * Listeners en attente de l'�v�nement qui modifie la s�lection du mod�le
	 * (issue du supra-mod�le)
	 */
	protected List selectionListeners = new ArrayList();

	/**
	 * Pr�vient les listeners abonn�s que la nouvelle s�lection est le fichier
	 * f.
	 * 
	 * @param f
	 *            nouveau fichier s�lectionn�
	 */
	protected void fireSelectionChanged(File f) {
		Iterator it = selectionListeners.iterator();
		SelectionChangedEvent e = new SelectionChangedEvent(this, f);
		while (it.hasNext())
			((SelectionChangedListener) it.next()).selectionChanged(e);
	}

	/**
	 * Ajoute un listener � l'�coute du changement de s�lection du mod�le.
	 * 
	 * @param l
	 *            un listener
	 */
	public void addSelectionChangedListener(SelectionChangedListener l) {
		if (l != null && !selectionListeners.contains(l))
			selectionListeners.add(l);
	}

	/**
	 * Supprime un listener � l'�coute du changement de s�lection du mod�le.
	 * 
	 * @param l
	 *            un listener
	 */
	public void removeSelectionChangedListener(SelectionChangedListener l) {
		if (l != null)
			selectionListeners.remove(l);
	}

}