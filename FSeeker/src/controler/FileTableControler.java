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
public class FileTableControler extends MouseAdapter {
    protected FileTableModel m = null;

    public FileTableControler(FileTableModel m) {
        this.m = m;
    }

    public void mouseClicked(MouseEvent e) {        
        
        if (e.getClickCount() == 1 && m.getMode() == FileTableModel.SPECIAL_MODE || e.getClickCount() == 2)
            setURI(e); 
            
     
    }
    
    protected void setURI(InputEvent e){
        File f = null;
        JTable source = (JTable) e.getSource();
        Object value = source.getValueAt(source.getSelectedRow(), source
                .getSelectedColumn());
        
        //Si on est en mode spécial on fixe la valeur de la colonne séléctionnée
        if(m.getMode() == FileTableModel.SPECIAL_MODE)
            m.setSelectedColumn(source.getSelectedColumn());
        
        //System.out.println("Value = "+value+" Class = "+value.getClass());
        
        //On modifie le méta-model pour mettre à jour l'arborescence
        if (value instanceof File)
            f = (File) value;
        if (f != null) {
            if (f.isDirectory()) {
    			if (f.canRead())
    				m.getModel().setURI(f, e.getSource());
    			else
    				GU.warn("Vous n'avez pas accès à ce dossier.");
    		}
        }
    }

    /**
     * Méthode de Test
     * @param args
     */
    public static void main(String[] args) {
    }
}