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
 * Une liste affichant un ensemble de fichiers, par l'intermédiaire d'un
 * ListImagesModel.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesGUI extends JList implements Observer {

	/** Le modèle de liste à utiliser */
	protected ListImagesModel m = null;

	// TODO le truc du false / true sera à virer quand le jdesktoppane sera ok
	public ListImagesGUI(ListImagesModel m) {
		this(m, false);
	}

	/**
	 * Construit une liste à partir du modèle passé en paramètre.
	 * 
	 * @param m
	 *            le modèle de liste sur lequel se baser
	 * @param simple
	 *            indique s'il faut former une liste à petit ou gros icônes
	 */
	public ListImagesGUI(ListImagesModel m, boolean simple) {
		this.m = m;
		m.addObserver(this);

		setModel(m);
		setVisibleRowCount(0);
		setDragEnabled(true);
		
		// Le prototypage accélère la vitesse d'affichage (pas de calcul à
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

