/*7
 * Created on 24 oct. 2004
 */
package misc;

import java.io.File;
import java.util.LinkedList;

import javax.swing.tree.TreePath;

/**
 * Classe utilitaire regroupant les méthodes appliquées sur des arbres.
 * 
 * @author sted
 */
public class TreeUtilities {
	
	/**
	 * Retourne le TreePath d'un fichier.
	 * 
	 * @param f un fichier
	 * @return son TreePath
	 */
	public static TreePath getTreePath(File f) {

		// On empile tout d'un côté...
		LinkedList filesList = new LinkedList();
		File g = new File(f.toString());

		while (g != null) {
			filesList.addLast(g);
			g = g.getParentFile();
		}

		TreePath treepath = new TreePath(filesList.removeLast());

		// ... et on ressort tout de l'autre sens !
		while (!filesList.isEmpty())
			treepath = treepath.pathByAddingChild(filesList.removeLast());

		return treepath;
	}
}