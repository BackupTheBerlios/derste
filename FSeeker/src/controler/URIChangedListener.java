/*
 * Created on 31 oct. 2004
 */
package controler;

import java.util.EventListener;

/**
 * Interface d'un listener sur un changement d'URI.
 * 
 * @author sted
 */
public interface URIChangedListener extends EventListener {

	/**
	 * Appelé quand le modèle sera modifié.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void URIChanged(URIChangedEvent e);
}