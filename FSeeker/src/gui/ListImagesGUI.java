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
 * Une liste affichant un ensemble de fichiers, par l'interm�diaire d'un
 * ListImagesModel.
 * 
 * @author Sted
 * @author brahim
 */
public class ListImagesGUI extends JList {

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
		super(m);
		this.m = m;

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
		ListImagesDataControler controler = new ListImagesDataControler(m, this);

		// Listeners de la mouse, clavier, et changement s�l sur le GUI
		addMouseListener(controler);
		addKeyListener(controler);
		addListSelectionListener(controler);

		// Listeners des �l�ments du mod�le, et du changement de s�lection du
		// mod�le (issu du supra-mod�le)
		m.addListDataListener(controler);
		m.addSelectionChangedListener(controler);
	}

	/**
	 * Affiche le tooltip �valu� dynamiquement. Appel�e automatiquement par
	 * Swing.
	 * 
	 * @param e
	 *            l'�v�nement de la souris associ�
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

