/*
 * Created on 27 oct. 2004
 */
package misc.file;

import java.io.File;
import java.util.Comparator;

/**
 * Comparateur de fichier, le plus grand est celui qui a la plus grande taille.
 * 
 * @author Sted
 */
public class CompareBySize implements Comparator {
    /** Le comparateur */
    protected static Comparator c = null;

    /* On empêche la construction */
    protected CompareBySize() {
    }

    /** Renvoie une instance (singleton) de CompareBySize. */
    public static Comparator get() {
        if (c == null)
            c = new CompareBySize();
        return c;
    }

    public int compare(Object o1, Object o2) {
        if (o1 == null)
            return 1;
        if (o2 == null)
            return -1;

        File f1 = (File) o1;
        File f2 = (File) o2;

        // Si on a pas deux fichiers, on trie comme le CompareByName
        if (!f1.isFile() || !f2.isFile())
            return CompareByName.get().compare(o1, o2);

        // Sinon, on compare la taille..
        return (f1.length() > f2.length() ? -1 : 1);
    }
}