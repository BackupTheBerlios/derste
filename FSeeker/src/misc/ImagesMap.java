/*
 * Created on 18 oct. 2004
 */
package misc;

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

    private static final String PREFIX16x16 = "16x16-";
	
	/** Là où sont stockées toutes les images */
	private static final String 
		IMAGES = "images" + SEP, 
		EXTENSIONS = "extensions" + SEP;
	
	/** Les images 'spéciales' */
	public static final String	
		FSEEKER_LOGO = "fseeker.png",
		DEFAULT_IMAGE = EXTENSIONS + "default.png",
		DIRECTORY_OPENED_IMAGE = EXTENSIONS + "folder_yellow.png",
		DIRECTORY_CLOSED_IMAGE = EXTENSIONS + "folder_yellow_open.png",
		DIRECTORY_LOCKED_IMAGE = EXTENSIONS + "folder_locked.png";

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

		// Sinon on la charge dans la hashmap et on renvoie
		String chemin = IMAGES + image;
		if (new File(chemin).exists()) {
			Icon pic = new ImageIcon(chemin);
			images.put(image, pic);
			return pic;
		}
		
		// On tente de mettre l'image par défaut
		return image.equals(DEFAULT_IMAGE) ? null : get(DEFAULT_IMAGE);
	}
	
	public static Icon get16x16(String image) {
		// Si on l'a déjà chargée, on renvoie celle de la hashmap
		if (images.containsKey(PREFIX16x16 + image))
			return (Icon) images.get(PREFIX16x16 + image);

		// Sinon on la charge dans la hashmap et on renvoie
		String chemin = IMAGES + image;
		if (new File(chemin).exists()) {
		    Image im = Toolkit.getDefaultToolkit().getImage(IMAGES + image);
		    Icon pic = new ImageIcon(im.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			images.put(PREFIX16x16 + image, pic);
			return pic;
		}
		
		// On tente de mettre l'image par défaut
		return image.equals(DEFAULT_IMAGE) ? null : get16x16(DEFAULT_IMAGE);
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
	public static Icon get(File file) {
		// Image spéciale pour tous les répertoires
		if (file.isDirectory()) {
			if (file.canRead())
				return get(DIRECTORY_CLOSED_IMAGE);
			return get(DIRECTORY_LOCKED_IMAGE);
		}
		
		// On a un fichier, on regarde son extension
		String s = file.getName();
		int rindex = s.lastIndexOf('.');

		// Y'a pas de '.' ou bien on a un fichier genre : "truc."
		if (rindex == -1 || rindex + 1 >= s.length())
			return get(DEFAULT_IMAGE);

		String extension = s.substring(rindex + 1);
		return get(EXTENSIONS + extension + ".png");
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
	public static Icon get16x16(File file) {
		// Image spéciale pour tous les répertoires
		if (file.isDirectory()) {
			if (file.canRead())
				return get16x16(DIRECTORY_CLOSED_IMAGE);
			return get16x16(DIRECTORY_LOCKED_IMAGE);
		}
		
		// On a un fichier, on regarde son extension
		String s = file.getName();
		int rindex = s.lastIndexOf('.');

		// Y'a pas de '.' ou bien on a un fichier genre : "truc."
		if (rindex == -1 || rindex + 1 >= s.length())
			return get16x16(DEFAULT_IMAGE);

		String extension = s.substring(rindex + 1);
		return get16x16(EXTENSIONS + extension + ".png");
	}

	/**
	 * Renvoie l'icône par défaut.
	 * 
	 * @return icône par défaut
	 */
	public static Icon getDefault() {
		return get(DEFAULT_IMAGE);
	}
	
	
}

////////

