package gui;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;
import javax.swing.JScrollPane;

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
public class ListImagesGUI extends JList implements Observer {

	/** Le mod�le de liste � utiliser */
	protected ListImagesModel m = null;

	// TODO le truc du false / true sera � virer quand le jdesktoppane sera ok
	public ListImagesGUI(ListImagesModel m) {
		this(m, false);
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

		setModel(m);
		setVisibleRowCount(0);
		setDragEnabled(true);
		
		// Le prototypage acc�l�re la vitesse d'affichage (pas de calcul �
		// faire) et uniformise l'affichage
		setPrototypeCellValue(new File("FICHIERPROTO.CONFIG"));
		
		// En liste simple, on affiche de haut en bas, gauche vers droite
		// en pas simple, on affiche de gauche vers droite, haut en bas
		if (simple)
			setLayoutOrientation(JList.VERTICAL_WRAP);
		else
			setLayoutOrientation(JList.HORIZONTAL_WRAP);

		setCellRenderer(new ListImagesCellRenderer(simple));
		ListImagesDataControler controler = new ListImagesDataControler(m);
		addMouseListener(controler);
		addKeyListener(controler);
	}

	public void update(Observable o, Object caller) {
		revalidate();
		repaint();
	}
	

	

}

