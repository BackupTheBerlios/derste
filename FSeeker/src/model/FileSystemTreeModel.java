package model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import misc.GU;
import misc.file.CompareByName;
import misc.file.FileUtilities;

/**
 * Représente un système de fichier sous forme d'arbre, en ne prenant en compte
 * que les répertoires.
 * 
 * @author brahim
 * @author Sted
 */
public class FileSystemTreeModel extends DefaultURIModel implements TreeModel,
		Observer {

	/** Racine de l'arbre */
	protected File root = null;

	/** Liste des listeners associés à l'évènement TreeModelEvent */
	protected List listeners = new ArrayList();

	/** Contient le dernier dossier dont on a listé et trié les fichiers */
	protected File cachedFile = null;

	/** Contient la liste triés des fichiers du dernier dossier listé */
	protected File[] cachedFilesList = null;

	/** Permet de ne sélectionner que les répertoires */
	protected FileFilter filter = new FileFilter() {
		public boolean accept(File f) {
			return f.isDirectory() && (f.isHidden() ? fsm.showHidden() : true);
		}
	};

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

	/**
	 * Retourne le supra-modèle.
	 * 
	 * @return supra-modèle
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	/**
	 * Appelée quand le supra-modèle a été modifié.
	 */
	public void update(Observable o, Object arg) {
		fireURIChanged(this, getModel().getURI());
	}

	/**
	 * Retourne la liste triée des fichiers d'un répertoire, et la met en cache
	 * dans cachedFilesList, en gardant en mémoire le répertoire associé dans
	 * cachedFile.
	 * 
	 * @param f
	 *            le répertoire dont récupérer les fils triés
	 * @return cachedFilesList, la liste des fichiers dans f triés
	 */
	protected File[] getFilesListFrom(File f) {
		if (!f.isDirectory())
			return null;

		// Si on a déjà calculé le tout dans la version en cache
		// On la renvoie !
		if (f.equals(cachedFile))
			return cachedFilesList;

		// Sinon, on calcule, et on met en cache
		cachedFilesList = null;
		File[] files = f.listFiles(filter);

		if (files != null) {
			cachedFilesList = new File[files.length];
			cachedFile = f;

			for (int i = 0, j = 0; i < files.length; i++)
				if (!files[i].isHidden() || fsm.showHidden())
					cachedFilesList[j++] = files[i];

			Arrays.sort(cachedFilesList, CompareByName.get());
		}

		return cachedFilesList;
	}

	/**
	 * Construit un modèle d'arbre de fichiers.
	 * 
	 * @param fsm
	 *            le supra-modèle
	 */
	public FileSystemTreeModel(FSeekerModel fsm) {
		this.fsm = fsm;
		this.root = fsm.getURI();
		fsm.addObserver(this);
	}

	/**
	 * @inheritDoc
	 */
	public Object getRoot() {
		return root;
	}

	/**
	 * @inheritDoc
	 */
	public Object getChild(Object parent, int index) {
		File files[] = getFilesListFrom((File) parent);
		String[] children = FileUtilities.toStrings(files);

		if (children == null || index < 0 || index >= children.length)
			return null;

		return new File((File) parent, children[index]);
	}

	/**
	 * @inheritDoc
	 */
	public int getChildCount(Object parent) {
		if (parent == null)
			return 0;

		File[] foo = getFilesListFrom((File) parent);
		return (foo != null ? foo.length : 0);
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLeaf(Object node) {
		File[] children = ((File) node).listFiles(filter);
		return children == null || children.length == 0;
	}

	/**
	 * @inheritDoc
	 */
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == null || child == null)
			return -1;

		File[] files = getFilesListFrom((File) parent);
		if (files == null)
			return -1;

		for (int i = 0; i < files.length; i++)
			if (files[i].equals(child))
				return i;

		return -1;
	}

	/**
	 * Ajoute un listener à avertir lors d'un changement dans le modèle.
	 * 
	 * @param l
	 *            listener à ajouter
	 */
	public void addTreeModelListener(TreeModelListener l) {
		if (l != null && !listeners.contains(l))
			listeners.add(l);
	}

	/**
	 * Supprime un listener précédemment ajouté.
	 * 
	 * @param l
	 *            listener à supprimer
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		if (l != null)
			listeners.remove(l);
	}

	/**
	 * Appelée quand la valeur d'un noeud change.
	 * 
	 * @param path
	 *            le path de l'élément modifié
	 * @param value
	 *            sa nouvelle valeur
	 */
	public void valueForPathChanged(TreePath path, Object value) {
		File oldFile = (File) path.getLastPathComponent();
		String fileParentPath = oldFile.getParent();

		String newFileName = (String) value;
		File targetFile = new File(fileParentPath, newFileName);
		if (!oldFile.renameTo(targetFile)) {
			GU.warn("Impossible de renommer ce dossier.");
			return;
		}

		File parent = new File(fileParentPath);
		int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
		Object[] changedChildren = { targetFile };

		fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices,
				changedChildren);

		// Après un renommage de noeud, la sélection courante est ce noeud..!
		fireURIChanged(this, getModel().getURI());
	}

	/**
	 * Prévient les listeners associés à cet événement.
	 * 
	 * @param parentPath
	 *            le path père
	 * @param indices
	 *            les indices des éléments modifiés
	 * @param children
	 *            les éléments modifiés
	 */
	protected void fireTreeNodesChanged(TreePath parentPath, int[] indices,
			Object[] children) {
		TreeModelEvent ev = new TreeModelEvent(this, parentPath, indices,
				children);
		Iterator it = listeners.iterator();
		while (it.hasNext())
			((TreeModelListener) it.next()).treeNodesChanged(ev);
	}

}

