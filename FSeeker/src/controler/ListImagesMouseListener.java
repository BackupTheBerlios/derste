/*
 * Created on 16 oct. 2004
 */
package controler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import model.ListImagesModel;

/**
 * @author brahim
 * @author Sted
 */
public class ListImagesMouseListener extends MouseAdapter {
	public void mouseClicked(MouseEvent e) {
		JList list = (JList) e.getSource();
		
		if (e.getClickCount() == 2) {
			File item = (File) list.getSelectedValue();
			if (item.isDirectory() && item.canRead()) {
				ListImagesModel dlm = (ListImagesModel) list.getModel();
				dlm.setDirectory(item);
			}
		}
	}
	
}