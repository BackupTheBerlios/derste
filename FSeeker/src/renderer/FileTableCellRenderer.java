/*
 * Créé le 20 oct. 2004
 *
 * Classe de rendu graphique pour une JTable
 */
package renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;

import java.awt.Dimension;

import java.text.SimpleDateFormat;

//import javax.swing.UIManager;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.util.*;

import javax.swing.table.DefaultTableCellRenderer;

//import misc.FileUtilities;

import misc.ImagesMap;

//import java.util.*;

//import misc.file.FileUtilities;

/**
 * 
 * @author aitelhab
 */
public class FileTableCellRenderer extends DefaultTableCellRenderer {
    

 
   //TODO Retirer de cette classe (dans misc car utilisé ailleurs, à voir ?)
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEEEEEEE dd MMMMMMMMM 'à' hh:mm:ss", Locale
                    .getDefault());
    
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (!isSelected) {
            Color c = table.getBackground();
            if ((row % 2) == 0 && c.getRed() > 20 && c.getGreen() > 20
                    && c.getBlue() > 20)
                setBackground(new Color(c.getRed() - 20, c.getGreen() - 20, c
                        .getBlue() - 20));
            else
                setBackground(c);
        }      
          
        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
    }

    public void setValue(Object value) {
        if (value != null) {
            if (value instanceof File) {
                File file = (File) value;
                Icon fileIcon = ImagesMap.get16x16(file);
                setHorizontalAlignment(JLabel.LEFT);               
                setIcon(fileIcon);
                setForeground(Color.black);                
                setToolTipText(null);//FileUtilities.getDetails(file)); 
                super.setValue(file.getName() + (file.isDirectory() ? "/" : ""));
                return ;
            } else if (value instanceof Date) {              
               String newValue = dateFormat.format((Date)value);
               setHorizontalAlignment(JLabel.LEFT);
               setForeground(Color.black);
               setIcon(null);
               super.setValue(newValue);
            } else if (value instanceof Date) {
                setIcon(null);
                setForeground(Color.black);
                setHorizontalAlignment(JLabel.CENTER);
                super.setValue(value+ " octets ");
            } else {
                setIcon(null);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(Color.black);
                super.setText(value.toString());
            }
        }
        
    }

}