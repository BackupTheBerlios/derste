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
 * Contrôle et effectue les changements de la vue et du modèle associé.
 * 
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeControler implements TreeSelectionListener,
		KeyListener, URIChangedListener {

	/** Le modèle associé */
	protected FileSystemTreeModel m = null;

	/** La vue associé */
	protected FileSystemTreeGUI gui = null;

	/**
	 * Construit un contrôleur à partir d'un modèle et d'une vue.
	 * 
	 * @param m
	 *            un modèle
	 * @param gui
	 *            une vue
	 */
	public FileSystemTreeControler(FileSystemTreeModel m, FileSystemTreeGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Appelée quand la sélection dans la vue change.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void valueChanged(TreeSelectionEvent e) {
		File f = (File) e.getPath().getLastPathComponent();
		m.getModel().setURI(f, gui);
	}

	/**
	 * Appelé quand des touches sont pressés dans la vue.
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void keyPressed(KeyEvent e) {
		GeneralControler.keyPressed(e, m.getModel().getURI(), m.getModel());
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Appelée quand le modèle a été modifié. (donc, quand le supra-modèle l'a
	 * été)
	 * 
	 * @param e
	 *            l'événement associé
	 */
	public void URIChanged(URIChangedEvent e) {
		if (m.getModel().isChanged(FSeekerModel.URI))
			gui.setDirectory(m.getModel().getURI());
	}

}