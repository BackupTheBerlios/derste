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
	 * Appel� quand le mod�le sera modifi�.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void URIChanged(URIChangedEvent e);
}