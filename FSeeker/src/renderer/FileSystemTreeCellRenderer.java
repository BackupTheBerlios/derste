/*
 * Created on 19 oct. 2004
 */
package renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import misc.ImagesMap;

/**
 * Renderer personnalisé du système de fichier représenté en arbre, pour rajouter
 * des icônes.
 * 
 * @author Sted
 */
public class FileSystemTreeCellRenderer extends DefaultTreeCellRenderer {

	protected Object root = null;
	
	public FileSystemTreeCellRenderer(Object root) {
		this.root = root;
	}
	
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded,
                leaf, row, hasFocus);
        
        if (value == null)
            return this;
                
        if (root != value)
        	setText(((File) value).getName());
        
        if (expanded)
            setIcon(ImagesMap.get16x16(ImagesMap.DIRECTORY_OPENED_IMAGE));
        else
            setIcon(ImagesMap.get16x16(ImagesMap.DIRECTORY_CLOSED_IMAGE));

        return this;
    }
}