/*
 * Created on 16 oct. 2004
 */
package controler;

import gui.FileSystemTreeGUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import misc.FileSystemTree;
import model.FileSystemTreeModel;

/**
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeControler implements TreeSelectionListener,
		KeyListener, TreeModelListener {
	protected FileSystemTreeModel m = null;

	protected FileSystemTreeGUI gui = null;

	public FileSystemTreeControler(FileSystemTreeModel m, FileSystemTreeGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	public void valueChanged(TreeSelectionEvent e) {
		File f = (File) e.getNewLeadSelectionPath().getLastPathComponent();
		m.getModel().setURI(f, e.getSource());
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER
				&& e.getModifiersEx() == KeyEvent.ALT_DOWN_MASK) {
			// TODO les propriétés, ie : FileUtilities.showProperties(File f)
			JTree tree = (JTree) e.getSource();
			tree.expandPath(FileSystemTree.getTreePath(m.getModel().getURI()));
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void treeNodesChanged(TreeModelEvent e) {
	}

	public void treeNodesInserted(TreeModelEvent e) {
	}

	public void treeNodesRemoved(TreeModelEvent e) {
	}

	public void treeStructureChanged(TreeModelEvent e) {
	}

}