package gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;

import misc.file.FileUtilities;
import model.ListImagesModel;
import renderer.ListImagesCellRenderer;
import controler.ListImagesDataControler;

/**
 * Une liste affichant un ensemble de fichiers, par l'interm�diaire d'un
 * ListImagesModel.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesGUI implements Observer {

	/** Le mod�le de liste � utiliser */
	protected ListImagesModel m = null;

	protected JList gui = null;
	
	// TODO le truc du false / true sera � virer quand le jdesktoppane sera ok
	public ListImagesGUI(ListImagesModel m) {
		this(m, false);
	}
	
	public JList getGUI() {
		return gui;
	}

	/**
	 * Construit une liste � partir du mod�le pass� en param�tre.
	 * 
	 * @param m
	 *            le mod�le de liste sur lequel se baser
	 * @param simple
	 *            indique s'il faut former une liste � petit ou gros ic�nes
	 */
	public ListImagesGUI(ListImagesModel m, boolean simple) {
		this.m = m;
		m.addObserver(this);

		gui = new JList(m) {
			public String getToolTipText(MouseEvent event) {
			    Point clic = event.getPoint();
			    int index = locationToIndex(clic);
				Rectangle r = ListImagesGUI.this.gui.getCellBounds(index, index);
				if (r.contains(clic)) {
				    File f = (File) ListImagesGUI.this.m.getElementAt(index);
				    return FileUtilities.getDetails(f);
				}
				return null;
			}
		};
		gui.setVisibleRowCount(0);
		gui.setDragEnabled(true);
		
		// Le prototypage acc�l�re la vitesse d'affichage (pas de calcul �
		// faire) et uniformise l'affichage
		// gui.setPrototypeCellValue(new File("FICHIERPROTO.CONFIG"));
		
		// En liste simple, on affiche de haut en bas, gauche vers droite
		// en pas simple, on affiche de gauche vers droite, haut en bas
		if (simple)
			gui.setLayoutOrientation(JList.VERTICAL_WRAP);
		else
			gui.setLayoutOrientation(JList.HORIZONTAL_WRAP);

		gui.setCellRenderer(new ListImagesCellRenderer(simple));
		ListImagesDataControler controler = new ListImagesDataControler(m);
		gui.addMouseListener(controler);
		gui.addKeyListener(controler);
	}

	public void update(Observable o, Object caller) {
		gui.revalidate();
		gui.repaint();
	}


}

