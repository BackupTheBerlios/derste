/*
 * Créé le 13 oct. 2004
 */
package misc;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Classe utilitaire ('Graphical Utilities' ho yeah) ne contenant que des
 * méthodes statiques.
 * 
 * @author Sted
 * @author brahim
 */
public class GU {

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
	 * Affiche une information.
	 * 
	 * @param s
	 *            information
	 */
	public static void info(String s) {
		JOptionPane.showMessageDialog(null, s, "Information",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Affiche un avertissement.
	 * 
	 * @param s
	 *            avertissement
	 */
	public static void warn(String s) {
		JOptionPane.showMessageDialog(null, s, "Avertissement",
				JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Demande la saisie d'un texte.
	 * 
	 * @param s
	 *            détail sur la chose à saisie
	 * @return la chose saisie
	 */
	public static String input(String s) {
		return JOptionPane.showInputDialog(null, s, "Demande de saisie",
				JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Demande la confirmation d'une chôôse.
	 * 
	 * @param s
	 *            le message pour lequel on attend confirmation ou infirmation
	 * @return true s'il y a eu confirmation, false sinon
	 */
	public static boolean confirm(String s) {
		return JOptionPane.showConfirmDialog(null, s, "Confirmation",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}

	/**
	 * Cree une bordure avec titre et l'applique à un JComponent.
	 * 
	 * @param title
	 *            titre de la bordure
	 * @param c
	 *            Le composant sur lequel on va fixer la bordure
	 */
	/*
	 * public static void putBorder(Border b, String title, JComponent c, int
	 * alignementX, int alignementY, Font f) {
	 * c.setBorder(BorderFactory.createTitledBorder(b, title, alignementX,
	 * alignementY, f)); }
	 */

	/*
	 * public static void createGUI(String title, JComponent comp) { JFrame
	 * frame = new JFrame(title); frame.getContentPane().add(comp);
	 * frame.setVisible(true);
	 * //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.pack(); }
	 */

	/*
	 * public static Image captureComponent(JComponent comp) throws AWTException {
	 * 
	 * Robot robot = new Robot(); Point p = new Point(0, 0);
	 * SwingUtilities.convertPointToScreen(p, comp); Rectangle area =
	 * comp.getBounds(); area.x = p.x; area.y = p.y; BufferedImage image = new
	 * Robot().createScreenCapture(area);
	 * 
	 * return image; }
	 */

}