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

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded,
                leaf, row, hasFocus);

        setText(((File) value).getName());
        
        if (expanded)
            setIcon(ImagesMap.get16x16(ImagesMap.DIRECTORY_OPENED_IMAGE));
        else
            setIcon(ImagesMap.get16x16(ImagesMap.DIRECTORY_CLOSED_IMAGE));

        return this;
    }
}