/*
 * Created on 16 oct. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import model.ListImagesModel;
import gui.ListImagesGUI;

/**
 * @author brahim
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ListMouseListener extends MouseAdapter {
	protected JList list;

	private boolean DEBUG;

	public ListMouseListener(ListImagesGUI list) {
		this.list = list.getList();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			File item = (File) list.getSelectedValue();
			if (item.isDirectory() && item.canRead()) {
				ListImagesModel dlm = (ListImagesModel) list.getModel();
				dlm.setDir(item);
			}

			if (DEBUG)
				System.out.println("Double clicked on " + item);
		}
	}
}