/**
 * 
 * Classe qui permet la repr�sentation graphique d'un syst�me de fichier 
 * sous forme d'une vue en table (vue d�tails)
 * 
 */
package model;

import javax.swing.table.*;
import java.util.*;
import java.io.*;

/**
 * @author aitelhab
 *
 * TODO h�riter de DefaultTableModel dans une classe sup�rieure et de 
 * Observable dans une sous classe pour ne pas avoir de getModel
 */
public class FileTableModel extends Observable {
	
	private  DefaultTableModel model;
	private String columnname;
	private File file ;
	
	public FileTableModel(File file, String columnName){	
		this.file = file;
		this.columnname = columnName;
		File [] files = file.listFiles();		
		model = new DefaultTableModel();		
		model.addColumn(columnName, files);
	}
	
	
	public void setFile(File file){
	    this.file = file;
	}
	
	public DefaultTableModel getModel(){
		return this.model;
	}

}
