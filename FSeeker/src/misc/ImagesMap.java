/*
 * Created on 18 oct. 2004
 */
package misc;

//<<<<<<< ImagesMap.java

//=======
import java.awt.Dimension;
//>>>>>>> 1.16
import java.awt.Image;

import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 * Classe permettant de gérer le chargement des images de FSeeker.
 * 
 * @author Sted
 * @author brahim
 */
public final class ImagesMap {
	/**
	 * Le tableau associatif contenant les images. <br>
	 * [String clef] => [Icon ressource]
	 */
	private static final HashMap images = new HashMap();

	/** Le séparateur de fichier */
	private static final char SEP = File.separatorChar;

	/** Là où sont stockées toutes les images */
	private static final String IMAGES = "images" + SEP,
			EXTENSIONS = "extensions" + SEP;

	/** Les dimensions standards */
	private static final Dimension d16x16 = new Dimension(16, 16),
			d32x32 = new Dimension(32, 32);

	/** Les images 'spéciales' */
	public static final String FSEEKER_LOGO = "fseeker.png",
			DEFAULT_IMAGE = EXTENSIONS + "default.png",
			DIRECTORY_OPENED_IMAGE = EXTENSIONS + "folder_yellow.png",
			DIRECTORY_CLOSED_IMAGE = EXTENSIONS + "folder_yellow_open.png",
			DIRECTORY_LOCKED_IMAGE = EXTENSIONS + "folder_locked.png";

	/**
	 * Retourne une image avec sa taille par défaut.
	 * 
	 * @param image
	 *            nom de l'image
	 * @return l'objet image
	 */
	public static Icon get(String image) {
		return get(image, null);
	}

	/**
	 * Retourne une image avec la taille standard 32x32.
	 * 
	 * @param image
	 *            nom de l'image
	 * @return l'objet image
	 */
	public static Icon get32x32(String image) {
		return get(image, d32x32);
	}

	/**
	 * Retourne une image avec la taille standard 16x16.
	 * 
	 * @param image
	 *            nom de l'image
	 * @return l'objet image
	 */
	public static Icon get16x16(String image) {
		return get(image, d16x16);
	}

	/**
	 * Renvoie une image à la dimension voulue. Si d est null, renvoie l'image à
	 * sa dimension par défaut.
	 * 
	 * @param image
	 *            chemin de l'image
	 * @param d
	 *            la dimension voulue
	 * @return l'image à la dimension voulue ou l'image par défaut si l'image
	 *         n'existe pas, ou null si celle-ci n'existe pas non plus.
	 */
	public static Icon get(String image, Dimension d) {
		final String prefix = (d == null ? "" : "#" + d.toString() + "#");

		// Si on l'a déjà chargée, on renvoie celle de la map
		if (images.containsKey(prefix + image))
			return (Icon) images.get(prefix + image);

		// Sinon on la charge, on la met dans la map, et on la renvoie !
		String chemin = IMAGES + image;
		if (new File(chemin).exists()) {
			Image im = Toolkit.getDefaultToolkit().getImage(chemin);

			// Si on a choisis une dimension autre que celle par défaut
			if (d != null)
				im = im
						.getScaledInstance(d.width, d.height,
								Image.SCALE_SMOOTH);

			// On met dans la map pour un futur usage et on renvoie !
			Icon pic = new ImageIcon(im);
			images.put(prefix + image, pic);
			return pic;
		}

		return image.equals(DEFAULT_IMAGE) ? null : get(DEFAULT_IMAGE, d);
	}

	/**
	 * Renvoie un objet Icon à partir d'un fichier. L'Icon renvoyé dépendra de
	 * l'extension de ce fichier. Si l'extension n'a pas d'image associée,
	 * l'image par défaut est renvoyée.
	 * 
	 * @param file
	 *            le fichier
	 * @param d
	 *            la dimension désirée de l'icône retourné
	 * @return l'icône
	 */
	public static Icon get(File file, Dimension d) {
		// Image spéciale pour tous les répertoires
		if (file.isDirectory()) {
			if (file.canRead())
				return get(DIRECTORY_CLOSED_IMAGE, d);
			return get(DIRECTORY_LOCKED_IMAGE, d);
		}

		// On a un fichier, on regarde son extension
		String s = file.getName();
		int rindex = s.lastIndexOf('.');

		// Y'a pas de '.' ou bien on a un fichier genre : "truc."
		if (rindex == -1 || rindex + 1 >= s.length())
			return get(DEFAULT_IMAGE, d);

		String extension = s.substring(rindex + 1);
		return get(EXTENSIONS + extension + ".png", d);
	}

	/**
	 * Renvoie l'icône d'un fichier en taille par défaut.
	 * 
	 * @param file
	 *            fichier dont obtenir l'icône
	 * @return l'icône
	 */
	public static Icon get(File file) {
		return get(file, d32x32);
	}

	/**
	 * Renvoie l'icône d'un fichier en taille standard 16x16.
	 * 
	 * @param file
	 *            ficheir dont obtenir l'icône
	 * @return l'icône en taille 16x16
	 */
	public static Icon get16x16(File file) {
		return get(file, d16x16);
	}

	/**
	 * Renvoie l'icône par défaut, à la taille par défaut.
	 * 
	 * @return icône par défaut
	 */
	public static Icon getDefault() {
		return getDefault(d32x32);
	}
//<<<<<<< ImagesMap.java
	

	
//=======

	/**
	 * Renvoie l'icône par défaut, à la taille spécifiée.
	 * 
	 * @param d
	 *            taille désirée
	 * @return icône par défaut à la taille d
	 */
	public static Icon getDefault(Dimension d) {
		return get(DEFAULT_IMAGE, d);
	}

//>>>>>>> 1.16
}

