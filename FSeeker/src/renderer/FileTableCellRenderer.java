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

import misc.FileDetails;
import misc.ImagesMap;

/**
 * 
 * @author aitelhab
 * @author Sted  
 */
public class FileTableCellRenderer extends DefaultTableCellRenderer {

	public FileTableCellRenderer() {
		//super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table
				.getCellRenderer(row, column);

		Color c = table.getBackground();
		//Couleur à personaliser ,on verra ca plus tard
		if ((row % 2) == 0 && c.getRed() > 40 && c.getGreen() > 30
				&& c.getBlue() > 10)
			setBackground(new Color(c.getRed() - 40, c.getGreen() - 30, c
					.getBlue() - 10));
		else
			setBackground(c);

		setValue(value);

		return renderer;
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
			//JLabel lab = new JLabel();
			Icon fileIcon = ImagesMap.getImage(file);
			//lab.setIcon(fileIcon);
			setIcon(fileIcon);
			super.setValue(file.getName() + (file.isDirectory() ? "/" : ""));
			setToolTipText(FileDetails.getDetails(file));
			//super.setValue( file.getName() ) ;
		}
	}

}