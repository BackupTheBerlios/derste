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
 * Représente une vue sous forme d'arbre d'un système de fichiers.
 * 
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeGUI extends JTree {

	/** Le modèle associé à cette vue */
	protected FileSystemTreeModel m = null;

	/**
	 * Construit une vue de système de fichiers en arbre, à partir d'un modèle.
	 * 
	 * @param m
	 *            le modèle en question
	 */
	public FileSystemTreeGUI(FileSystemTreeModel m) {
		this.m = m;

		setModel(m);
		setDirectory(m.getModel().getURI());
		setEditable(true);
		setCellRenderer(new FileSystemTreeCellRenderer());

		// Par défaut, le JTree ne s'enregistre pas
		ToolTipManager.sharedInstance().registerComponent(this);

		FileSystemTreeControler fstc = new FileSystemTreeControler(m, this);
		addTreeSelectionListener(fstc);
		addKeyListener(fstc);
		m.addURIChangedListener(fstc);
	}

	/**
	 * Positionne la sélection dans l'arbre sur le fichier <code>dir</code>.
	 * 
	 * @param dir
	 *            le fichier à sélectionner dans l'arbre
	 */
	public void setDirectory(File dir) {
		TreePath tp = getSelectionPath();
		// Pas besoin de setter si on y est déjà ! (si on a provoqué l'événement
		// quoi)
		if (tp == null || !tp.equals(TreeUtilities.getTreePath(dir)))
			setSelectionPath(TreeUtilities.getTreePath(dir));
	}

	/**
	 * Affiche le tooltip évalué dynamiquement. Appelée automatiquement par
	 * Swing.
	 * 
	 * @param event
	 *            l'événement de souris associé
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