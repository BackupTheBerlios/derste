/*
 * Created on 16 oct. 2004
 */
package controler;


import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.FileSystemTreeModel;

/**
 * @author brahim
 * @author Sted
 */
public class FileSystemTreeControler implements TreeSelectionListener {
	protected FileSystemTreeModel m = null;
	
	public FileSystemTreeControler(FileSystemTreeModel m) {
		this.m = m;
	}

	public void valueChanged(TreeSelectionEvent e) {
		System.out.println("FileSystemTreeControler.valueChanged() / " + e);
	    File f = (File) e.getPath().getLastPathComponent();
		m.setCurrentDirectory(f);
	}
	    
}
