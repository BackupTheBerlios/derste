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
 * Représente un système de fichier, en ne prenant en compte que les répertoires.
 * 
 * @author brahim
 * @author Sted
 */
public class FileSystemModel implements TreeModel {

	/**
	 * Représente un fichier. Utilisé pour que le toString ne renvoie que le nom
	 * de fichier.
	 * 
	 * @author sted
	 */
	public class MyFile extends File {
		
		/**
		 * Construit un MyFile à partir d'une String représentant le nom du fichier.
		 * 
		 * @param f nom du fichier
		 */
		public MyFile(String f) {
			super(f);
		}

		/**
		 * Construit un MyFile à partir d'une String, représentant le nom du fichier, et un File, représentant le père de ce fichier. 
		 * 
		 * @param parent le parent du child
		 * @param child le fichier
		 */
		public MyFile(File parent, String child) {
			super(parent, child);
		}

		/**
		 * Retourne le nom du fichier.
		 */
		public String toString() {
			return getName();
		}
	}

	/** Racine de l'arbre */
	protected File root = null;

	/** Liste des listeners associés à l'évènement TreeModelEvent */ 
	protected ArrayList listeners = new ArrayList();

	/** Permet de ne sélectionner que les répertoires */
	protected FileFilter filter = new FileFilter() {
		public boolean accept(File pathname) {
			return (pathname.isDirectory());
		}
	};

	/**
	 * Construit un FileSystemModel avec le fichier représenté par <code>s</code> comme racine.
	 * 
	 * @param s racine
	 */
	public FileSystemModel(String s) {
		this(new File(s));
	}
	
	/**
	 * Construit un FileSystemModel avec <code>root</code> comme racine.
	 * 
	 * @param root racine
	 */
	public FileSystemModel(File root) {
		this.root = root;
	}

	public Object getRoot() {
		return root;
	}

	public Object getChild(Object parent, int index) {
		File file = (File) parent;
		String[] children = getStrFiles(file.listFiles(filter));

		if (children == null || index < 0 || index >= children.length)
			return null;

		return new MyFile((File) parent, children[index]);
	}

	public int getChildCount(Object parent) {
		File f = (File) parent;

		if (parent == null/* || f.isFile() Forcément un rép */)
			return 0;

		File[] files = (f.listFiles(filter));
		return (files != null ? files.length : 0);
	}

	public boolean isLeaf(Object node) {
		// return ((File) node).isFile();
		return false;
	}

	public int getIndexOfChild(Object parent, Object child) {
		File f = (File) parent;

		//if (f.isFile()) C'est forcément un rép dans le cas présent
		//return -1;

		// TODO ça fait pareil avec sous sans filtre non ? Vérifier. Je pense
		// qu'il vaudrait mieux le mettre. :|
		File[] files = f.listFiles();
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
			((TreeModelListener) it.next()).treeNodesChanged(ev);
	}

	/**
	 * Renvoie un tableau contenant les noms des fichiers d'un tableau de File.
	 * 
	 * @param files tableau de Files dont retournés les noms
	 * @return un tableau de String ne contenant que les noms des fichiers
	 */
	public static String[] getStrFiles(File[] files) {
		int num = files.length;
		String[] dirs = new String[num];
		for (int i = 0; i < num; i++)
			dirs[i] = files[i].getName();

		return dirs;
	}

}

