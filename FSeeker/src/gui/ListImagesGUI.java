package gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import misc.file.FileUtilities;
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
public class ListImagesGUI extends JList {

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
		super(m);
		this.m = m;

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
		ListImagesDataControler controler = new ListImagesDataControler(m, this);

		// Listeners de la mouse, clavier, et changement sél sur le GUI
		addMouseListener(controler);
		addKeyListener(controler);
		addListSelectionListener(controler);

		// Listeners des éléments du modèle, et du changement de sélection du
		// modèle (issu du supra-modèle)
		m.addListDataListener(controler);
		m.addSelectionChangedListener(controler);
	}

	/**
	 * Affiche le tooltip évalué dynamiquement. Appelée automatiquement par
	 * Swing.
	 * 
	 * @param e
	 *            l'événement de la souris associé
	 */
	public String getToolTipText(MouseEvent e) {
		Point clic = e.getPoint();
		int index = locationToIndex(clic);
		Rectangle r = getCellBounds(index, index);
		if (r.contains(clic)) {
			File f = (File) m.getElementAt(index);
			return FileUtilities.getToolTip(f);
		}
		return null;
	}
	

}

