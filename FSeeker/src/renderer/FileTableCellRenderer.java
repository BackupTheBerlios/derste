/*
 * Créé le 20 oct. 2004
 */
package renderer;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import misc.ImagesMap;
import misc.file.FileUtilities.FileDetails;

/**
 * Classe de rendu graphique pour la vue de détails.
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		if (!isSelected) {
			Color c = table.getBackground();

			// Faudra que tu m'explique pour ces get*, ça fait pareil sans :D
			// && c.getRed() > 20 && c.getGreen() > 20 && c.getBlue() > 20
			if ((row % 2) == 0)
				setBackground(new Color(c.getRGB() - 5460)); // Mouahah
			else
				setBackground(c);
		}

		return this;
	}

	public void setValue(Object value) {

		if (value != null) {

			if (value instanceof File) {
				File file = (File) value;
				FileDetails fd = new FileDetails(file);
				setHorizontalAlignment(JLabel.LEFT);
				setIcon(ImagesMap.get16x16(file));
				setToolTipText(fd.getToolTip());
				setFont(fd.getFont());
				setText(fd.getName());

			} else if (value instanceof Date) {
				setHorizontalAlignment(JLabel.LEFT);
				setText(FileDetails.dateFormat.format((Date) value));
				setIcon(null);

			} else {
				setHorizontalAlignment(JLabel.CENTER);
				setText(value.toString());
				setIcon(null);
			}

		}
	}

}