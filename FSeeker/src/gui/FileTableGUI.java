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
 * @author aitelhab
 *
 */
public class FileTableGUI extends JTable implements Observer{
	
	private TableModel m;
	
	public FileTableGUI(FileTableModel m){
		this.m = m.getTableModel();
		m.addObserver(this);
		this.setDefaultRenderer(Object.class, new FileTableCellRenderer());
		setShowGrid(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);	
		//setRowHeight(25); 
		//setRowMargin(5); 
		setDragEnabled(true);		
		//setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//setCellSelectionEnabled(true); 
        setLayout(new FlowLayout());
		addMouseListener(new TableModelControler(m));
		setModel(this.m);			
	}
	
	public void update(Observable o, Object arg){
	    System.out.println("update() de la JTable appelé");
		//Mettre à jour la vue
	    revalidate();
        repaint();
	}
	
	public static void main(String [] args){
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
