/*
 * Created on 16 oct. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package controler;


import gui.FileSystemGUI;

import java.io.File;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.FileSystemModel;

/**
 * @author brahim
 *
 * Cette classe ne sert à rien ... Envisager à la deleter ?
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileSystemControler implements TreeSelectionListener {
	protected FileSystemModel m = null;
	protected FileSystemGUI gui = null;

	public FileSystemControler(FileSystemModel m, FileSystemGUI gui) {
		this.m = m;
		this.gui = gui;
		
	}

	public void valueChanged(TreeSelectionEvent e) {
		File f = (File) e.getPath().getLastPathComponent();
		String s = f.getAbsolutePath();
		
		gui.setText(
			"Path : "
				+ s
				+ "\nFichier : "
				+ f.toString()
				+ "\nTaille : "
				+ f.length()
				+ "\toctets");
	}
}