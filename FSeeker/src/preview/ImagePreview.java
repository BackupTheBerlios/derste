/*
 * Created on 31 oct. 2004
 */
package preview;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Cette classe permet la prévisualisation d'une image.
 * 
 * @author brahim
 * @author Sted
 */
public class ImagePreview extends JPanel implements Preview {

	/** Fichier correspondant au fichier image */
	private File image;

	/** La label ou l'on fixe l'imgage */
	private JLabel imageLabel;

	/** Longueur et largeur de la nouvelle image à obtenir */
	private int thumbWidth, thumbHeight;

	public ImagePreview(File image, int thumbWidth, int thumbHeight) {
		this.image = image;
		this.thumbWidth = thumbWidth;
		this.thumbHeight = thumbHeight;
		imageLabel = new JLabel();
	}

	/**
	 * Créé la prévisualisation.
	 */
	public void preview() {
		imageLabel.setIcon(getThumbnail());
		add(imageLabel);
	}

	/**
	 * Retourne la miniature d'une image.
	 * 
	 * @return la miniature
	 */
	public Icon getThumbnail() {
		if (image.exists()) {
			Icon thumbnail = null;
			Image img = Toolkit.getDefaultToolkit().getImage(
					"" + image.getAbsolutePath());

			// On ne peut pas récupérer les infos qur l'image si elle n'est pas
			// chargée donc il faut utiliser cette méthode
			try {
				MediaTracker tracker = new MediaTracker(this);
				tracker.addImage(img, 1);
				tracker.waitForAll(); // load it now, then we can get its width
				// & height
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			// Garder la proportionalité
			int imageWidth = img.getWidth(null);
			int imageHeight = img.getHeight(null);

			// Si la hauteur ou la longueur de l'image d'origine est plus grande
			// que la taille voulue
			if (imageWidth > thumbWidth || imageHeight > thumbHeight) {
				double thumbRatio = (double) thumbWidth / (double) thumbHeight;
				double imageRatio = (double) imageWidth / (double) imageHeight;
				if (thumbRatio < imageRatio) {
					thumbHeight = (int) (thumbWidth / imageRatio);
				} else {
					thumbWidth = (int) (thumbHeight * imageRatio);
				}

				thumbnail = new ImageIcon(img.getScaledInstance(thumbWidth,
						thumbHeight, Image.SCALE_SMOOTH));
			}
			// Pas besoin de redimensionner, on renvoie l'image telle qu'elle
			else
				thumbnail = new ImageIcon(img);

			return thumbnail;
		}

		// On ne renvoie rien, le fichier n'existe pas
		return null;
	}
}