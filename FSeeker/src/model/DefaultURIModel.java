/*
 * Created on 31 oct. 2004
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import controler.URIChangedEvent;
import controler.URIChangedListener;

/**
 * Un mod�le par d�faut d'URI.
 * 
 * @author sted
 */
public class DefaultURIModel {
	/** Les listeners � pr�venir quand l'URI changera */
	protected List listeners = new ArrayList();

	/**
	 * Ajoute un listener sur l'�v�nement de changement d'URI.
	 * 
	 * @param cl
	 *            un listener
	 */
	public void addURIChangedListener(URIChangedListener cl) {
		if (cl != null && !listeners.contains(cl))
			listeners.add(cl);
	}

	/**
	 * Supprime un listener pr�c�demment ajout�.
	 * 
	 * @param cl
	 *            le listener � supprimer
	 */
	public void removeURIChangedListener(URIChangedListener cl) {
		if (cl != null)
			listeners.remove(cl);
	}

	/**
	 * Pr�viens tous les listeners du changement d'URI.
	 * 
	 * @param source
	 *            le mod�le dont l'URI a �t� chang�
	 * @param newURI
	 *            la nouvelle URI
	 */
	protected void fireURIChanged(Object source, File newURI) {
		URIChangedEvent e = new URIChangedEvent(source, newURI);
		Iterator it = listeners.iterator();
		while (it.hasNext())
			((URIChangedListener) it.next()).URIChanged(e);
	}
}