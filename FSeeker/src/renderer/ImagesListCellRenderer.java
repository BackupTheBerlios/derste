package renderer;
import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.SwingConstants;

import misc.ImagesMap;

public class ImagesListCellRenderer extends DefaultListCellRenderer {

    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    
		File file = (File) value;
		String s = file.getName();
        Icon fileIcon = ImagesMap.getIcon(file);
        
        //Ca fait moche ca et puis si le systéme de fichier respecte la casse ca le fait plus
        //TODO A retirer plus tard
        if (file.isDirectory()) {            
            setText(s.toUpperCase());
        } else {
            setText(s.toLowerCase());            
        }	
		
		setIcon(fileIcon);

		setToolTipText("<html>Nom : " + file + "<br>Taille : " + file.length() + " octets</html>");
		
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		
		return this;
	}
}

