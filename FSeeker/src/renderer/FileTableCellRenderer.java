/*
 * Créé le 20 oct. 2004
 */
package renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import misc.ImagesMap;

/**
 * Classe de rendu graphique pour une JTable.
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableCellRenderer extends JLabel implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

    	if (value == null)
    		return this;
    	
        if (value instanceof File) {
        	File f = (File) value;
        	setText(f.getName());
        } else {       
        	setText(value.toString());
        }
        
        return this;
    }

}


class FileTableCellRenderer2 extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		/*DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table
				.getCellRenderer(row, column);*/

		System.out.println("prout");
		
		if (value == null)
			return this;
		
		File f = (File) value;
		
		/*if (file != null) {
			Color c = table.getBackground();
			//Couleur à personaliser ,on verra ca plus tard
			if ((column == 0) && (row % 2) == 0 && c.getRed() > 30
					&& c.getGreen() > 20 && c.getBlue() > 20)
				setBackground(new Color(c.getRed() - 30, c.getGreen() - 20, c
						.getBlue() - 20));
			else
				setBackground(c);
		}*/

		/*if (isSelected) {
			setBackground(new Color(115, 118, 113));

			//setForeground(Color.white);
		}*/

		/*if (value != null)
			setValue(value);*/

		setIcon(ImagesMap.get16x16(f));
		setValue(f.getName()  + (f.isDirectory() ? File.separator : ""));
	
		return this;
	}

	public void validate() {}
    public void revalidate() {}
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
	
	/**
	 * Surcharge de la méthode setValue avec traitement différent en fonction de
	 * la classe de l'objet value
	 * 
	 * @param value
	 *            l'objet dont on veut personaliser l'affichage
	 */
	/*public void setValue(Object value) {

		Class cl = value.getClass();
		if (cl == File.class) {
			File file = (File) value;

			Icon fileIcon = ImagesMap.get16x16(file);
			setIcon(fileIcon);

			setValue(file.getName() /* + (file.isDirectory() ? "/" : "") *///);
		/*	setToolTipText(null);//FileUtilities.getDetails(file));
		} else
			super.setValue(value);

	}*/

}