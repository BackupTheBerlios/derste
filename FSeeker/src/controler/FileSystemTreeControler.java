/*
 * Created on 16 oct. 2004
 */
package controler;

import java.io.File;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.FileSystemTreeModel;

/**
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeControler implements TreeSelectionListener {
    protected FileSystemTreeModel m = null;

    public FileSystemTreeControler(FileSystemTreeModel m) {
        this.m = m;
    }

    public void valueChanged(TreeSelectionEvent e) {
        File f = (File) e.getNewLeadSelectionPath().getLastPathComponent();
        m.getModel().setURI(f, e.getSource());
    }

}