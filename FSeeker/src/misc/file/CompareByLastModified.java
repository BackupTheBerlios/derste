/*
 * Created on 27 oct. 2004
 */
package misc.file;

import java.io.File;
import java.util.Comparator;

/**
 * Comparateur de fichier, le plus grand est celui qui a la plus grande date de
 * dernière modification.
 * 
 * @author Sted
 */
public class CompareByLastModified implements Comparator {
    /** Le comparateur */
    protected static Comparator c = null;

    /* On empêche la construction */
    protected CompareByLastModified() {
    }

    /** Renvoie une instance (singleton) de CompareByLastModified. */
    public static Comparator get() {
        if (c == null)
            c = new CompareByLastModified();
        return c;
    }

    public int compare(Object o1, Object o2) {
        if (o1 == null)
            return 1;
        if (o2 == null)
            return -1;

        File f1 = (File) o1;
        File f2 = (File) o2;

        // Les répertoires, toujours en premier
        if (f1.isDirectory() && f2.isFile())
            return -1;
        if (f1.isFile() && f2.isDirectory())
            return 1;

        return (f1.lastModified() > f2.lastModified() ? -1 : 1);
    }
}