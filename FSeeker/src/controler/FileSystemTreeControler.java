/*
 * Created on 16 oct. 2004
 */
package controler;

import gui.FileSystemTreeGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import misc.PopupManager;

import misc.TreeUtilities;
import model.FSeekerModel;
import model.FileSystemTreeModel;

/**
 * Contrôle et effectue les changements de la vue et du modèle associé.
 * 
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeControler extends MouseAdapter implements
        TreeSelectionListener, KeyListener, URIChangedListener {

    /**
     * Retourne le popup d'un fichier sélectionné ou null si ce fichier est
     * null.
     * 
     * @param f
     *            un fichier (normalement, celui en sélection)
     * @return le popup
     */
    public JPopupMenu getPopup(final File f) {
        if (f == null)
            return null;

        // Le popup
        JPopupMenu popup = new JPopupMenu();

        // Pour créer plus facilement et clairement des popups
        PopupManager pm = new PopupManager(f, m.getModel());

        popup.add(pm.getFileName());
        popup.addSeparator();

        // Spécial au JTree
        if (gui.isExpanded(TreeUtilities.getTreePath(f)))
            popup.add(PopupManager.createMenuItem("Fermer",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            gui.collapsePath(TreeUtilities.getTreePath(f));
                        }
                    }));
        else
            popup.add(PopupManager.createMenuItem("Ouvrir",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            gui.expandPath(TreeUtilities.getTreePath(f));
                        }
                    }));

        popup.addSeparator();
        popup.add(pm.getCreateDirectory());
        popup.add(pm.getCreateFile());
        popup.addSeparator();
        popup.add(pm.getProperties());

        return popup;
    }

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
        m.getModel().setURI(f, e.getSource());
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

    /**
     * Gère le clic droit dans l'arbre.
     * 
     * @param e
     *            l'événement associé
     */
    public void mouseClicked(MouseEvent e) {
        // On vérifie qu'il s'agit du click droit
        if (SwingUtilities.isRightMouseButton(e)) {

            JTree source = (JTree) e.getSource();

            // On sélectionne le noeud où on l'utilisateur a cliqué
            TreePath path = source.getPathForLocation(e.getX(), e.getY());
            source.setSelectionPath(path);

            // On récup la sélection
            File f = null;
            if (path != null)
                f = (File) path.getLastPathComponent();

            // On affiche le popup
            PopupManager.showPopup(e, getPopup(f));
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}