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
 */
public class ImagesMap {
    /** L'image par défaut */
    private static final String DEFAULT_IMAGE = "dot.gif";

    /**
     * Le tableau associatif contenant les images. <br>
     * [String clef] => [Icon ressource]
     */
    protected static final HashMap images = new HashMap();

    /** Le séparateur de fichier */
    private static final char SEP = File.separatorChar;

    /** Là où sont stockées toutes les images */
    private static final String IMAGES = "images" + SEP;

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
        String chemin = IMAGES + SEP + image;
        if (new File(chemin).exists()) {
            Icon pic = new ImageIcon(chemin);
            images.put(image, pic);
            return pic;
        }

        return null;
    }

    public static Icon getDefault() {
        return get(DEFAULT_IMAGE);
    }
}

////////

