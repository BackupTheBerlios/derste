/*
 * Created on 7 nov. 2004
 */
package preview;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import misc.ImagesMap;

/**
 * Classe qui permet la repr�sentation d'un fichier quelconqueen affichant
 * simplement son ic�ne.
 * 
 * @author brahim
 * @author Sted
 */
public class SimpleImagePreview extends JPanel implements Preview {

	/** Label ou sera mise l'image */
	private JLabel imgLabel;

	/** Fichier image */
	private File file;

	/**
	 * Cr�� la preview d'une image.
	 * 
	 * @param file
	 *            image � pr�visualiser
	 */
	public SimpleImagePreview(File file) {
		imgLabel = new JLabel();
		this.file = file;
	}

	/**
	 * Lance la preview du fichier
	 */
	public void preview() {
		// On fixe l'image au label
		imgLabel.setIcon(ImagesMap.get(file));
		// On fixe le label au panel
		add(imgLabel);

	}

}