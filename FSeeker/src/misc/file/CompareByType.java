/*
 * Created on 27 oct. 2004
 */
package misc.file;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

/**
 * Comparateur de fichier, triés avec leurs extensions par ordre
 * lexicographique, avec les nom de fichiers triés pareillement dans leur groupe
 * d'extension.
 * 
 * @author Sted
 */
public class CompareByType implements Comparator {
	/** Le comparateur */
	protected static Comparator c = null;

	/* On empêche la construction */
	protected CompareByType() {
	}

	/** Renvoie une instance (singleton) de CompareByType. */
	public static Comparator get() {
		if (c == null)
			c = new CompareByType();
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
		// ATTENTION, ne pas utiliser isFile() car un device n'est NI un rép, ni
		// un file !
		if (f1.isDirectory() && !f2.isDirectory())
			return -1;
		if (!f1.isDirectory() && f2.isDirectory())
			return 1;

		// ".truc" sera avant "afoo" (sinon, le '.' sera détecté après, et on
		// aura "afoo" avant ".truc"
		if (f1.isDirectory() && f2.isDirectory())
			return CompareByName.get().compare(f1, f2);

		String sf1 = f1.getName();
		String sf2 = f2.getName();
		int r1 = sf1.lastIndexOf('.');
		int r2 = sf2.lastIndexOf('.');

		// Fichiers sans extensions, "prout" vs "machin" > machin avant prout
		if (r1 == -1 && r2 == -1)
			return Collator.getInstance().compare(sf1, sf2);

		// F1 sans extension, "prout" vs "truc.foo" > prout avant truc.foo
		if (r1 == -1)
			return -1;

		// F2 sans extension, "prout.txt" vs "truc" > truc avant prout.txt
		if (r2 == -1)
			return 1;

		// Avec extensions, on commence par comparer les extensions
		String ext1 = sf1.substring(r1 + 1);
		String ext2 = sf2.substring(r2 + 1);

		// "truc.foo" vs "bidule.foo" > bidule.foo avant truc.foo
		if (Collator.getInstance().compare(ext1, ext2) == 0)
			return Collator.getInstance().compare(sf1, sf2);

		// "truc.foo" vs "bidule.bar" > bidule.bar avant
		return Collator.getInstance().compare(ext1, ext2);
	}
}