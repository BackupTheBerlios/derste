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
	public static final int NONE = 0, COMPARATOR = 1, URI = 2, SHOWHIDDEN = 4,
			SELECTION = 8;

	/** Le truc modifi� */
	protected int changed = NONE;

	/** Liste des fichiers du r�pertoire courant tri�e */
	protected File[] filesList = null;

	/** Nombre de clics pour ouvrir un dossier / fichier */
	protected int nbClick = 2;

	/** Fichier en s�lection */
	protected File selection = null;

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
		this.selection = uri;
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
		if (!uri.equals(this.uri)) {
			if (!uri.exists()) {
				GU.info("Ce fichier ou r�pertoire n'existe pas.");
				return;
			}
			this.uri = uri;

			setChanged(URI, src);

			// Si on change l'URI, forc�ment, la s�lection change.. !
			setSelection(uri, src);
		}
	}

	/**
	 * Change l'URI courante et pr�vient tous les abonn�s.
	 * 
	 * @param uri
	 *            la nouvelle URI
	 */
	public void setURI(File uri) {
		setURI(uri, getSelection());
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
			setChanged(SHOWHIDDEN, null);
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
			setChanged(COMPARATOR, null);
		}
	}

	/**
	 * Indique quoi a �t� modifi�, et indique l'�tat du mod�le comme modifi�.
	 * 
	 * @param whatChanged
	 *            une constante repr�sentant un composant global d�fini dans
	 *            FSeekerModel
	 * @param src
	 *            la source du changement
	 */
	protected void setChanged(int whatChanged, Object param) {
		// Seul la s�lection ne modifie pas la liste des fichiers
		if (whatChanged != SELECTION)
			filesList = null;
		changed = whatChanged;
		setChanged();
		notifyObservers(param);
	}

	/**
	 * Indique que le mod�le a chang� sans rien modifi�. Utiliser pour une mise
	 * � jour dans un m�me dossier par exemple.
	 */
	public void update() {
		setChanged(URI, getSelection());
	}

	/**
	 * Permet de modifier le fichier en cours de s�lection
	 * 
	 * @param f
	 *            fichier s�lectionn�
	 */
	public void setSelection(File f) {
		setSelection(f, null);
	}

	/**
	 * Permet de modifier le fichier en cours de s�lection
	 * 
	 * @param f
	 *            fichier s�lectionn�
	 * @param src
	 *            la source du changement
	 */
	public void setSelection(File f, Object src) {
		selection = f;
		setChanged(SELECTION, src);
	}
	
	/**
	 * Retourne la s�lection courante.
	 * 
	 * @return la s�lection
	 */
	public File getSelection() {
		return selection;
	}

	/**
	 * Indique si un �tat (URI, SELECTION etc) a �t� modifi�. Possibilit� de
	 * combiner les valeurs, ie : URI | SELECTION par exemple.
	 * 
	 * @return <code>true</code> si ce que repr�sente isChanged a �t� modifi�
	 */
	public boolean isChanged(int isChanged) {
		return (changed & isChanged) > 0;
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
				List filesArray = new ArrayList(files.length);

				for (int i = 0, j = 0; i < files.length; i++)
					if (!files[i].isHidden() || showHidden)
						filesArray.add(files[i]);

				filesList = (File[]) filesArray.toArray(new File[] {});
				Arrays.sort(filesList, comparator);
			}
		}

		return filesList;
	}

	/**
	 * Retourne le nombre de clics n�cessaire pour ouvrir un r�pertoire ou un
	 * fichier. Normalement, 1 ou 2.
	 * 
	 * @return ce nombre
	 */
	public int getClickCount() {
		return nbClick;
	}

}