/*
 * Créé le 20 oct. 2004
 *
 */
package gui;

import javax.swing.*;
import javax.swing.table.*;
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
	
	private DefaultTableModel m;
	
	public FileTableGUI(FileTableModel m){
		this.m = m.getModel();
		this.setDefaultRenderer(Object.class, new FileTableCellRenderer());
		setShowGrid(false);
		
		setModel(this.m);			
	}
	
	public void update(Observable o, Object arg){
		//Mettre à jour la vue
	}
	
	public static void main(String [] args){		
		FileTableModel model = new FileTableModel(new File("/home/brahim/"), "Colonne Fichier");
		FileTableGUI vue = new FileTableGUI(model);
		JFrame frame = new JFrame("Test vue détail");
		
		JScrollPane jsp = new JScrollPane(vue);
		frame.getContentPane().add(jsp);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
}
