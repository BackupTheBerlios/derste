/*
 * Created on 14 oct. 2004
 */
package gui;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import misc.FileSystemTree;
import model.FileSystemTreeModel;
import renderer.FileSystemTreeCellRenderer;
import controler.FileSystemTreeControler;

/**
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeGUI extends JTree implements Observer {
    protected FileSystemTreeModel m = null;

    public FileSystemTreeGUI(FileSystemTreeModel m) {
        this.m = m;
        m.addObserver(this);

        getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        setModel(m);
        setEditable(true);
        setCellRenderer(new FileSystemTreeCellRenderer());
        addTreeSelectionListener(new FileSystemTreeControler(m));
    }

    public void update(Observable o, Object caller) {
        if (caller != this)
            setDirectory(m.getModel().getURI());
    }

    public void setDirectory(File dir) {
        setSelectionPath(FileSystemTree.getTreePath(dir));
    }

}