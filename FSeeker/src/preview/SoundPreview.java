/*
 * Created on 1 nov. 2004
 */
package preview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javazoom.jl.decoder.JavaLayerException;
import misc.GU;
import misc.ImagesMap;
import misc.MP3Previewer;

/**
 * Classe qui permet la prévisualisation de fichier sons. Formats supportés :
 * mp3, wav, PCM .... cf doc JLayer pour fichier ogg, mettre les plugins
 * 
 * @author brahim
 * @author Sted
 */
public class SoundPreview extends JPanel implements Preview, ActionListener {

	/** Fichier mp3 ou wav ou PCM dont on souhaite effectuer la lecture */
	private File file;

	/** Permet la gestion de play et stop */
	private MP3Previewer pl;

	/** Si impossible de lire le fichier */
	private boolean error = false;
	
	/**
	 * Les quelques boutons qui servont à lancer le son et l'arretée
	 */
	private JButton[] controls = new JButton[2];

	/**
	 * Construit la préview d'un fichier son.
	 * 
	 * @param file
	 *            fichier son dont obtenir une prévisualisation
	 */
	public SoundPreview(File file) {
		this.file = file;

		try {
			pl = new MP3Previewer(file);
		} catch (JavaLayerException jle) {
			error = true;
		}
	}

	/**
	 * Lance la prévisualisation.
	 */
	public void preview() {
		if (error) {
			add(new JLabel("Impossible de lire"));
			return;
		}
		
		controls[0] = new JButton(ImagesMap.get("play.png"));
		controls[1] = new JButton(ImagesMap.get("stop.png"));

		for (int i = 0; i < controls.length; i++) {
			add(controls[i]);
			controls[i].addActionListener(this);
		}
	}

	/**
	 * Au clic d'un bouton..
	 */
	public void actionPerformed(ActionEvent ev) {
		// TODO Optimiser < lol
		if (ev.getSource().equals(controls[0]))
			try {
				pl.play();
			} catch (JavaLayerException jle) {
				GU.warn("Impossible de lire le fichier " + file);
			}
		else if (ev.getSource() == controls[1])
			pl.stop();
	}
}