/*
 * Created on 20 oct. 2004
 */
package model;

import java.io.File;
import java.util.Arrays;
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

	/** Comparateur utilis� pour afficher les fichiers */
	protected Comparator comparator = CompareByType.get();

	/** Les identifiants pour savoir quoi a �t� modifi� */
	public static final int NONE = 0, COMPARATOR = 1, URI = 2, SHOWHIDDEN = 3;

	/** Le truc modifi� */
	protected int changed = NONE;

	/** Liste des fichiers du r�pertoire courant tri�e */
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
		// TODO virer le src (voir qui l'utilise, car peut �tre devenu inutile)
		if (!uri.equals(this.uri)) {
			if (!uri.exists()) {
				GU.message("Ce fichier ou r�pertoire n'existe pas.");
				return;
			}
			this.uri = uri;

			setChanged(URI);
		}
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
	 * Remonte dans l'arborescence de un niveau.
	 */
	public void gotoParent() {
		File parent = uri.getParentFile();
		if (parent != null)
			setURI(parent);
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
			setChanged(SHOWHIDDEN);
		}
	}

	/**
	 * Retourne le comparateur utilis� pour afficher les fichiers.
	 * 
	 * @return le comparateur
	 */
	public Comparator getComparator() {
		return comparator;
	}

	/**
	 * Modifie le comparateur utilis� pour afficher les fichiers, et pr�viens
	 * tous les abonn�s.
	 * 
	 * @param comparator
	 *            nouveau comparateur
	 */
	public void setComparator(Comparator comparator) {
		if (comparator != this.comparator) {
			this.comparator = comparator;
			setChanged(COMPARATOR);
		}
	}

	/**
	 * Indique quoi a �t� modifi�, et indique l'�tat du mod�le comme modifi�.
	 * 
	 * @param whatChanged
	 *            une constante repr�sentant un composant global d�fini dans
	 *            FSeekerModel
	 */
	protected void setChanged(int whatChanged) {
		filesList = null;
		changed = whatChanged;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return <code>true</code> si ce que repr�sente isChanged a �t� modifi�
	 */
	public boolean isChanged(int isChanged) {
		return changed == isChanged;
	}

	/**
	 * Retourne la liste des fichiers tri�s.
	 * 
	 * @return liste de fichiers du r�pertoire courant
	 */
	public File[] getFilesList() {
		// Late instanciating
		if (filesList == null) {
			File[] files = uri.listFiles();
			
			if (files != null) {
				filesList = new File[files.length];
			
				for (int i = 0, j = 0; i < files.length; i++)
					if (!files[i].isHidden() || showHidden)
						filesList[j++] = files[i];

				Arrays.sort(filesList, comparator);
			}
		}

		return filesList;
	}

}