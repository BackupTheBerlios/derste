package renderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;

import misc.ImagesMap;

public class ImagesListCellRenderer extends DefaultListCellRenderer {

    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    // TODO Refaire cette méthode au propre
		File file = (File) value;
		String s = file.getName();
		
				
		Icon fileIcon = null;
		String fileStr = file.toString();
		
		if (file.isDirectory()) {				
			fileIcon = ImagesMap.get("directory.png");
			setText(s.toUpperCase());
		} else {
		    setText(s.toLowerCase());
		    
		    int rindex = fileStr.lastIndexOf('.');
		    
		    String ext = null;
		    if (rindex >= 0)
		        ext = fileStr.substring(rindex);
		    
			if (ext != null) {
				fileIcon = ImagesMap.get(ext + ".png");
			}
			
		    if (fileIcon == null)
		        fileIcon = ImagesMap.get("dot.gif");
		}	
		
		setIcon(fileIcon);

		setToolTipText("<html>Nom : " + file + "<br>Taille : " + file.length() + " octets</html>");
		
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		
		return this;
	}
}
