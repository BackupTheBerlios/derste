/*
 * Créé le 20 oct. 2004
 *
 */
package gui;

import javax.swing.*;
import javax.swing.table.*;

import controler.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import model.*;
import renderer.*;

/**
 * Classe qui étends JTable en permettant la représentation sous forme d'un
 * tableau d'un systéme de fichier
 * 
 * @author aitelhab
 *  
 */
public class FileTableGUI extends JTable {

    private final static int ROWMARGIN = 5;

    private final static int ROWHEIGHT = 30;

    private TableModel m;

    public FileTableGUI(FileTableModel m) {
        super(m);
        this.m = m;

        getTableHeader().setReorderingAllowed(false);

        FileTableCellRenderer renderer = new FileTableCellRenderer();
        this.setDefaultRenderer(Object.class, renderer);
        this.setDefaultRenderer(Long.class, renderer);
        this.setDefaultRenderer(Date.class, renderer);
        this.setDefaultRenderer(File.class, renderer);

        //m.sortAllRows();//Tri des données

        // propriétés de la JTable
        setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setRowHeight(ROWHEIGHT);
        setRowMargin(ROWMARGIN);

        //Fixe la taille des colonnes
        initTableSize();// TODO ne pas coder en dur les valeurs de cette méthode
        setDragEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addMouseListener(new TableModelControler(m));

    }

    /**
     * A défaut de calculer la taille de l'objet placé dans une cellule on fixe
     * les valeurs
     */
    public void initTableSize() {
        TableColumn column = null;
        for (int i = 0; i < this.getColumnCount(); i++) {
            column = getColumnModel().getColumn(i);
            //          Attribut à mettre en final static à tester
            if (i == 3) {
                column.setPreferredWidth(140); //Taille pour la Date
            } else if (i == 1) {
                column.setPreferredWidth(100); //Taille pour la taille
            }

        }
    }

    /**
     * Méthode de test
     */
    public static void main(String[] args) {
        FSeekerModel fsm = new FSeekerModel(new File("/home/brahim/"));
        FileTableModel model = new FileTableModel(fsm,
                FileTableModel.SPECIAL_MODE);
        FileTableGUI vue = new FileTableGUI(model);
        JFrame frame = new JFrame("Test vue détail");

        JScrollPane jsp = new JScrollPane(vue);
        frame.getContentPane().add(jsp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}