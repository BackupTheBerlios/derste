/*
 * Created on 7 nov. 2004
 */
package preview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import misc.file.FileUtilities;
import model.FSeekerModel;

/**
 * Gère le changement de la preview.
 * 
 * @author brahim
 * @author Sted
 */
public class PreviewManager extends JPanel implements Observer {

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

	/** Largeur des images à prévisualiser */
	private int width = 120;

	/** Hauteur des images à prévisualiser */
	private int height = 90;

	/** Couleur d'arrière plan pour la preview */
	private Color BACKG = Color.WHITE;

	/**
	 * Construit l'objet à partir d'un supra-modèle.
	 * 
	 * @param fsm
	 *            Le supra-modèle
	 */
	public PreviewManager(FSeekerModel fsm) {
		this.fsm = fsm;
		fsm.addObserver(this);
		setLayout(new BorderLayout());
		setBackground(BACKG);
	}

	/**
	 * Méthode appelée quand la sélection dans le supra-modèle change.
	 */
	public void update(Observable o, Object arg) {

		if (fsm.isChanged(FSeekerModel.SELECTION)) {

			// Selon le fichier on met à jour la preview nécessaire
			File selection = fsm.getSelection();

			// On sort si la sélection est null
			if (selection == null)
				return;
			removeAll(); // Sécurité

			Preview p = null;
			JPanel preview = null;
			String mime = "";
			String ext = "";

			if (selection.canRead()) {
				mime = FileUtilities.getMIMEType(selection);

				// On récupère l'extension
				String s = selection.getName();
				int index = s.lastIndexOf('.');
				// On vérifie la valeur de l'index récupéré
				if (!(index == -1 || index + 1 >= s.length()))
					ext = s.substring(index + 1);
			}

			if (selection.isDirectory())
				p = new SimpleImagePreview(selection);
			else if (mime.startsWith("image/"))
				p = new ImagePreview(selection, width, height);
			else if (mime.equals("text/plain"))
				p = new TextPreview(selection);
			else if (ext.equals("mp3") || ext.equals("wav"))
				p = new SoundPreview(selection);
			else
				p = new SimpleImagePreview(selection);

			// On lance la preview
			p.preview();
			preview = (JPanel) p;
			preview.setOpaque(true);
			preview.setBackground(BACKG);
			JPanel tmp = new JPanel(new BorderLayout());
			tmp.setBackground(BACKG);
			tmp.add(preview, BorderLayout.WEST);
			add(tmp, BorderLayout.NORTH);

			// Maintenant on s'occupe de mettre les infos
			JPanel info = new FileInfo(selection);
			info.setOpaque(true);
			info.setBackground(BACKG);
			add(info, BorderLayout.SOUTH);

			// On met à jour l'affichage
			repaint();
			revalidate();
		}
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}