/*
 * Créé le 20 oct. 2004
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
 * @author aitelhab
 *  
 */
public class FileTableGUI extends JTable /*implements Observer*/ {

	//protected TableModel m = null;

	public FileTableGUI(FileTableModel m) {
		super(m);
		//this.m = m;
		//m.addObserver(this);
		
		getTableHeader().setReorderingAllowed(false);
		
		setDefaultRenderer(Object.class, new FileTableCellRenderer());
		
		setShowGrid(false);
		
		//setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		//setRowHeight(25);
		//setRowMargin(5);
		
		setDragEnabled(true);
		
		//setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//setCellSelectionEnabled(false);
		//setLayout(new FlowLayout());
		
		addMouseListener(new TableModelControler(m));
	}

    Color normal = getBackground();
	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        
        // Have fun !
        if (row % 2 == 0 && !isCellSelected(row, column)) {
            c.setBackground(Color.LIGHT_GRAY);
        } else {
            c.setBackground(normal);
        }
        
        if (column % 2 == 0 && row % 2 == 1 && !isCellSelected(row, column)) {
            c.setBackground(Color.yellow);
        } else {
            // If not shaded, match the table's background
            c.setBackground(normal);
        }
        
        return c;
	}
	
	/*public void update(Observable o, Object arg) {
		System.out.println("update() de la JTable appelé");
		//Mettre à jour la vue
		revalidate();
		repaint();
	}*/

	public static void main(String[] args) {
		FSeekerModel fsm = new FSeekerModel(new File("/home/brahim/"));
		FileTableModel model = new FileTableModel(fsm);
		FileTableGUI vue = new FileTableGUI(model);
		JFrame frame = new JFrame("Test vue détail");

		JScrollPane jsp = new JScrollPane(vue);
		frame.getContentPane().add(jsp);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}