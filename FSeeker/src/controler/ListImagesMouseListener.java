/*
 * Created on 16 oct. 2004
 */
package controler;

import gui.ListImagesGUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import model.ListImagesModel;

/**
 * @author brahim
 */
public class ListImagesMouseListener extends MouseAdapter {
	protected JList list;

	public ListImagesMouseListener(ListImagesGUI list) {
		this.list = list.getList();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			File item = (File) list.getSelectedValue();
			if (item.isDirectory() && item.canRead()) {
				ListImagesModel dlm = (ListImagesModel) list.getModel();
				dlm.setDirectory(item);
			}
		}
	}
	
}