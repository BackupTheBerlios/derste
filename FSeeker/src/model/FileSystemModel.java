package model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author brahim
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileSystemModel implements TreeModel {

	public class MyFile extends File {
		public MyFile(String f) {
			super(f);
		}

		public MyFile(File parent, String child) {
			super(parent, child);
		}

		public String toString() {
			return getName();
		}
	}

	protected File root = null;

	protected ArrayList listeners = new ArrayList();

	protected FileFilter filter = new DirFilter();

	public FileSystemModel(String s) {
		this(new File(s));
	}

	public FileSystemModel(File root) {
		this.root = root;
	}

	public Object getRoot() {
		return root;
	}

	public Object getChild(Object parent, int index) {
		File file = (File) parent;
		String[] children = getStrFiles(file.listFiles(filter));//Utilisation
		// du filtre

		if (children == null || index < 0 || index >= children.length)
			return null;

		return new MyFile((File) parent, children[index]);
	}

	public int getChildCount(Object parent) {
		File f = (File) parent;

		if (parent == null || f.isFile())
			return 0;

		File[] files = (f.listFiles(filter)); //Utilisation du filtre
		return (files != null ? files.length : 0);
	}

	public boolean isLeaf(Object node) {
		//return ((File) node).isFile();
		return false;
	}

	public int getIndexOfChild(Object parent, Object child) {
		File f = (File) parent;

		if (f.isFile())
			return -1;

		File[] files = f.listFiles(); //Utilisation du filtre
		for (int i = 0; i < files.length; i++)
			if (files[i].equals(child))
				return i;

		return -1;
	}

	public void addTreeModelListener(TreeModelListener l) {
		if (l != null && !listeners.contains(l))
			listeners.add(l);
	}

	public void removeTreeModelListener(TreeModelListener l) {
		if (l != null)
			listeners.remove(l);
	}

	public void valueForPathChanged(TreePath path, Object value) {
		File oldFile = (File) path.getLastPathComponent();
		String fileParentPath = oldFile.getParent();
		String newFileName = (String) value;
		File targetFile = new File(fileParentPath, newFileName);
		oldFile.renameTo(targetFile);
		File parent = new File(fileParentPath);
		int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
		Object[] changedChildren = { targetFile };
		fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices,
				changedChildren);
	}

	public void fireTreeNodesChanged(TreePath parentPath, int[] indices,
			Object[] children) {
		TreeModelEvent ev = new TreeModelEvent(this, parentPath, indices,
				children);
		Iterator it = listeners.iterator();
		while (it.hasNext())
			//On gére qiue les dossiers
			((TreeModelListener) it.next()).treeNodesChanged(ev);
	}

	/**
	 * On renvoie touts les fichiers du tableau files sous formes de chaine de
	 * characteres *
	 */
	public static String[] getStrFiles(File[] files) {
		int num = files.length;
		String[] dirs = new String[num];
		for (int i = 0; i < num; i++)
			dirs[i] = files[i].getName();

		return dirs;

	}

}

class DirFilter implements FileFilter {
	public boolean accept(File pathname) {
		return (pathname.isDirectory());
	}
}

