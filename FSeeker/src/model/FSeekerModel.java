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

		if (!uri.equals(this.uri)) {
			if (!uri.exists()) {
				GU.message("Ce fichier ou r�pertoire n'existe pas.");
				return;
			}
			this.uri = uri;
			filesList = null;

			setChanged(URI);
			notifyObservers(src);
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
			filesList = null;
			setChanged(SHOWHIDDEN);
			notifyObservers();
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
			filesList = null;
			setChanged(COMPARATOR);
			notifyObservers();
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
		getFilesList();
		changed = whatChanged;
		setChanged();
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
		if (filesList == null) {
			File[] files = uri.listFiles();
			filesList = new File[files.length];
			
			if (files != null)
				for (int i = 0, j = 0; i < files.length; i++)
					if (!files[i].isHidden() || showHidden)
						filesList[j++] =  files[i];

			Arrays.sort(filesList, comparator);
		}

		return filesList;
	}

}