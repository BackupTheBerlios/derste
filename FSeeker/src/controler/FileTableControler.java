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
 * R�cup�re les �v�nements d'une vue de table, et les associe � un mod�le de
 * table.
 * 
 * @author brahim
 * @author Sted
 */
public class FileTableControler extends MouseAdapter implements KeyListener,
        ListSelectionListener, SelectionChangedListener {

    /** Le mod�le associ� */
    protected FileTableModel m = null;

    protected FileTableGUI gui = null;

    /**
     * Construit un contr�leur � partir d'un mod�le.
     * 
     * @param m
     *            un mod�le de table
     */
    public FileTableControler(FileTableModel m, FileTableGUI gui) {
        this.m = m;
        this.gui = gui;
    }

    /**
     * Quand on clique, ca ouvre le dossier si on est positionn� sur un dossier.
     * 
     * @param e
     *            l'�v�nement associ�
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
                // Le popup � l'ext�rieur des �l�ments
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
     * Retourne l'�l�ment s�lectionn� (le fichier p�re), et si le mod�le est en
     * mode sp�cial, il met � jour la derni�re colonne s�lectionn�e.
     * 
     * @param e
     *            l'�v�nement associ�
     * @return l'objet s�lectionn� ou null si aucun
     */
    private Object getSelectedObject(InputEvent e) {
        JTable source = (JTable) e.getSource();
        int lg = source.getSelectedRow();
        int col = source.getSelectedColumn();
        if (lg == -1 || col == -1)
            return null;

        // Si on est en mode sp�cial on fixe la valeur de la colonne
        // s�lectionn�e
        if (m.getMode() == FileTableModel.SPECIAL_MODE)
            m.setSelectedColumn(((JTable) e.getSource()).getSelectedColumn());

        return source.getValueAt(lg, col);
    }

    /**
     * Si une touche a �t� appuy�.
     * 
     * @param e
     *            l'�v�nement associ�
     */
    public void keyPressed(KeyEvent e) {
        Object o = getSelectedObject(e);
        if (o instanceof File)
            GeneralControler.keyPressed(e, (File) o, m.getModel());
    }

    /**
     * La s�lection du mod�le a �t� modifi�, on met � jour la vue
     * TODO g�rer la selection pour la vue speciale
     * @param e
     *            L'�venement associ�
     */
    public void selectionChanged(SelectionChangedEvent e) {
        int lg = gui.getSelectedRow();
        int col = gui.getSelectedColumn();

        FileTableModel m = (FileTableModel) e.getSource();

        //Nous sommes bien en mode simple (vue d�tail)
        if (m.getMode() == FileTableModel.SIMPLE_MODE) {
            //Si pas de s�l�ction, on selectionne
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
                //Idem que la selection courante pas la peine de mettre � jour
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
     * La s�lection de la vue a �t� modifi�, on met � jour le mod�le.
     * 
     * @param e
     *            l'�v�nement associ�
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

            //On met � jour le mod�le
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