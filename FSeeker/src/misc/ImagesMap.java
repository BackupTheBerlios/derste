/*
 * Created on 18 oct. 2004
 */
package misc;

import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Classe permettant de g�rer le chargement des images de FSeeker.
 * 
 * @author Sted
 * @author aitelhab
 */
public class ImagesMap {
	/** L'image par d�faut */
	private static final String DEFAULT_IMAGE = "default.png";

	/**
	 * Le tableau associatif contenant les images. <br>
	 * [String clef] => [Icon ressource]
	 */
	protected static final HashMap images = new HashMap();

	/** Le s�parateur de fichier */
	private static final char SEP = File.separatorChar;

	/** L� o� sont stock�es toutes les images */
	private static final String IMAGES = "images", EXTENSIONS = "extensions";

	/**
	 * Renvoie un objet Icon � partir du chemin de image. Si le chemin n'est pas
	 * valide ou que l'image n'existe pas, renvoie null.
	 * 
	 * @param image
	 *            le chemin de l'image (ie: "dot.gif");
	 * @return la ressource Icon
	 */
	public static Icon get(String image) {
		// Si on l'a d�j� charg�e, on renvoie celle de la hashmap
		if (images.containsKey(image))
			return (Icon) images.get(image);

		// Sinon on la charge dans la hasp et on renvoie
		String chemin = IMAGES + SEP + image;
		if (new File(chemin).exists()) {
			Icon pic = new ImageIcon(chemin);
			images.put(image, pic);
			return pic;
		}

		return null;
	}

	/**
	 * Renvoie un objet Icon � partir d'un fichier. L'Icon renvoy� d�pendra de
	 * l'extension de ce fichier. Si l'extension n'a pas d'image associ�e,
	 * l'image par d�faut est renvoy�e.
	 * 
	 * @param file
	 *            le fichier
	 * @return la ressource Icon
	 */
	public static Icon getImage(File file) {
		// Image sp�ciale pour tous les r�pertoires
		if (file.isDirectory())
			return get("directory.png");

		// On a un fichier, on regarde son extension		
		String s = file.getName();
		int rindex = s.lastIndexOf('.');		

		// Y'a pas de '.' ou bien on a un fichier genre : "truc."
		if (rindex == -1 || rindex + 1 >= s.length())
			return getDefault();
		
		String ext = s.substring(rindex + 1);
		return get(EXTENSIONS + SEP + ext + ".png");
	}

	public static Icon getDefault() {
		return get(DEFAULT_IMAGE);
	}
}

////////

