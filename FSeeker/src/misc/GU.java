/*
 * Créé le 13 oct. 2004
 */
package misc;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe utilitaire ne contenant que des méthodes statiques.
 * 
 * @author derosias
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
	 * Attribue une image à un JLabel. (setIcon..)
	 * 
	 * @param label
	 *            le JLabel auquel affecté une image
	 * @param image
	 *            l'image en question à associer
	 */
	public static void setIcon(JLabel label, String image) {
		ImageIcon ii = new ImageIcon("images/" + image);
		label.setIcon(ii);
	}

	
	/**
	 * Fixe le Look & Feel à celui du système sous-jacent.
	 */
	public static void changeLF() {
		changeLF(UIManager.getSystemLookAndFeelClassName());
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