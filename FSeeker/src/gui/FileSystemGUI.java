/*
 * Created on 14 oct. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;

import misc.FileExtensionMap;
import misc.GU;
import model.FileSystemModel;

/**
 * @author brahim
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
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
		JScrollPane scrollTop = new JScrollPane(tree);
		JScrollPane scrollBottom = new JScrollPane(text);		
		sp.setTopComponent(scrollTop);
		sp.setBottomComponent(scrollBottom);
		add(sp);

	}
	
	public void setText(String txt){
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

class MyCellRenderer extends DefaultTreeCellRenderer {
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {		
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		// Associe un icone au fichier
		FileExtensionMap map = new FileExtensionMap((File)value);
		setIcon(map.getIcon(""));
		
		return this;
	}
}



class FileSystemMain {
	public static void main(String args[]) {
		FileSystemModel m = new FileSystemModel("/");
		FileSystemGUI gui = new FileSystemGUI(m);
		GU.createGUI("Test FileSystemGUI", gui);
		
	}
}

