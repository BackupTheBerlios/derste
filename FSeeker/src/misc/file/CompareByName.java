/*
 * Created on 27 oct. 2004
 */
package misc.file;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

/**
 * Comparateur de fichier, le plus grand est celui qui a le nom le plus long,
 * selon la Locale courante.
 * 
 * @author Sted
 */
public class CompareByName implements Comparator {
    /** Le comparateur */
    protected static Comparator c = null;

    /* On empêche la construction */
    protected CompareByName() {
    }

    /** Renvoie une instance (singleton) de CompareByName. */
    public static Comparator get() {
        if (c == null)
            c = new CompareByName();
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
        // ATTENTION, ne pas utiliser isFile() car un device n'est NI un rép, ni un file !
        if (f1.isDirectory() && !f2.isDirectory())
            return -1;
        if (!f1.isDirectory() && f2.isDirectory())
            return 1;

        return Collator.getInstance().compare(f1.getName(), f2.getName());
    }
}