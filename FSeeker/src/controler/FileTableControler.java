/*
 * Created on 27 oct. 2004
 */
package controler;

import gui.FileTableGUI;

//import java.awt.List;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
//import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import misc.PopupManager;
import model.FileTableModel;
import model.SelectionChangedEvent;
import model.SelectionChangedListener;

/**
 * Récupère les événements d'une vue de table, et les associe à un modèle de
 * table.
 * 
 * @author brahim
 * @author Sted
 */
public class FileTableControler extends MouseAdapter implements KeyListener,
        ListSelectionListener, SelectionChangedListener {

    /** Le modèle associé */
    protected FileTableModel m = null;

    protected FileTableGUI gui = null;

    /**
     * Construit un contrôleur à partir d'un modèle.
     * 
     * @param m
     *            un modèle de table
     */
    public FileTableControler(FileTableModel m, FileTableGUI gui) {
        this.m = m;
        this.gui = gui;
    }

    /**
     * Quand on clique, ca ouvre le dossier si on est positionné sur un dossier.
     * 
     * @param e
     *            l'événement associé
     */
    public void mouseClicked(MouseEvent e) {
        // Si on a un clic droit > popup
        if (SwingUtilities.isRightMouseButton(e)) {
            Object o = getSelectedObject(e);
            if (o != null) {
                File f = (File) o;
                PopupManager.showPopup(e, PopupManager.getDefaultPopupIn(f, m
                        .getModel()));
            } else {
                // Le popup à l'extérieur des éléments
                PopupManager.showPopup(e, PopupManager.getDefaultPopupOut(m
                        .getModel()));
            }
        }

        // Sinon, si c'est un gauche > ouverture
        else if (SwingUtilities.isLeftMouseButton(e)
                && e.getClickCount() == m.getModel().getClickCount()) {
            Object o = getSelectedObject(e);
            if (((File) o).isDirectory())
                m.getModel().setURI((File) o);
        }
    }

    /**
     * Retourne l'élément sélectionné (le fichier pére), et si le modèle est en
     * mode spécial, il met à jour la dernière colonne sélectionnée.
     * 
     * @param e
     *            l'événement associé
     * @return l'objet sélectionné ou null si aucun
     */
    private Object getSelectedObject(InputEvent e) {
        JTable source = (JTable) e.getSource();
        int lg = source.getSelectedRow();
        int col = source.getSelectedColumn();
        if (lg == -1 || col == -1)
            return null;

        // Si on est en mode spécial on fixe la valeur de la colonne
        // sélectionnée
        if (m.getMode() == FileTableModel.SPECIAL_MODE)
            m.setSelectedColumn(((JTable) e.getSource()).getSelectedColumn());

        return source.getValueAt(lg, col);
    }

    /**
     * Si une touche a été appuyé.
     * 
     * @param e
     *            l'événement associé
     */
    public void keyPressed(KeyEvent e) {
        Object o = getSelectedObject(e);
        if (o instanceof File)
            GeneralControler.keyPressed(e, (File) o, m.getModel());
    }

    /**
     * La sélection du modèle a été modifié, on met à jour la vue
     * TODO gérer la selection pour la vue speciale
     * @param e
     *            L'évenement associé
     */
    public void selectionChanged(SelectionChangedEvent e) {
        int lg = gui.getSelectedRow();
        int col = gui.getSelectedColumn();

        FileTableModel m = (FileTableModel) e.getSource();

        //Nous sommes bien en mode simple (vue détail)
        if (m.getMode() == FileTableModel.SIMPLE_MODE) {
            //Si pas de séléction, on selectionne
            if (lg == -1 && col == -1) {
                //int i ;
                //System.out.println("col = "+col);
                //System.out.println("last = "+(gui.getColumnCount()));

                //if(m.getMode() == FileTableModel.SIMPLE_MODE)
                int i = m.getIndexOf(e.getSelection(), 0);
                //else i = m.getIndexOf(e.getSelection(),
                // gui.getColumnCount()-2);

                gui.getSelectionModel().setSelectionInterval(i, i);

            } else if (lg != -1 && col != -1) { // Une selection est deja
                                                // presente
                //Idem que la selection courante pas la peine de mettre à jour
                // le gui
                if (!gui.getValueAt(lg, col).equals(e.getSelection())) {
                    //int i;
                    //if(m.getMode() == FileTableModel.SIMPLE_MODE)
                    int i = m.getIndexOf(e.getSelection(), 0);
                    // else i = m.getIndexOf(e.getSelection(),
                    // gui.getColumnCount()-1);
                    gui.getSelectionModel().setSelectionInterval(i, i);
                }
            }
        }
    }

    /**
     * La sélection de la vue a été modifié, on met à jour le modèle.
     * 
     * @param e
     *            l'événement associé
     */
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            //= (JTable) e.getSource();

            ListSelectionModel source = (ListSelectionModel) e.getSource();
            //System.out.println("min : "+);
            //System.out.println("max : "+src.getMaxSelectionIndex());
            //int lg = source.getSelectedRow();
            //int col = source.getSelectedColumn();

            int lg = source.getMinSelectionIndex();
            int col = gui.getSelectedColumn();//La colonne des fichiers a pour
                                              // indice 0

            if (lg == -1 || col == -1)
                return;

            //On met à jour le modéle
            //System.out.println("min : " + source.getMinSelectionIndex());
            Object o = gui.getValueAt(source.getMinSelectionIndex(), col);
            if (o instanceof File) {
                //System.out.println("(File)o : " + (File) o);
                if (!m.getModel().getSelection().equals(o))
                    m.getModel().setSelection((File) o, source);
            }
        }
    }




    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

}