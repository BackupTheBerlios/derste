package renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;

import misc.FileUtilities;
import misc.ImagesMap;

/**
 * @author sted
 */

public class ListImagesCellRenderer extends DefaultListCellRenderer {

	protected boolean simple = false;

	public ListImagesCellRenderer() {
	}

	public ListImagesCellRenderer(boolean simple) {
		this.simple = simple;
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);

		// Penser à trouver un système pour reconnaître le parent (ie: "..") et
		// le nommer comme tel. Peut être un attribut dans le model ? (passer
		// par le contrôleur !!)

		File file = (File) value;
		setText(file.getName() + (file.isDirectory() ? "/" : ""));

		setIcon(ImagesMap.getImage(file));
		setToolTipText(FileUtilities.getDetails(file));

		if (!simple) {
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		return this;
	}
}

