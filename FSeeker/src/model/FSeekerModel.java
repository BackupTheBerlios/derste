/*
 * Created on 20 oct. 2004
 */
package model;

import java.io.File;
import java.util.Comparator;
import java.util.Observable;

import misc.GU;
import misc.file.CompareByType;

/**
 * Le supra-modèle qui contient l'URI courante.
 * 
 * @author Sted
 */
public class FSeekerModel extends Observable {

	/** L'URI en cours de vue */
	protected File uri = null;

	/** Montrer les fichiers cachés ? */
	protected boolean showHidden = true;
	
	protected Comparator comparator = CompareByType.get();

	/**
	 * Retourne l'URI courante.
	 * 
	 * @return URI courante
	 */
	public File getURI() {
		return uri;
	}

	/**
	 * Construit un supra-modèle avec pour URI de départ <code>uri</code>
	 * 
	 * @param uri
	 *            URI de départ
	 */
	public FSeekerModel(File uri) {
		this.uri = uri;
	}

	/**
	 * Change l'URI courante, et prévient tous les abonnés.
	 * 
	 * @param uri
	 *            la nouvelle URI courante
	 * @param src
	 *            la source du changement
	 */
	public void setURI(File uri, Object src) {
		// TODO ou déjà done ca peut être..!
		// le setSelectionPath dans le JTree provoque 2 déclenchages ici à
		// chaque sélection autre que sur le JTree (la JList, etc)
		// C'est normal car à chaque changement de sél, le supramodel change
		// donc le jtree setselectionpath, mais cette méthode déclenche
		// l'événement
		// valuechanged qui _refait_ un setURI sur la selection justement (donc
		// la même)

		// System.out.println("FSeekerModel.setURI(" + uri.getAbsolutePath() +
		// ")");
		if (!uri.exists()) {
			GU.message("Ce fichier ou répertoire n'existe pas.");
			return;
		}
		this.uri = uri;
		setChanged();
		notifyObservers(src);
	}

	/**
	 * Change l'URI courante et prévient tous les abonnés.
	 * 
	 * @param uri
	 *            la nouvelle URI
	 */
	public void setURI(File uri) {
		setURI(uri, null);
	}

	/**
	 * Retourne <code>true</code> s'il faut afficher les fichiers cachés.
	 * 
	 * @return <code>true</code> s'il faut afficher les fichiers cachés.
	 */
	public boolean showHidden() {
		return showHidden;
	}

	/**
	 * Change le fait de montrer les fichiers cachés ou non. Dans le cas d'un
	 * changement, prévient tous les abonnés.
	 * 
	 * @param showHidden
	 *            nouvel état du flag
	 */
	public void showHidden(boolean showHidden) {
		if (this.showHidden != showHidden) {
			this.showHidden = showHidden;
			setChanged();
			notifyObservers();
		}
	}
	
	public Comparator getComparator() {
		return comparator;
	}
	
	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
		setChanged();
		notifyObservers();
	}

}