/*7
 * Created on 24 oct. 2004
 */
package misc;

import java.io.File;
import java.util.LinkedList;

import javax.swing.tree.TreePath;

/**
 * @author sted
 */
public class TreeUtilities {
	public static TreePath getTreePath(File f) {

		LinkedList filesList = new LinkedList();
		File g = new File(f.toString());

		while (g != null) {
			filesList.addLast(g);
			g = g.getParentFile();
		}

		TreePath treepath = new TreePath(filesList.removeLast());

		while (!filesList.isEmpty())
			treepath = treepath.pathByAddingChild(filesList.removeLast());

		return treepath;
	}
}