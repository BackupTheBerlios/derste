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
 * Contr�le et effectue les changements de la vue et du mod�le associ�.
 * 
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeControler extends MouseAdapter implements
        TreeSelectionListener, KeyListener, URIChangedListener {

    /**
     * Retourne le popup d'un fichier s�lectionn� ou null si ce fichier est
     * null.
     * 
     * @param f
     *            un fichier (normalement, celui en s�lection)
     * @return le popup
     */
    public JPopupMenu getPopup(final File f) {
        if (f == null)
            return null;

        // Le popup
        JPopupMenu popup = new JPopupMenu();

        // Pour cr�er plus facilement et clairement des popups
        PopupManager pm = new PopupManager(f, m.getModel());

        popup.add(pm.getFileName());
        popup.addSeparator();

        // Sp�cial au JTree
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
        m.getModel().setURI(f, e.getSource());
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

    /**
     * G�re le clic droit dans l'arbre.
     * 
     * @param e
     *            l'�v�nement associ�
     */
    public void mouseClicked(MouseEvent e) {
        // On v�rifie qu'il s'agit du click droit
        if (SwingUtilities.isRightMouseButton(e)) {

            JTree source = (JTree) e.getSource();

            // On s�lectionne le noeud o� on l'utilisateur a cliqu�
            TreePath path = source.getPathForLocation(e.getX(), e.getY());
            source.setSelectionPath(path);

            // On r�cup la s�lection
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