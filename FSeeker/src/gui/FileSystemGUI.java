/*
 * Created on 14 oct. 2004
 */
package gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;

import misc.GU;
import model.FileSystemModel;

/**
 * @author brahim
 */
public class FileSystemGUI extends JPanel {
	protected FileSystemModel m = null;

	protected JTextPane text = new JTextPane();

	protected JTree tree = null;

	public FileSystemGUI(FileSystemModel m) {
		this.m = m;
		m.addTreeModelListener(new MyTreeModelListener());
		setLayout(new GridLayout(1, 2));
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
		JSplitPane sp = new JSplitPane();
		sp.setTopComponent(tree);
		sp.setBottomComponent(text);
		add(sp);

	}

	public void setText(String txt) {
		text.setText(txt);
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

// TODO UNIFORMISER CES PUTAINS DE CHEMIN D'IMAGES !!!
// et faire que ca fonctionne, que ca affiche !!! ><
class MyCellRenderer extends DefaultTreeCellRenderer {
	protected final static Icon directoryOpened = GU.getImage("dot.gif"),
			directoryClosed = GU.getImage("dot.gif");

	protected HashMap extimg = new HashMap();
	{
		extimg.put("txt", "txt.png");
		extimg.put("java", "dot.gif");
		extimg.put("class", "dot.gif");
		extimg.put("gif", "dot.gif");
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);
		
		if (((File) value).isDirectory()) {
			if (expanded) {
				setIcon(directoryOpened);
			}
			else
				setIcon(directoryClosed);
		} else {
			Iterator it = extimg.keySet().iterator();
			boolean ok = false;
			while (it.hasNext()) {
				String ext = (String) it.next();
				if (value.toString().endsWith(ext)) {
					setIcon(GU.getImage((String) extimg.get(ext)));
					ok = true;
					break;
				}
			}
			if (!ok)
				setIcon(GU.getImage("dot.gif"));
		}
		
		return this;
	}
}

class FileSystemMain {
	public static void main(String args[]) {
		FileSystemModel m = new FileSystemModel("/");
		FileSystemGUI gui = new FileSystemGUI(m);
		//TODO GU.createGUI("Test FileSystemGUI", gui);

		JFrame f = new JFrame();
		f.add(gui);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}

