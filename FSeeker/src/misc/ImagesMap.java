/*
 * Created on 18 oct. 2004
 */
package misc;

import java.io.File;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Sted
 */
public class ImagesMap {
    protected static final HashMap images = new HashMap();
    
	public static final char SEP = File.separatorChar;

	/** Là où sont stockées toutes les images */
	private static final String IMAGES = "images" + SEP;
    
    public static Icon get(String image) {
        if (images.containsKey(image))
            return (Icon) images.get(image);
        String chemin = IMAGES + SEP + image;
        if (new File(chemin).exists()) {
            Icon pic = new ImageIcon(chemin);
            images.put(image, pic);
            return pic;
        }
        return null;
    }
}



////////

