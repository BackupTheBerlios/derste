package renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;

import misc.ImagesMap;

public class ListImagesCellRenderer extends DefaultListCellRenderer {

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);

		// Penser � trouver un syst�me pour reconna�tre le parent (ie: "..") et
		// le nommer comme tel. Peut �tre un attribut dans le model ? (passer
		// par le contr�leur !!)

		File file = (File) value;
		setText(file.getName() + (file.isDirectory() ? "/" : ""));

		setIcon(ImagesMap.getImage(file));
		setToolTipText("<html>Nom : " + file + "<br>Taille : " + file.length()
				+ " octets</html>");
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);

		return this;
	}
}

