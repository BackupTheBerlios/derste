/*
 * Created on 24 oct. 2004
 */
package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author sted
 */
public class FileUtilities {
    public static String getDetails(File f) {
        StringBuffer sb = new StringBuffer(50);
        sb.append("<html>");
        sb.append("<center><img src=\"images/dot.gif\"></center><br>");

        sb.append("<b>" + (f.isDirectory() ? "Doss" : "Fich") + "ier</b> : "
                + f.getName() + "<br>");
        sb.append("<b>Taille</b> : " + f.length() + " octets<br>");

        DateFormat dateFormat = DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT, DateFormat.DEFAULT);
        Date d = new Date(f.lastModified());
        sb.append("<b>Derni�re modification</b> : " + dateFormat.format(d)
                + "<br>");

        File[] foo = f.listFiles();
        if (foo != null && foo.length > 0)
            sb.append("<b>Contient</b> : " + foo.length + " fichiers");

        sb.append("</html>");

        return sb.toString();
    }

    /**
     * Copie un fichier.
     * 
     * @param src
     *            fichier source
     * @param dst
     *            fichier destination
     * @return <code>true</code> si la copie a r�ussi
     */
    public static boolean copy(File src, File dst) {
        // On n'�crase pas (� faire du c�t� de l'appelant �a)
        if (dst.exists())
            return false;

        boolean resultat = false;

        // Declaration des flux
        FileInputStream sourceFile = null;
        FileOutputStream destinationFile = null;

        try {
            // Cr�ation du fichier
            dst.createNewFile();

            // Ouverture des flux
            sourceFile = new FileInputStream(src);
            destinationFile = new FileOutputStream(dst);

            // Lecture par segment de 0.5Mo
            byte buffer[] = new byte[512 * 1024];
            int nbLecture;

            while ((nbLecture = sourceFile.read(buffer)) != -1)
                destinationFile.write(buffer, 0, nbLecture);

            // Copie r�ussie
            resultat = true;
        } catch (FileNotFoundException f) {

        } catch (IOException e) {

        } finally {
            // Quoi qu'il arrive, on ferme les flux
            try {
                sourceFile.close();
            } catch (IOException e) {}
            try {
                destinationFile.close();
            } catch (IOException e) {}
        }

        return resultat;
    }

    /**
     * D�place un fichier.
     * 
     * @param src
     *            fichier source
     * @param dst
     *            fichier destination
     * @return <code>true</code> si le d�placement a r�ussi
     */
    public static boolean move(File src, File dst) {
        // On n'�crase pas, et on essaye avec renameTo ou avec delete + copy
        return !dst.exists()
                && (src.renameTo(dst) || (copy(src, dst) && src.delete()));
    }

    /**
     * Supprime un fichier ou r�pertoire (en r�cursif pour celui-ci).
     * 
     * @param file
     *            le fichier ou r�pertoire � supprimer
     * @return <code>true</code> si la suppression a �t� correctement
     *         effectu�e
     */
    public static boolean delete(File file) {
        // Cas triviaux
        if (!file.exists())
            return false;

        if (file.isFile())
            return file.delete();

        // On a un r�pertoire, r�cursivit� powa
        boolean resultat = true;

        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++)
            if (files[i].isDirectory())
                resultat &= delete(files[i]);
            else
                resultat &= files[i].delete();

        resultat &= file.delete();
        
        return resultat;
    }

}