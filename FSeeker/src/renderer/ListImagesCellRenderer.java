package renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;

import misc.ImagesMap;
import misc.file.FileUtilities.FileDetails;

/**
 * @author sted
 */

public class ListImagesCellRenderer extends DefaultListCellRenderer {

    protected boolean simple = false;

    public ListImagesCellRenderer(boolean simple) {
        this.simple = simple;
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);

        if (value == null)
        	return this;
        
        File file = (File) value;
        FileDetails fd = new FileDetails(file);
        
        setText(fd.getName());
        setFont(fd.getFont());
        
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

