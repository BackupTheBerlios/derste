package model;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

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
		String[] children = ((File) parent).list();

		if (children == null || index < 0 || index >= children.length)
			return null;

		return new MyFile((File) parent, children[index]);
	}

	public int getChildCount(Object parent) {
		File f = (File) parent;

		if (parent == null || f.isFile())
			return 0;

		File[] files = f.listFiles();
		return (files == null ? 0 : files.length);
	}

	public boolean isLeaf(Object node) {
		return ((File) node).isFile();
	}

	public int getIndexOfChild(Object parent, Object child) {
		File f = (File) parent;

		if (f.isFile())
			return -1;

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

	public void valueForPathChanged(TreePath path, Object newValue) {
		File f = (File) path.getLastPathComponent();
		if (!f.equals(newValue)) {
			/*
			 * String fileParentPath = oldFile.getParent(); String newFileName =
			 * (String) newValue; File targetFile = new File(fileParentPath,
			 * newFileName); oldFile.renameTo(targetFile); File parent = new
			 * File(fileParentPath); int[] changedChildrenIndices = {
			 * getIndexOfChild(parent, targetFile) }; Object[] changedChildren = {
			 * targetFile };
			 * 
			 * fireTreeNodesChanged(path.getParentPath(),
			 * changedChildrenIndices, changedChildren);
			 */
			///
			File target = new File(f.getParent(), (String) newValue);
			f.renameTo(target);

			fireTreeNodesChanged(
					path.getParentPath(),
					new int[] { getIndexOfChild(new File(f.getParent()), target) },
					new Object[] { target });

		}
	}

	public void fireTreeNodesChanged(TreePath parentPath, int[] indices,
			Object[] children) {
		TreeModelEvent ev = new TreeModelEvent(this, parentPath, indices,
				children);
		Iterator it = listeners.iterator();
		while (it.hasNext())
			((TreeModelListener) it.next()).treeNodesChanged(ev);
	}

}

class MyTreeModelListener implements TreeModelListener {
	public void treeNodesChanged(TreeModelEvent e) {
		File f = (File) e.getChildren()[0];
	}

	public void treeNodesInserted(TreeModelEvent e) {
	}

	public void treeNodesRemoved(TreeModelEvent e) {
	}

	public void treeStructureChanged(TreeModelEvent e) {
	}
}

class FileSystemGUI extends JFrame {
	protected FileSystemModel m = null;

	protected JTextArea text = new JTextArea(10, 10);

	protected JTree tree = null;

	public void setText(String s) {
		text.setText(s);
	}

	public FileSystemGUI(FileSystemModel m) {
		this.m = m;
		m.addTreeModelListener(new MyTreeModelListener());

		Container c = getContentPane();
		c.setLayout(new GridLayout(1, 2));
		tree = new JTree(m);
		tree.setEditable(true);
		tree.setCellRenderer(new MyCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				File f = (File) e.getPath().getLastPathComponent();
				String s = f.getAbsolutePath();

				setText("Path : " + s + "\nFichier : "
						+ e.getPath().getLastPathComponent().toString()
						+ "\nTaille : "
						+ ((File) e.getPath().getLastPathComponent()).length()
						+ " octets");
			}
		});

		c.add(new JScrollPane(tree));
		c.add(new JScrollPane(text));

		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class MyCellRenderer extends JLabel implements TreeCellRenderer {
	protected ImageIcon directoryOpened = new ImageIcon("diropen.gif"),
			directoryClosed = new ImageIcon("dir.gif");

	protected HashMap extimg = new HashMap();
	{
		extimg.put("txt", "txt.gif");
		extimg.put("java", "java.gif");
		extimg.put("class", "class.gif");
		extimg.put("gif", "gif.gif");
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		setText(value.toString());
		if (((File) value).isDirectory()) {
			if (expanded)
				setIcon(directoryOpened);
			else
				setIcon(directoryClosed);
		} else {
			Iterator it = extimg.keySet().iterator();
			boolean ok = false;
			while (it.hasNext()) {
				String ext = (String) it.next();
				if (value.toString().endsWith(ext)) {
					setIcon(new ImageIcon((String) extimg.get(ext)));
					ok = true;
					break;
				}
			}
			if (!ok)
				setIcon(new ImageIcon("file.gif"));
		}

		return this;
	}
}

class FileSystemMain {
	public static void main(String args[]) {
		FileSystemModel m = new FileSystemModel("/");
		FileSystemGUI gui = new FileSystemGUI(m);
	}
}

class FileSystemControler implements TreeSelectionListener {
	protected FileSystemModel m = null;

	protected FileSystemGUI gui = null;

	public FileSystemControler(FileSystemModel m, FileSystemGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	public void valueChanged(TreeSelectionEvent e) {
		gui.setText("Path : " + e.getPath().getParentPath().toString()
				+ "\nFichier : "
				+ e.getPath().getLastPathComponent().toString() + "Taille : "
				+ ((File) e.getPath().getLastPathComponent()).length()
				+ " octets");
	}
}