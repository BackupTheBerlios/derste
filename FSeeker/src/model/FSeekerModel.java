/*
 * Created on 20 oct. 2004
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

	/** Comparateur utilisé pour afficher les fichiers */
	protected Comparator comparator = CompareByType.get();

	/** Les identifiants pour savoir quoi a été modifié */
	public static final int NONE = 0, COMPARATOR = 1, URI = 2, SHOWHIDDEN = 4;

	/** Le truc modifié */
	protected int changed = NONE;

	/** Liste des fichiers du répertoire courant triée */
	protected File[] filesList = null;

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
		if (!uri.equals(this.uri)) {
			if (!uri.exists()) {
				GU.info("Ce fichier ou répertoire n'existe pas.");
				return;
			}
			this.uri = uri;

			setChanged(URI, src);
		}
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
	 * Remonte dans l'arborescence de un niveau.
	 */
	public void gotoParent() {
		File parent = uri.getParentFile();
		if (parent != null)
			setURI(parent);
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
			setChanged(SHOWHIDDEN, null);
		}
	}

	/**
	 * Retourne le comparateur utilisé pour afficher les fichiers.
	 * 
	 * @return le comparateur
	 */
	public Comparator getComparator() {
		return comparator;
	}

	/**
	 * Modifie le comparateur utilisé pour afficher les fichiers, et préviens
	 * tous les abonnés.
	 * 
	 * @param comparator
	 *            nouveau comparateur
	 */
	public void setComparator(Comparator comparator) {
		if (comparator != this.comparator) {
			this.comparator = comparator;
			setChanged(COMPARATOR, null);
		}
	}

	/**
	 * Indique quoi a été modifié, et indique l'état du modèle comme modifié.
	 * 
	 * @param whatChanged
	 *            une constante représentant un composant global défini dans
	 *            FSeekerModel
	 * @param src
	 *            la source du changement
	 */
	protected void setChanged(int whatChanged, Object src) {
		filesList = null;
		changed = whatChanged;
		setChanged();
		notifyObservers(src);
	}

	/**
	 * @return <code>true</code> si ce que représente isChanged a été modifié
	 */
	public boolean isChanged(int isChanged) {
		return (changed & isChanged) > 0;
	}

	/**
	 * Retourne la liste des fichiers triés.
	 * 
	 * @return liste de fichiers du répertoire courant
	 */
	public File[] getFilesList() {
		// Late instanciating
		if (filesList == null) {
			File[] files = uri.listFiles();

			if (files != null) {
				List filesArray = new ArrayList(files.length);

				for (int i = 0, j = 0; i < files.length; i++)
					if (!files[i].isHidden() || showHidden)
						filesArray.add(files[i]);

				filesList = (File []) filesArray.toArray(new File[] {});
				Arrays.sort(filesList, comparator);
			}
		}

		return filesList;
	}

}