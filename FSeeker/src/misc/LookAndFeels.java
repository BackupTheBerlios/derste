/*
 * Created on 30 oct. 2004
 */
package misc;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author sted
 */
public final class LookAndFeels {

	/**
	 * R�cup�re la liste des look 'n feels install�s.
	 * 
	 * @return la map (nom look 'n feel) => (nom de sa classe)
	 */
	private static Map getInstalledLookAndFeels() {
		LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
		Map lfmap = new HashMap();

		for (int i = 0; i < info.length; i++) {
			String lf = info[i].getName();
			String classe = info[i].getClassName();
			lfmap.put(lf, classe);
		}

		return lfmap;
	}

	/**
	 * Renvoie le menu des look 'n feels.
	 * 
	 * @param c
	 *            g�n�ralement, la jframe de l'application, qu'on mettra � jour
	 *            quand l'utilisateur changera le look 'n feel.
	 * @return le menu des l&f
	 */
	public static JMenu getLookAndFeelsMenu(final Component c) {
		JMenu menu = new JMenu("Look 'n Feels");
		ButtonGroup bg = new ButtonGroup();
		Map lfmap = getInstalledLookAndFeels();

		Iterator it = lfmap.keySet().iterator();

		while (it.hasNext()) {

			String clef = (String) it.next();
			final String classe = (String) lfmap.get(clef);
			boolean natif = classe.equals(UIManager
					.getSystemLookAndFeelClassName());

			JRadioButtonMenuItem item = new JRadioButtonMenuItem(clef, natif);

			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						UIManager.setLookAndFeel(classe);
					} catch (ClassNotFoundException e) {
						System.err.println("Classe non trouv�e : "
								+ e.getMessage());
					} catch (InstantiationException e) {
						System.err.println("Erreur d'instanciation : "
								+ e.getMessage());
					} catch (IllegalAccessException e) {
						System.err
								.println("Erreur d'acc�s : " + e.getMessage());
					} catch (UnsupportedLookAndFeelException e) {
						System.err.println("L&F non g�r� : " + e.getMessage());
					}

					// Tout mettre � jour !
					SwingUtilities.updateComponentTreeUI(c);
				}
			});

			bg.add(item);
			menu.add(item);
		}

		return menu;
	}
	
	/**
	 * Fixe le Look & Feel � celui du syst�me sous-jacent.
	 */
	public static void changeLF() {
		changeLF(UIManager.getSystemLookAndFeelClassName());
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