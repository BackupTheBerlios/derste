/*
 * Created on 16 oct. 2004
 */
package controler;

import gui.FileSystemTreeGUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.sun.corba.se.spi.orbutil.fsm.FSM;

import misc.TreeUtilities;
import model.FSeekerModel;
import model.FileSystemTreeModel;

/**
 * Contr�le et effectue les changements de la vue et du mod�le associ�.
 * 
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeControler implements TreeSelectionListener,
		KeyListener, URIChangedListener {

	/** Le mod�le associ� */
	protected FileSystemTreeModel m = null;

	/** La vue associ� */
	protected FileSystemTreeGUI gui = null;

	/**
	 * Construit un contr�leur � partir d'un mod�le et d'une vue.
	 * 
	 * @param m
	 *            un mod�le
	 * @param gui
	 *            une vue
	 */
	public FileSystemTreeControler(FileSystemTreeModel m, FileSystemTreeGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Appel�e quand la s�lection dans la vue change.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void valueChanged(TreeSelectionEvent e) {
		File f = (File) e.getPath().getLastPathComponent();
		m.getModel().setURI(f, gui);
	}

	/**
	 * Appel� quand des touches sont press�s dans la vue.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void keyPressed(KeyEvent e) {
		GeneralControler.keyPressed(e, m.getModel().getURI(), m.getModel());
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Appel�e quand le mod�le a �t� modifi�. (donc, quand le supra-mod�le l'a
	 * �t�)
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void URIChanged(URIChangedEvent e) {
		if (m.getModel().isChanged(FSeekerModel.URI))
			gui.setDirectory(m.getModel().getURI());
	}

}