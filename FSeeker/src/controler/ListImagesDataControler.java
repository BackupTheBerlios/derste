package controler;

import gui.ListImagesGUI;

import java.awt.Point;
import java.awt.Rectangle;
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

    /** Le modèle de liste à contrôler */
    protected ListImagesModel m = null;
    
    /** La vue associée */
    protected ListImagesGUI gui = null;

    /**
     * Construit le contrôleur associé au modèle <code>m</code>.
     * 
     * @param m
     *            le modèle
     */
    public ListImagesDataControler(ListImagesModel m, ListImagesGUI gui) {
        this.m = m;
        this.gui = gui;
    }

    /**
     * Modifie l'URI courante. A partir d'un événement, elle récupère la source
     * (file) sur laquelle on a agit, et la défini comme nouvelle URI du
     * supra-modèle.
     * 
     * @param e
     *            l'événement (souris ou clavier)
     */
    protected void setURI(InputEvent e) {
        File f = (File) ((JList) e.getSource()).getSelectedValue();
        if (f.isDirectory()) {
            if (f.canRead())
                m.getModel().setURI(f, e.getSource());
            else
                GU.info("Vous n'avez pas accès à ce dossier.");
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2)
            setURI(e);
    }
    
    public void mousePressed(MouseEvent e) {
        // TODO System.out.println(e.getButton());
        if (e.getButton() == MouseEvent.BUTTON2_DOWN_MASK) {
            JList list = (JList) e.getSource();
            //TOUT LE MONDE n'a pas la 1.5
            //JPopupMenu popup = list.getComponentPopupMenu();
            
            Point clic = e.getPoint();
            int index = list.locationToIndex(clic);
            Rectangle r = list.getCellBounds(index, index);
            if (r.contains(clic)) {
                //popup.add(new JMenuItem("salut"));
                
            }
        }
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            setURI(e);
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            m.getModel().gotoParent();
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void contentsChanged(ListDataEvent e) {
        gui.ensureIndexIsVisible(0);
        gui.setSelectedIndex(0);
    }

    public void intervalAdded(ListDataEvent e) {}

    public void intervalRemoved(ListDataEvent e) {}

}