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
 * G�re le changement de la preview.
 * 
 * @author brahim
 * @author Sted
 */
public class PreviewManager extends JPanel implements Observer {

	/** Le supra-mod�le */
	protected FSeekerModel fsm = null;

	/** Largeur des images � pr�visualiser */
	private int width = 120;

	/** Hauteur des images � pr�visualiser */
	private int height = 90;

	/** Couleur d'arri�re plan pour la preview */
	private Color BACKG = Color.WHITE;

	/**
	 * Construit l'objet � partir d'un supra-mod�le.
	 * 
	 * @param fsm
	 *            Le supra-mod�le
	 */
	public PreviewManager(FSeekerModel fsm) {
		this.fsm = fsm;
		fsm.addObserver(this);
		setLayout(new BorderLayout());
		setBackground(BACKG);
	}

	/**
	 * M�thode appel�e quand la s�lection dans le supra-mod�le change.
	 */
	public void update(Observable o, Object arg) {

		if (fsm.isChanged(FSeekerModel.SELECTION)) {

			// Selon le fichier on met � jour la preview n�cessaire
			File selection = fsm.getSelection();

			// On sort si la s�lection est null
			if (selection == null)
				return;
			removeAll(); // S�curit�

			Preview p = null;
			JPanel preview = null;
			String mime = "";
			String ext = "";

			if (selection.canRead()) {
				mime = FileUtilities.getMIMEType(selection);

				// On r�cup�re l'extension
				String s = selection.getName();
				int index = s.lastIndexOf('.');
				// On v�rifie la valeur de l'index r�cup�r�
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

			// On met � jour l'affichage
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