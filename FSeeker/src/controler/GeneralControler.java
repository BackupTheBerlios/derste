/*
 * Created on 3 nov. 2004
 */
package controler;

import java.awt.event.KeyEvent;
import java.io.File;

import misc.GU;
import misc.file.FileUtilities;
import model.FSeekerModel;

/**
 * Contrôleur regroupant des méthodes identiques aux différents contrôleurs de
 * la liste, de la table, de l'arbre etc.
 * 
 * @author Sted
 */
public class GeneralControler {

	/**
	 * Quand une touche est pressé, on gère. !
	 * 
	 * @param e
	 *            l'événement associé
	 * @param f
	 *            le fichier en sélection
	 * @param fsm
	 *            le supra-modèle
	 */
	public static void keyPressed(KeyEvent e, File f, FSeekerModel fsm) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			if (f.isDirectory())
				fsm.setURI(f);
			break;
		case KeyEvent.VK_BACK_SPACE:
			fsm.gotoParent();
			break;
		case KeyEvent.VK_DELETE:
			if (GU.confirm("Êtes-vous sûr de vouloir supprimer le fichier : "
					+ f.getName()))
				if (!FileUtilities.delete(f))
					GU.warn("Impossible de supprimer le fichier : "
							+ f.getName());
				else
					fsm.update();
			break;
		}

	}

	/**
	 * Quand l'utilisateur a simple cliqué quelque part, on gère. !
	 * 
	 * @param f
	 *            le fichier en sélection (sous le click)
	 * @param fsm
	 *            le supra-modèle
	 */
	public static void mouseSimpleClick(File f, FSeekerModel fsm) {
		if (f.isDirectory())
			fsm.setURI(f);
	}

}