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
 * Le supra-mod�le qui contient l'URI courante.
 * 
 * @author Sted
 */
public class FSeekerModel extends Observable {

	/** L'URI en cours de vue */
	protected File uri = null;

	/** Montrer les fichiers cach�s ? */
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
	 * Construit un supra-mod�le avec pour URI de d�part <code>uri</code>
	 * 
	 * @param uri
	 *            URI de d�part
	 */
	public FSeekerModel(File uri) {
		this.uri = uri;
	}

	/**
	 * Change l'URI courante, et pr�vient tous les abonn�s.
	 * 
	 * @param uri
	 *            la nouvelle URI courante
	 * @param src
	 *            la source du changement
	 */
	public void setURI(File uri, Object src) {
		// TODO ou d�j� done ca peut �tre..!
		// le setSelectionPath dans le JTree provoque 2 d�clenchages ici �
		// chaque s�lection autre que sur le JTree (la JList, etc)
		// C'est normal car � chaque changement de s�l, le supramodel change
		// donc le jtree setselectionpath, mais cette m�thode d�clenche
		// l'�v�nement
		// valuechanged qui _refait_ un setURI sur la selection justement (donc
		// la m�me)

		// System.out.println("FSeekerModel.setURI(" + uri.getAbsolutePath() +
		// ")");
		if (!uri.exists()) {
			GU.message("Ce fichier ou r�pertoire n'existe pas.");
			return;
		}
		this.uri = uri;
		setChanged();
		notifyObservers(src);
	}

	/**
	 * Change l'URI courante et pr�vient tous les abonn�s.
	 * 
	 * @param uri
	 *            la nouvelle URI
	 */
	public void setURI(File uri) {
		setURI(uri, null);
	}

	/**
	 * Retourne <code>true</code> s'il faut afficher les fichiers cach�s.
	 * 
	 * @return <code>true</code> s'il faut afficher les fichiers cach�s.
	 */
	public boolean showHidden() {
		return showHidden;
	}

	/**
	 * Change le fait de montrer les fichiers cach�s ou non. Dans le cas d'un
	 * changement, pr�vient tous les abonn�s.
	 * 
	 * @param showHidden
	 *            nouvel �tat du flag
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