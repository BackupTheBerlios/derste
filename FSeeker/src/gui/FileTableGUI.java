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

    private FileTableModel m = null;

    public FileTableGUI(FileTableModel m) {
        super(m);
        this.m = m;

        getTableHeader().setReorderingAllowed(false);

        FileTableCellRenderer renderer = new FileTableCellRenderer();
        this.setDefaultRenderer(Object.class, renderer);
        this.setDefaultRenderer(Long.class, renderer);
        this.setDefaultRenderer(Date.class, renderer);
        this.setDefaultRenderer(File.class, renderer);

        // propriétés de la JTable
        setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Fixe la taille des lignes et colonnes
        setRowHeight(ROWHEIGHT);
        packColumns(this, ROWMARGIN);

        setDragEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        FileTableControler ftc = new FileTableControler(m);
        addMouseListener(ftc);
        addKeyListener(ftc);
    }

   
    
    /* Appel packColum sur toutes les colonne */
    public void packColumns(JTable table, int margin) {
        for (int c = 0; c < table.getColumnCount(); c++) {
            packColumn(table, c, 2);
        }
    }

    // Sets the preferred width of the visible column specified by vColIndex.
    // The column
    // will be just wide enough to show the column head and the widest cell in
    // the column.
    // margin pixels are added to the left and right
    // (resulting in an additional width of 2*margin pixels).
    public void packColumn(JTable table, int vColIndex, int margin) {
        TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
                .getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;

        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(table, col
                .getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        // Get maximum width of column data
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(table, table
                    .getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        // Add margin
        width += 2 * margin;

        // Set the width
        col.setPreferredWidth(width);
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

        JScrollPane jsp = new JScrollPane(vue, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
       
        frame.getContentPane().add(jsp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}