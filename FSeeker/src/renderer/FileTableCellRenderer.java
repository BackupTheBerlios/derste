/*
 * Créé le 20 oct. 2004
 *
 * Classe de rendu graphique pour une JTable
 */
package renderer;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

//import misc.FileUtilities;

import misc.ImagesMap;
//import java.util.*;

//import misc.file.FileUtilities;


/**
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableCellRenderer extends DefaultTableCellRenderer {

    public FileTableCellRenderer() {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table
                .getCellRenderer(row, column);

        File file = null;
        if (value instanceof File && value != null)
            file = (File) value;
        if (file != null) {
            Color c = table.getBackground();
            //Couleur à personaliser ,on verra ca plus tard
            if ( (column == 0) && (row % 2) == 0 && c.getRed() > 30 && c.getGreen() > 20
                    && c.getBlue() > 20 )
                setBackground(new Color(c.getRed() - 30 , c.getGreen() - 20, c
                        .getBlue() - 20));
            else
                setBackground(c);
        }
        
        if(isSelected){
            setBackground(new Color(115,118, 113));
           
            //setForeground(Color.white);
        }

        if (value != null)
            setValue(value);

        return this;
    }

    /**
     * Surcharge de la méthode setValue avec traitement différent en fonction de
     * la classe de l'objet value
     * 
     * @param value
     *            l'objet dont on veut personaliser l'affichage
     */
    public void setValue(Object value) {

        Class cl = value.getClass();
        if (cl == File.class) {
            File file = (File) value;
            
            
                Icon fileIcon = ImagesMap.get16x16(file);
                setIcon(fileIcon); 
                          
            setValue(file.getName() /*+ (file.isDirectory() ? "/" : "")*/);
            setToolTipText(null);//FileUtilities.getDetails(file));            
        } else
            super.setValue(value);

    }

}