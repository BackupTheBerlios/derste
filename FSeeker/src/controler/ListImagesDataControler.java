package controler;

import gui.ListImagesGUI;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import model.ListImagesModel;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesDataControler extends MouseAdapter implements
		KeyListener, ListDataListener {

	/** Le mod�le de liste � contr�ler */
	protected ListImagesModel m = null;

	/** La vue associ�e */
	protected ListImagesGUI gui = null;

	/**
	 * Construit le contr�leur associ� au mod�le <code>m</code>.
	 * 
	 * @param m
	 *            le mod�le
	 */
	public ListImagesDataControler(ListImagesModel m, ListImagesGUI gui) {
		this.m = m;
		this.gui = gui;
	}

	/**
	 * Quand on clique, ca ouvre le dossier si on est positionn� sur un dossier.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			File f = (File) ((JList) e.getSource()).getSelectedValue();
			GeneralControler.mouseSimpleClick(f, m.getModel());
		}


		if (e.getButton() == MouseEvent.BUTTON3_DOWN_MASK) {
			JList list = (JList) e.getSource();
			//TOUT LE MONDE n'a pas la 1.5
			//JPopupMenu popup = list.getComponentPopupMenu();

			Point clic = e.getPoint();
			int index = list.locationToIndex(clic);
			Rectangle r = list.getCellBounds(index, index);
			if (r.contains(clic)) {
				//popup.add(new JMenuItem("salut"));

			}
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Si une touche a �t� appuy�.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 */
	public void keyPressed(KeyEvent e) {
		File f = (File) ((JList) e.getSource()).getSelectedValue();
		GeneralControler.keyPressed(e, f, m.getModel());
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void contentsChanged(ListDataEvent e) {
		gui.ensureIndexIsVisible(0);
		gui.setSelectedIndex(0);
	}

	public void intervalAdded(ListDataEvent e) {
	}

	public void intervalRemoved(ListDataEvent e) {
	}

}