/*
 * Created on 19 oct. 2004
 */
package renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

import misc.ImagesMap;

/**
 * Renderer personnalis� du syst�me de fichier repr�sent� en arbre, pour
 * rajouter des ic�nes.
 * 
 * @author Sted
 */
public class FileSystemTreeCellRenderer extends DefaultTreeCellRenderer {

	protected FileSystemView sys = FileSystemView.getFileSystemView();

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);

		if (value == null)
			return this;

		setText(sys.getSystemDisplayName((File) value));

		if (expanded)
			setIcon(ImagesMap.get16x16(ImagesMap.DIRECTORY_OPENED_IMAGE));
		else
			setIcon(ImagesMap.get16x16(ImagesMap.DIRECTORY_CLOSED_IMAGE));

		return this;
	}
}