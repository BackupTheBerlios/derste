/*
 * Created on 16 oct. 2004
 */
package controler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import misc.FileSystemTree;
import model.FileSystemTreeModel;

/**
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeControler implements TreeSelectionListener,
        KeyListener {
    protected FileSystemTreeModel m = null;

    public FileSystemTreeControler(FileSystemTreeModel m) {
        this.m = m;
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

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

}