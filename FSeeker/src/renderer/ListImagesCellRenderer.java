package renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;

import misc.FileUtilities;
import misc.ImagesMap;
import model.ListImagesModel;

/**
 * @author sted
 */

public class ListImagesCellRenderer extends DefaultListCellRenderer {

    protected boolean simple = false;

    protected ListImagesModel m = null;

    public ListImagesCellRenderer(ListImagesModel m) {
        this(m, false);
    }

    public ListImagesCellRenderer(ListImagesModel m, boolean simple) {
        this.m = m;
        this.simple = simple;
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);

        File file = (File) value;

        // Si on doit afficher le parent, alors on affiche ".."
        if (file.equals(m.getParent()))
            setText("..");
        else
            setText(file.getName() + (file.isDirectory() ? "/" : ""));

        setToolTipText(FileUtilities.getDetails(file));
        
        if (!simple) {
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setHorizontalAlignment(SwingConstants.CENTER);
            setIcon(ImagesMap.get(file));
        } else
            setIcon(ImagesMap.get16x16(file));

        return this;
    }
}

