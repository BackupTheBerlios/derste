/*
 * Créé le 13 oct. 2004
 */
package misc;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

/**
 * Classe utilitaire ne contenant que des méthodes statiques.
 * 
 * @author derosias & Ait elhaj brahim
 */
public class GU {

	public static final char SEP = File.separatorChar;//System.getProperty("path.separator");

	private static final String RESOURCES = "resources" + SEP + "images" + SEP;

	/**
	 * Centre une JFrame sur l'écran.
	 * 
	 * @param frame
	 *            la JFrame à centrer
	 */
	public static void center(JFrame frame) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = frame.getSize();

		frame.setLocation((int) (screen.width - size.width) / 2,
				(int) (screen.height - size.height) / 2);
	}

	/**
	 * Attribue une image à un JLabel. (setIcon..) TODO: Faire un type global
	 * pour les JButtons, JLabel etc.
	 * 
	 * @param label
	 *            le JLabel auquel affecté une image
	 * @param image
	 *            l'image en question à associer
	 */
	public static void setIcon(JLabel label, String image) {
		//TODO Mettre toutres les images dans resources/images
		ImageIcon ii = createImg(image);
		label.setIcon(ii);
	}

	/**
	 * Fixe le Look & Feel à celui du système sous-jacent.
	 */
	public static void changeLF() {
		changeLF(UIManager.getSystemLookAndFeelClassName());
	}

	/**
	 * @param location
	 *            adresse du fichier sur le systéme
	 * @return
	 */
	public static ImageIcon createImg(String location) {
		ImageIcon img = new ImageIcon(RESOURCES + location);
		return img;
	}

	/**
	 * Affiche tout simplement une boite de dialogue de type JOptionPane
	 * signalant un message
	 * 
	 * @param mess
	 *            Message à afficher dans la boite de dialogue
	 */
	public static void message(String mess) {

		JOptionPane.showMessageDialog(new JFrame(), mess, "Avertissement",
				JOptionPane.WARNING_MESSAGE, createImg("mess.png"));
	}

	/**
	 * Affiche un panneau de confirmation qui et renvoie en meme temps la
	 * reponse fourni
	 * 
	 * @param titre
	 *            Le titre souhaité pour la boite de dialogue
	 * @param mess
	 *            Le message pour lequel on attend confirmation ou infirmation
	 * @return Renvoie la valeur JOptionPane.YES_OPTION ou JOptionPane.NO_OPTION
	 *         selon le choix
	 */
	public static int confirm(String titre, String mess) {
		int reponse = JOptionPane.showConfirmDialog(new JFrame(), mess, titre,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				createImg("ask.png"));
		return reponse;
	}

	/**
	 * Crée un JEditorPane chargée avec une page HTML, cela est intéressant pour
	 * afficher une mini aide ou autre dans le genre
	 * 
	 * @param url
	 *            URL du fichier à charger dans le JEditorPane
	 * @return Un JEditorPane affichant un fichier html
	 */
	public static JEditorPane createEditor(String url) {
		JEditorPane HelpPane = new JEditorPane();
		HelpPane.setEditable(false);
		//eP.setContentType("text/html");
		String helpURL = "resources" + SEP + url;
		if (helpURL != null) {
			try {
				//Si pas de probléme on fixe la page
				HelpPane.setPage(helpURL);
			} catch (java.io.IOException e) {
				System.err.println("Le fichier n'est pas accessible : "
						+ helpURL);
			}
		} else {
			System.err.println("Ne pas trouver : " + helpURL);
		}

		return HelpPane;
	}

	/**
	 * Cree une bordure avec titre et l'applique à un JComponent
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}

	/**
	 * Fixe le Look & Feel à celui spécifié.
	 */
	public static void changeLF(String name) {
		try {
			UIManager.setLookAndFeel(name);
		} catch (UnsupportedLookAndFeelException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		}
	}
}