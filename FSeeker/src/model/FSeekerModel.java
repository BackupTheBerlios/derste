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
import java.util.prefs.Preferences;

import misc.GU;
import misc.file.CompareByLastModified;
import misc.file.CompareByName;
import misc.file.CompareBySize;
import misc.file.CompareByType;

/**
 * Le supra-mod�le qui contient l'URI courante.
 * 
 * @author Sted
 */
public class FSeekerModel extends Observable {

	/** Les identifiants pour savoir quoi a �t� modifi� */
	public static final int NONE = 0, COMPARATOR = 1, URI = 2, SHOWHIDDEN = 4,
			SELECTION = 8;

	protected static Preferences pref = Preferences.userRoot();

	/** Le truc modifi� */
	protected int changed = NONE;

	/** Comparateur utilis� pour afficher les fichiers */
	protected Comparator comparator = CompareByType.get();

	/** Liste des fichiers du r�pertoire courant tri�e */
	protected File[] filesList = null;

	/** Nombre de clics pour ouvrir un dossier / fichier */
	protected int nbClick = 2;

	/** Fichier en s�lection */
	protected File selection = null;

	protected File uri = null;

	/** Montrer les fichiers cach�s ? */
	protected boolean showHidden = true;

	/** La liste des URIs visit�es (pr�c�dent/suivant) */
	protected Navigation uris = null;

	/**
	 * Classe interne g�rant la navigation dans les uris. (pr�c�dent/suivant)
	 */
	private class Navigation {
		/** L� o� nous sommes dans la liste des uris */
		protected int jeton = 0;

		/** La liste des uris */
		protected List liste = new ArrayList();

		/**
		 * Construit l'objet avec un fichier de d�part.
		 * 
		 * @param start
		 *            fichier de d�part
		 */
		public Navigation(File start) {
			liste.add(0, start);
		}

		/**
		 * Se rend � l'uri pr�c�dente et la renvoie.
		 * 
		 * @return uri pr�c�dente
		 */
		public File gotoPrevious() {
			if (jeton > 0)
				return (File) liste.get(--jeton);
			return getCurrent();
		}

		/**
		 * Se rend � l'uri suivante et la renvoie.
		 * 
		 * @return uri suivante
		 */
		public File gotoNext() {
			if (jeton + 1 < liste.size())
				return (File) liste.get(++jeton);
			return getCurrent();
		}

		/**
		 * Retourne l'uri courante.
		 * 
		 * @return uri courante
		 */
		public File getCurrent() {
			return (File) liste.get(jeton);
		}

		/**
		 * Modifie l'uri actuelle comme �tant l'uri de f.
		 * 
		 * @param f
		 *            un fichier
		 * @return le fichier ajout�
		 */
		public File add(File f) {
			if (getCurrent().equals(f))
				return f;

			if (jeton + 1 < liste.size()) {
				liste.set(++jeton, f);
				for (int i = jeton + 1; i < liste.size(); )
					liste.remove(i);
			} else
				liste.add(++jeton, f);

			return f;
		}

		public String toString() {
			return liste.toString() + " @ " + jeton;
		}

	}

	/**
	 * Construit un supra-mod�le avec pour URI de d�part <code>uri</code>
	 * 
	 * @param uri
	 *            URI de d�part
	 */
	public FSeekerModel(File uri) {
		uris = new Navigation(uri);
		this.uri = uri;
		this.selection = uri;

		// Les pr�f�rences
		showHidden = pref.getBoolean("showHidden", true);
		String c = pref.get("comparator", "type");
		if (c.equals("type"))
			comparator = CompareByType.get();
		else if (c.equals("size"))
			comparator = CompareBySize.get();
		else if (c.equals("lastmodified"))
			comparator = CompareByLastModified.get();
		else
			comparator = CompareByName.get();
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

	/**
	 * Retourne le comparateur utilis� pour afficher les fichiers.
	 * 
	 * @return le comparateur
	 */
	public Comparator getComparator() {
		return comparator;
	}

	/**
	 * Retourne la liste des fichiers tri�s.
	 * 
	 * @return liste de fichiers du r�pertoire courant
	 */
	public File[] getFilesList() {
		// Late instanciating
		if (filesList == null) {
			File[] files = getURI().listFiles();

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
	 * Retourne la s�lection courante.
	 * 
	 * @return la s�lection
	 */
	public File getSelection() {
		return selection;
	}

	/**
	 * Retourne l'URI courante.
	 * 
	 * @return URI courante
	 */
	public File getURI() {
		return uri;
	}

	/**
	 * Remonte dans l'arborescence de un niveau.
	 */
	public void gotoParent() {
		File parent = getURI().getParentFile();
		if (parent != null)
			setURI(parent);
	}

	public void gotoPrevious() {
		setURI(uris.gotoPrevious());
	}

	public void gotoNext() {
		setURI(uris.gotoNext());
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
	 * Change l'URI courante et pr�vient tous les abonn�s.
	 * 
	 * @param uri
	 *            la nouvelle URI
	 */
	public void setURI(File uri) {
		setURI(uri, null);
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
		if (!uri.equals(getURI())) {
			if (!uri.exists()) {
				GU.info("Ce fichier ou r�pertoire n'existe pas.");
				return;
			}

			this.uri = uri;
			uris.add(uri);
			System.out.println(uris);
			setChanged(URI, src);

			// Si on change l'URI, forc�ment, la s�lection change.. !
			setSelection(uri, src);
		}
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
	 * Indique que le mod�le a chang� sans rien modifi�. Utiliser pour une mise
	 * � jour dans un m�me dossier par exemple.
	 */
	public void update() {
		setChanged(URI, getSelection());
	}

}