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
    /** L'image par défaut */
    private static final String DEFAULT_IMAGE = "default.png";

    /**
     * Le tableau associatif contenant les images. <br>
     * [String clef] => [Icon ressource]
     */
    protected static final HashMap images = new HashMap();

    /** Le séparateur de fichier */
    private static final char SEP = File.separatorChar;

    /** Là où sont stockées toutes les images */
    private static final String IMAGES = "images"+SEP+"extensions";

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

    public static Icon getIcon(File file) {
        String s = file.getName();
        Icon fileIcon = null;
        String fileStr = file.toString();

        if (file.isDirectory()) {
            fileIcon = get("directory.png");
        } else {

            int rindex = fileStr.lastIndexOf('.');

            String ext = null;
            if (rindex >= 0)
                ext = fileStr.substring(rindex + 1);

            if (ext != null)
                fileIcon = get(ext + ".png");
            if (fileIcon == null)
                fileIcon = getDefault();
            
        }
        return fileIcon;
    }

    private  Icon getImage(String ext) {
        if ("".equals(ext))
            return getDefault();
        return get(ext + ".png");
    }

    public static Icon getDefault() {
        return get(DEFAULT_IMAGE);
    }
}

////////

