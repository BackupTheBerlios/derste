/*
 * Cr�� le 13 oct. 2004
 */
package misc;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

/**
 * Classe utilitaire ne contenant que des m�thodes statiques.
 * 
 * @author derosias & Ait elhaj brahim
 */
public class GU {

	/** Le s�parateur de path du syst�me */
	public static final char SEP = File.separatorChar;

	/** L� o� sont stock�es toutes les images */
	private static final String IMAGES = "images" + SEP;

	/**
	 * Centre une JFrame sur l'�cran.
	 * 
	 * @param frame
	 *            la JFrame � centrer
	 */
	public static void center(JFrame frame) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = frame.getSize();

		frame.setLocation((int) (screen.width - size.width) / 2,
				(int) (screen.height - size.height) / 2);
	}

	/**
	 * Attribue une image � un JLabel. (setIcon..)
	 * 
	 * @param label
	 *            le JLabel auquel affect� une image
	 * @param image
	 *            l'image en question � associer
	 */
	/* TODO: Faire un type global pour les JButtons, JLabel etc. */
	public static void setIcon(JLabel label, String image) {
		label.setIcon(getImage(image));
	}

	/**
	 * @param location
	 *            adresse du fichier sur le syst�me
	 * @return
	 */
	public static Icon getImage(String location) {
		// TODO ne pas oublier quand �a sera pr�t de faire le lien avec la
		// classe d�di�es aux images
		return new ImageIcon(IMAGES + location);
	}

	/**
	 * Affiche tout simplement une boite de dialogue de type JOptionPane
	 * signalant un message
	 * 
	 * @param mess
	 *            Message � afficher dans la boite de dialogue
	 */
	public static void message(String mess) {
		JOptionPane.showMessageDialog(null, mess, "Avertissement",
				JOptionPane.INFORMATION_MESSAGE, ImagesMap.get("ask.png"));
	}

	/**
	 * Affiche un panneau de confirmation qui et renvoie en meme temps la
	 * reponse fourni
	 * 
	 * @param titre
	 *            Le titre souhait� pour la boite de dialogue
	 * @param mess
	 *            Le message pour lequel on attend confirmation ou infirmation
	 * @return Renvoie la valeur JOptionPane.YES_OPTION ou JOptionPane.NO_OPTION
	 *         selon le choix
	 */
	public static int confirm(String titre, String mess) {
		return JOptionPane.showConfirmDialog(null, mess, titre,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				getImage("ask.png"));
	}

	/**
	 * Cree une bordure avec titre et l'applique � un JComponent
	 * 
	 * @param title
	 *            titre de la bordure
	 * @param toPutOn
	 *            Le composant sur lequel on va fixer la bordure
	 */
	public static void putBorder(Border b, String title, JComponent toPutOn,
			int alignementX, int alignementY, Font f) {
		toPutOn.setBorder(BorderFactory.createTitledBorder(b, title,
				alignementX, alignementY, f));

	}

	public static void createGUI(String title, JComponent comp) {
		JFrame frame = new JFrame(title);
		frame.getContentPane().add(comp);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}


}