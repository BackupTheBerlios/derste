package controler;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import misc.GU;
import model.ListImagesModel;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesDataControler extends MouseAdapter implements
		KeyListener {

	private ListImagesModel m = null;;

	public ListImagesDataControler(ListImagesModel m) {
		this.m = m;
	}

	protected void setURI(InputEvent e) {
		File f = (File) ((JList) e.getSource()).getSelectedValue();
		if (f.isDirectory()) {
			if (f.canRead())
				m.getModel().setURI(f, e.getSource());
			else
				GU.message("Vous n'avez pas accès à ce dossier.");
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2)
			setURI(e);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			setURI(e);
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}