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
 * Un modèle par défaut d'URI.
 * 
 * @author sted
 */
public class DefaultURIModel {
	/** Les listeners à prévenir quand l'URI changera */
	protected List listeners = new ArrayList();

	/**
	 * Ajoute un listener sur l'événement de changement d'URI.
	 * 
	 * @param cl
	 *            un listener
	 */
	public void addURIChangedListener(URIChangedListener cl) {
		if (cl != null && !listeners.contains(cl))
			listeners.add(cl);
	}

	/**
	 * Supprime un listener précédemment ajouté.
	 * 
	 * @param cl
	 *            le listener à supprimer
	 */
	public void removeURIChangedListener(URIChangedListener cl) {
		if (cl != null)
			listeners.remove(cl);
	}

	/**
	 * Préviens tous les listeners du changement d'URI.
	 * 
	 * @param source
	 *            le modèle dont l'URI a été changé
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