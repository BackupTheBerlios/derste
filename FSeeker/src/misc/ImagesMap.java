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
 * Classe permettant de g�rer le chargement des images de FSeeker.
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

	/** Le s�parateur de fichier */
	private static final char SEP = File.separatorChar;

	/** L� o� sont stock�es toutes les images */
	private static final String IMAGES = "images" + SEP,
			EXTENSIONS = "extensions" + SEP;

	/** Les dimensions standards */
	private static final Dimension d16x16 = new Dimension(16, 16),
			d32x32 = new Dimension(32, 32);

	/** Les images 'sp�ciales' */
	public static final String FSEEKER_LOGO = "fseeker.png",
			DEFAULT_IMAGE = EXTENSIONS + "default.png",
			DIRECTORY_OPENED_IMAGE = EXTENSIONS + "folder_yellow.png",
			DIRECTORY_CLOSED_IMAGE = EXTENSIONS + "folder_yellow_open.png",
			DIRECTORY_LOCKED_IMAGE = EXTENSIONS + "folder_locked.png";

	/**
	 * Retourne une image avec sa taille par d�faut.
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
	 * Renvoie une image � la dimension voulue. Si d est null, renvoie l'image �
	 * sa dimension par d�faut.
	 * 
	 * @param image
	 *            chemin de l'image
	 * @param d
	 *            la dimension voulue
	 * @return l'image � la dimension voulue ou l'image par d�faut si l'image
	 *         n'existe pas, ou null si celle-ci n'existe pas non plus.
	 */
	public static Icon get(String image, Dimension d) {
		final String prefix = (d == null ? "" : "#" + d.toString() + "#");

		// Si on l'a d�j� charg�e, on renvoie celle de la map
		if (images.containsKey(prefix + image))
			return (Icon) images.get(prefix + image);

		// Sinon on la charge, on la met dans la map, et on la renvoie !
		String chemin = IMAGES + image;
		if (new File(chemin).exists()) {
			Image im = Toolkit.getDefaultToolkit().getImage(chemin);

			// Si on a choisis une dimension autre que celle par d�faut
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
	 * Renvoie un objet Icon � partir d'un fichier. L'Icon renvoy� d�pendra de
	 * l'extension de ce fichier. Si l'extension n'a pas d'image associ�e,
	 * l'image par d�faut est renvoy�e.
	 * 
	 * @param file
	 *            le fichier
	 * @param d
	 *            la dimension d�sir�e de l'ic�ne retourn�
	 * @return l'ic�ne
	 */
	public static Icon get(File file, Dimension d) {
		// Image sp�ciale pour tous les r�pertoires
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
	 * Renvoie l'ic�ne d'un fichier en taille par d�faut.
	 * 
	 * @param file
	 *            fichier dont obtenir l'ic�ne
	 * @return l'ic�ne
	 */
	public static Icon get(File file) {
		return get(file, d32x32);
	}

	/**
	 * Renvoie l'ic�ne d'un fichier en taille standard 16x16.
	 * 
	 * @param file
	 *            ficheir dont obtenir l'ic�ne
	 * @return l'ic�ne en taille 16x16
	 */
	public static Icon get16x16(File file) {
		return get(file, d16x16);
	}

	/**
	 * Renvoie l'ic�ne par d�faut, � la taille par d�faut.
	 * 
	 * @return ic�ne par d�faut
	 */
	public static Icon getDefault() {
		return getDefault(d32x32);
	}
//<<<<<<< ImagesMap.java
	

	
//=======

	/**
	 * Renvoie l'ic�ne par d�faut, � la taille sp�cifi�e.
	 * 
	 * @param d
	 *            taille d�sir�e
	 * @return ic�ne par d�faut � la taille d
	 */
	public static Icon getDefault(Dimension d) {
		return get(DEFAULT_IMAGE, d);
	}

//>>>>>>> 1.16
}

