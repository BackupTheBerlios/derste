/*
 * Created on 14 oct. 2004
 */
package gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;

import misc.TreeUtilities;
import misc.file.FileUtilities;
import model.FileSystemTreeModel;
import renderer.FileSystemTreeCellRenderer;
import controler.FileSystemTreeControler;

/**
 * Repr�sente une vue sous forme d'arbre d'un syst�me de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeGUI extends JTree {

	/** Le mod�le associ� � cette vue */
	protected FileSystemTreeModel m = null;

	/**
	 * Construit une vue de syst�me de fichiers en arbre, � partir d'un mod�le.
	 * 
	 * @param m
	 *            le mod�le en question
	 */
	public FileSystemTreeGUI(FileSystemTreeModel m) {
		this.m = m;

		setModel(m);
		setDirectory(m.getModel().getURI());
		setEditable(true);
		setCellRenderer(new FileSystemTreeCellRenderer());

		// Par d�faut, le JTree ne s'enregistre pas
		ToolTipManager.sharedInstance().registerComponent(this);

		FileSystemTreeControler fstc = new FileSystemTreeControler(m, this);
		addTreeSelectionListener(fstc);
		addKeyListener(fstc);
		m.addURIChangedListener(fstc);
	}

	/**
	 * Positionne la s�lection dans l'arbre sur le fichier <code>dir</code>.
	 * 
	 * @param dir
	 *            le fichier � s�lectionner dans l'arbre
	 */
	public void setDirectory(File dir) {
		TreePath tp = getSelectionPath();
		// Pas besoin de setter si on y est d�j� ! (si on a provoqu� l'�v�nement
		// quoi)
		if (tp == null || !tp.equals(TreeUtilities.getTreePath(dir)))
			setSelectionPath(TreeUtilities.getTreePath(dir));
	}

	/**
	 * Affiche le tooltip �valu� dynamiquement. Appel�e automatiquement par
	 * Swing.
	 * 
	 * @param event
	 *            l'�v�nement de souris associ�
	 */
	public String getToolTipText(MouseEvent event) {
		Point clic = event.getPoint();
		if (getRowForLocation(clic.x, clic.y) != -1) {
			File f = (File) getPathForLocation(clic.x, clic.y)
					.getLastPathComponent();
			return FileUtilities.getToolTip(f);
		}

		return null;
	}

}