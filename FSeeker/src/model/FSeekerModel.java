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
 * Le supra-modèle qui contient l'URI courante.
 * 
 * @author Sted
 */
public class FSeekerModel extends Observable {

	/** Les identifiants pour savoir quoi a été modifié */
	public static final int NONE = 0, COMPARATOR = 1, URI = 2, SHOWHIDDEN = 4,
			SELECTION = 8;

	protected static Preferences pref = Preferences.userRoot();

	/** Le truc modifié */
	protected int changed = NONE;

	/** Comparateur utilisé pour afficher les fichiers */
	protected Comparator comparator = CompareByType.get();

	/** Liste des fichiers du répertoire courant triée */
	protected File[] filesList = null;

	/** Nombre de clics pour ouvrir un dossier / fichier */
	protected int nbClick = 2;

	/** Fichier en sélection */
	protected File selection = null;

	protected File uri = null;

	/** Montrer les fichiers cachés ? */
	protected boolean showHidden = true;

	/** La liste des URIs visitées (précédent/suivant) */
	protected Navigation uris = null;

	/**
	 * Classe interne gérant la navigation dans les uris. (précédent/suivant)
	 */
	private class Navigation {
		/** Là où nous sommes dans la liste des uris */
		protected int jeton = 0;

		/** La liste des uris */
		protected List liste = new ArrayList();

		/**
		 * Construit l'objet avec un fichier de départ.
		 * 
		 * @param start
		 *            fichier de départ
		 */
		public Navigation(File start) {
			liste.add(0, start);
		}

		/**
		 * Se rend à l'uri précédente et la renvoie.
		 * 
		 * @return uri précédente
		 */
		public File gotoPrevious() {
			if (jeton > 0)
				return (File) liste.get(--jeton);
			return getCurrent();
		}

		/**
		 * Se rend à l'uri suivante et la renvoie.
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
		 * Modifie l'uri actuelle comme étant l'uri de f.
		 * 
		 * @param f
		 *            un fichier
		 * @return le fichier ajouté
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
	 * Construit un supra-modèle avec pour URI de départ <code>uri</code>
	 * 
	 * @param uri
	 *            URI de départ
	 */
	public FSeekerModel(File uri) {
		uris = new Navigation(uri);
		this.uri = uri;
		this.selection = uri;

		// Les préférences
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
	 * Retourne le nombre de clics nécessaire pour ouvrir un répertoire ou un
	 * fichier. Normalement, 1 ou 2.
	 * 
	 * @return ce nombre
	 */
	public int getClickCount() {
		return nbClick;
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
	 * Retourne la liste des fichiers triés.
	 * 
	 * @return liste de fichiers du répertoire courant
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
	 * Retourne la sélection courante.
	 * 
	 * @return la sélection
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
	 * Indique si un état (URI, SELECTION etc) a été modifié. Possibilité de
	 * combiner les valeurs, ie : URI | SELECTION par exemple.
	 * 
	 * @return <code>true</code> si ce que représente isChanged a été modifié
	 */
	public boolean isChanged(int isChanged) {
		return (changed & isChanged) > 0;
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
	protected void setChanged(int whatChanged, Object param) {
		// Seul la sélection ne modifie pas la liste des fichiers
		if (whatChanged != SELECTION)
			filesList = null;
		changed = whatChanged;
		setChanged();
		notifyObservers(param);
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
	 * Permet de modifier le fichier en cours de sélection
	 * 
	 * @param f
	 *            fichier sélectionné
	 */
	public void setSelection(File f) {
		setSelection(f, null);
	}

	/**
	 * Permet de modifier le fichier en cours de sélection
	 * 
	 * @param f
	 *            fichier sélectionné
	 * @param src
	 *            la source du changement
	 */
	public void setSelection(File f, Object src) {
		selection = f;
		setChanged(SELECTION, src);
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
	 * Change l'URI courante, et prévient tous les abonnés.
	 * 
	 * @param uri
	 *            la nouvelle URI courante
	 * @param src
	 *            la source du changement
	 */
	public void setURI(File uri, Object src) {
		if (!uri.equals(getURI())) {
			if (!uri.exists()) {
				GU.info("Ce fichier ou répertoire n'existe pas.");
				return;
			}

			this.uri = uri;
			uris.add(uri);
			System.out.println(uris);
			setChanged(URI, src);

			// Si on change l'URI, forcément, la sélection change.. !
			setSelection(uri, src);
		}
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
	 * Indique que le modèle a changé sans rien modifié. Utiliser pour une mise
	 * à jour dans un même dossier par exemple.
	 */
	public void update() {
		setChanged(URI, getSelection());
	}

}