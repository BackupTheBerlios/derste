package controler;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import misc.GU;
import model.ListImagesModel;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesDataControler extends MouseAdapter implements
        KeyListener, ListDataListener {

    /** Le mod�le de liste � contr�ler */
    protected ListImagesModel m = null;;

    /**
     * Construit le contr�leur associ� au mod�le <code>m</code>.
     * 
     * @param m
     *            le mod�le
     */
    public ListImagesDataControler(ListImagesModel m) {
        this.m = m;
    }

    /**
     * Modifie l'URI courante. A partir d'un �v�nement, elle r�cup�re la source
     * (file) sur laquelle on a agit, et la d�fini comme nouvelle URI du
     * supra-mod�le.
     * 
     * @param e
     *            l'�v�nement (souris ou clavier)
     */
    protected void setURI(InputEvent e) {
        File f = (File) ((JList) e.getSource()).getSelectedValue();
        if (f.isDirectory()) {
            if (f.canRead())
                m.getModel().setURI(f, e.getSource());
            else
                GU.message("Vous n'avez pas acc�s � ce dossier.");
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2)
            setURI(e);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            setURI(e);
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void contentsChanged(ListDataEvent e) {}

    public void intervalAdded(ListDataEvent e) {}

    public void intervalRemoved(ListDataEvent e) {}

}