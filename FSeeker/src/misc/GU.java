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
import javax.swing.JEditorPane;
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
	// System.getProperty("path.separator");
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
	 * Fixe le Look & Feel � celui du syst�me sous-jacent.
	 */
	public static void changeLF() {
		changeLF(UIManager.getSystemLookAndFeelClassName());
	}

	/**
	 * @param location
	 *            adresse du fichier sur le syst�me
	 * @return
	 */
	public static Icon getImage(String location) {
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
		// TODO c'est quoi ce new JFrame foireux l� ?
		// Faut passer le parent en param l�, pas un nouveau sorti d'on ne sait
		// o�
		JOptionPane.showMessageDialog(new JFrame(), mess, "Avertissement",
				JOptionPane.WARNING_MESSAGE, getImage("mess.png"));
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
		// TODO: idem que message (new JFrame() n'imp)
		return JOptionPane.showConfirmDialog(new JFrame(), mess, titre,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				getImage("ask.png"));
	}

	/**
	 * Cr�e un JEditorPane charg�e avec une page HTML, cela est int�ressant pour
	 * afficher une mini aide ou autre dans le genre
	 * 
	 * @param url
	 *            URL du fichier � charger dans le JEditorPane
	 * @return Un JEditorPane affichant un fichier html
	 */
	// TODO: je pense pas que �a ait � faire quelque chose ici �a	
	public static JEditorPane createEditor(String url) {
		JEditorPane HelpPane = new JEditorPane();
		HelpPane.setEditable(false);
		//eP.setContentType("text/html");
		String helpURL = "resources" + SEP + url;
		if (helpURL != null) {
			try {
				//Si pas de probl�me on fixe la page
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}

	/**
	 * Fixe le Look & Feel � celui sp�cifi�.
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