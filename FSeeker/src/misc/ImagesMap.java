/*
 * Created on 18 oct. 2004
 */
package misc;

import java.io.File;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Classe permettant de gérer le chargement des images de FSeeker.
 * 
 * @author Sted
 * @author aitelhab
 */
public class ImagesMap {
	/**
	 * Le tableau associatif contenant les images. <br>
	 * [String clef] => [Icon ressource]
	 */
	protected static final HashMap images = new HashMap();

	/** Le séparateur de fichier */
	private static final char SEP = File.separatorChar;

	/** Là où sont stockées toutes les images */
	private static final String 
		IMAGES = "images" + SEP, 
		EXTENSIONS = "extensions" + SEP;
	
	/** Les images 'spéciales' */
	private static final String	
		DEFAULT_IMAGE = EXTENSIONS + "default.png",
		DIRECTORY_IMAGE = EXTENSIONS + "directory.png";

	/**
	 * Renvoie un objet Icon à partir du chemin de image. Si le chemin n'est pas
	 * valide ou que l'image n'existe pas, renvoie null.
	 * 
	 * @param image
	 *            le chemin de l'image (ie: "dot.gif");
	 * @return la ressource Icon
	 */
	public static Icon get(String image) {
		// Si on l'a déjà chargée, on renvoie celle de la hashmap
		if (images.containsKey(image))
			return (Icon) images.get(image);

		// Sinon on la charge dans la hasp et on renvoie
		String chemin = IMAGES + image;
		if (new File(chemin).exists()) {
			Icon pic = new ImageIcon(chemin);
			images.put(image, pic);
			return pic;
		}

		return null;
	}

	/**
	 * Renvoie un objet Icon à partir d'un fichier. L'Icon renvoyé dépendra de
	 * l'extension de ce fichier. Si l'extension n'a pas d'image associée,
	 * l'image par défaut est renvoyée.
	 * 
	 * @param file
	 *            le fichier
	 * @return la ressource Icon
	 */
	public static Icon getImage(File file) {
		// Image spéciale pour tous les répertoires
		if (file.isDirectory())
			return get(DIRECTORY_IMAGE);

		// On a un fichier, on regarde son extension
		String s = file.getName();
		int rindex = s.lastIndexOf('.');

		// Y'a pas de '.' ou bien on a un fichier genre : "truc."
		if (rindex == -1 || rindex + 1 >= s.length())
			return getDefault();

		String extension = s.substring(rindex + 1);
		return get(EXTENSIONS + extension + ".png");
	}

	/**
	 * Renvoie l'icône par défaut.
	 * 
	 * @return icône par défaut
	 */
	public static Icon getDefault() {
		return get(DEFAULT_IMAGE);
	}
	
	/**
	 * Retourne l'icône par défaut, représentant un répertoire.
	 * 
	 * @return icône d'un répertoire par défaut
	 */
	public static Icon getDirectory() {
		return get(DIRECTORY_IMAGE);
	}
	
}

////////

