package model;

import java.util.Observable;
import java.util.Observer;

/**
 * Un modèle d'URI qui contient.. une URI !
 * 
 * @author Sted
 */
public class URIModel extends DefaultURIModel implements Observer {

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

	/**
	 * Contruit un modèle d'URI.
	 * 
	 * @param fsm
	 *            un supra-modèle
	 */
	public URIModel(FSeekerModel fsm) {
		this.fsm = fsm;
		fsm.addObserver(this);
	}

	/**
	 * Retourne le supra-modèle.
	 * 
	 * @return le supra-modèle
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	/**
	 * Appelé quand le supra-modèle a été modifié.
	 */
	public void update(Observable o, Object arg) {
		// On ne prend en compte que les changements d'URI
		if (fsm.isChanged(FSeekerModel.URI))
			fireURIChanged(this, getModel().getURI());
	}

}