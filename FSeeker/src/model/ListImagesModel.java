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
 * Représente le modèle d'une liste de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends AbstractListModel implements Observer {

	/** L'ensemble trié contenant les fichiers du répertoire courant */
	protected File[] filesList = null;

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

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
	 * Renvoie l'élément à l'index <code>index</code> dans la filesList.
	 * 
	 * @param index
	 *            l'index de l'objet désiré
	 * @return l'objet à l'index désiré
	 */
	public Object getElementAt(int index) {
		if (filesList == null || index > filesList.length)
			return null;
		return filesList[index];
	}

	/**
	 * Retourne le supra-modèle.
	 * 
	 * @return supra-modèle
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
	 * Met à jour la liste des fichiers, et prévient les vues du changement.
	 */
	public void update(Observable o, Object param) {
		int lastSize = getSize();
		filesList = getModel().getFilesList();
		
		// Si l'URI (et donc la sélection aussi) change
		if (fsm.isChanged(FSeekerModel.URI)) {
			fireContentsChanged(this, 0, getSize() - 1);
		}
		
		// On met car seule la sélection peut changer aussi
		if (fsm.isChanged(FSeekerModel.SELECTION)) {
			fireSelectionChanged(fsm.getSelection());
		}
	}

	/**
	 * Listeners en attente de l'événement qui modifie la sélection du modèle
	 * (issue du supra-modèle)
	 */
	protected List selectionListeners = new ArrayList();

	/**
	 * Prévient les listeners abonnés que la nouvelle sélection est le fichier
	 * f.
	 * 
	 * @param f
	 *            nouveau fichier sélectionné
	 */
	protected void fireSelectionChanged(File f) {
		Iterator it = selectionListeners.iterator();
		SelectionChangedEvent e = new SelectionChangedEvent(this, f);
		while (it.hasNext())
			((SelectionChangedListener) it.next()).selectionChanged(e);
	}

	/**
	 * Ajoute un listener à l'écoute du changement de sélection du modèle.
	 * 
	 * @param l
	 *            un listener
	 */
	public void addSelectionChangedListener(SelectionChangedListener l) {
		if (l != null && !selectionListeners.contains(l))
			selectionListeners.add(l);
	}

	/**
	 * Supprime un listener à l'écoute du changement de sélection du modèle.
	 * 
	 * @param l
	 *            un listener
	 */
	public void removeSelectionChangedListener(SelectionChangedListener l) {
		if (l != null)
			selectionListeners.remove(l);
	}

}