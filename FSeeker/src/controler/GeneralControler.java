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
 * Contr�leur regroupant des m�thodes identiques aux diff�rents contr�leurs de
 * la liste, de la table, de l'arbre etc.
 * 
 * @author Sted
 */
public class GeneralControler {

	/**
	 * Quand une touche est press�, on g�re. !
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 * @param f
	 *            le fichier en s�lection
	 * @param fsm
	 *            le supra-mod�le
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
			if (GU.confirm("�tes-vous s�r de vouloir supprimer le fichier : "
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
	 * Quand l'utilisateur a simple cliqu� quelque part, on g�re. !
	 * 
	 * @param f
	 *            le fichier en s�lection (sous le click)
	 * @param fsm
	 *            le supra-mod�le
	 */
	public static void mouseSimpleClick(File f, FSeekerModel fsm) {
		if (f.isDirectory())
			fsm.setURI(f);
	}

}