/*
 * Created on 27 oct. 2004
 *
 *
 */
package controler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.JTable;

import misc.GU;
import model.*;

/**
 * @author brahim
 *  
 */
public class TableModelControler extends MouseAdapter {
    private FileTableModel m = null;;

    public TableModelControler(FileTableModel m) {
        this.m = m;
    }

    public void mouseClicked(MouseEvent e) {        
        if (e.getClickCount() == 2) {
           setURI(e);
        }
        //}else if (e.getClickCount() == 4) {
        //    System.out.println("4 clicks");
       // }
        //if (e.getClickCount() == 2) 
        //       System.out.println("2 clicks");
    }
    
    public void setURI(InputEvent e){
        File f = null;
        JTable source = (JTable) e.getSource();
        Object value = source.getValueAt(source.getSelectedRow(), source
                .getSelectedColumn());
        //System.out.println("Value = "+value+" Class = "+value.getClass());
        if (value instanceof File)
            f = (File) value;
        if (f != null) {
            if (f.isDirectory()) {
    			if (f.canRead())
    				m.getModel().setURI(f, e.getSource());
    			else
    				GU.message("Vous n'avez pas accès à ce dossier.");
    		}
        }
    }

    public static void main(String[] args) {
    }
}