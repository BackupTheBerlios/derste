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
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;

import misc.ImagesMap;
import misc.file.CompareByLastModified;
import misc.file.CompareByName;
import misc.file.CompareBySize;
import misc.file.CompareByType;
import misc.file.FileUtilities.FileDetails;
import model.FileTableModel;

/**
 * Classe de rendu graphique pour la vue de détails.
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableCellRenderer extends DefaultTableCellRenderer {

	/** Le modèle associé */
	protected FileTableModel m = null;

	/** La couleur de la colonne triée */
	protected Color sortColor = new Color(240, 220, 200);

	/**
	 * Construit un renderer.
	 * 
	 * @param m
	 *            le modèle associée à la vue
	 */
	public FileTableCellRenderer(FileTableModel m) {
		this.m = m;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		if (!isSelected) {
			Color c = table.getBackground();

			if ((row % 2) == 0)
				setBackground(new Color(c.getRGB() - 5460)); //Couleur perso !
			else
				setBackground(c);

			if (m.getMode() == FileTableModel.SIMPLE_MODE) {
				if (m.getModel().getComparator() == CompareByName.get()
						&& column == 0)
					setBackground(sortColor);
				else if (m.getModel().getComparator() == CompareBySize.get()
						&& column == 1)
					setBackground(sortColor);
				else if (m.getModel().getComparator() == CompareByType.get()
						&& column == 2)
					setBackground(sortColor);
				else if (m.getModel().getComparator() == CompareByLastModified
						.get()
						&& column == 3)
					setBackground(sortColor);
			}
		}

		return this;
	}

	public void setValue(Object value) {
		super.setValue(value);

		if (value != null) {

			if (value instanceof File) {
				File file = (File) value;
				FileDetails fd = new FileDetails(file);
				setIcon(ImagesMap.get16x16(file));
				setToolTipText(fd.getToolTip());
				setFont(fd.getFont());
				setHorizontalAlignment(JLabel.LEFT);
				setText(FileSystemView.getFileSystemView()
						.getSystemDisplayName(file));

			} else if (value instanceof Date) {
				setHorizontalAlignment(JLabel.LEFT);
				setText(FileDetails.dateFormat.format((Date) value));
				setIcon(null);

			} else {
				setHorizontalAlignment(JLabel.CENTER);
				setText(value.toString());
				setIcon(null);
			}

		} else {
			// Si la cellule représente le nihilisme totale du monde, on la
			// représente comme telle !
			setIcon(null);
			setHorizontalAlignment(JLabel.LEFT);
		}
	}

}