package model;

import java.util.Observable;
import java.util.Observer;

/**
 * Un mod�le d'URI qui contient.. une URI !
 * 
 * @author Sted
 */
public class URIModel extends DefaultURIModel implements Observer {

	/** Le supra-mod�le */
	protected FSeekerModel fsm = null;

	/**
	 * Contruit un mod�le d'URI.
	 * 
	 * @param fsm
	 *            un supra-mod�le
	 */
	public URIModel(FSeekerModel fsm) {
		this.fsm = fsm;
		fsm.addObserver(this);
	}

	/**
	 * Retourne le supra-mod�le.
	 * 
	 * @return le supra-mod�le
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	/**
	 * Appel� quand le supra-mod�le a �t� modifi�.
	 */
	public void update(Observable o, Object arg) {
		// On ne prend en compte que les changements d'URI
		if (fsm.isChanged(FSeekerModel.URI))
			fireURIChanged(this, getModel().getURI());
	}

}