/*
 * Created on 30 oct. 2004
 */
package misc;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

/**
 * 
 * Classe de gestion des Look&Feels Java.
 * 
 * @author sted
 * @author brahim
 *  
 */

public final class LookAndFeels {

	private static final String SKINSDIR = "skins" + File.separator;

	/**
	 * Récupère la liste des look 'n feels installés.
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
	 *            généralement, la jframe de l'application, qu'on mettra à jour
	 *            quand l'utilisateur changera le look 'n feel.
	 * @return le menu des l&f
	 */
	public static JMenu getLookAndFeelsMenu(final Component c) {
		JMenu menu = new JMenu("Look 'n Feels");
		ButtonGroup bg = new ButtonGroup();
		Map lfmap = getInstalledLookAndFeels();

		Iterator it = lfmap.keySet().iterator();

		/* On ajoute les Look&Feels par défaut du systéme */
		while (it.hasNext()) {

			String clef = (String) it.next();
			final String classe = (String) lfmap.get(clef);
			boolean natif = classe.equals(UIManager
					.getSystemLookAndFeelClassName());

			JRadioButtonMenuItem item = new JRadioButtonMenuItem(clef, natif);

			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					changeLF(classe);
					SwingUtilities.updateComponentTreeUI(c);
				}
			});

			bg.add(item);
			menu.add(item);
		}

		/* On ajoute les Look&Feels skinlf */
		final String clef = "themepack.zip";
		/* Les skins L&F ne sont pas natifs */
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(clef, false);

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				changeSkin(clef);
				SwingUtilities.updateComponentTreeUI(c);
			}
		});

		bg.add(item);
		menu.add(item);

		return menu;
	}

	/**
	 * Fixe le Look & Feel à celui du système sous-jacent.
	 */
	public static void changeLF() {
		changeLF(UIManager.getSystemLookAndFeelClassName());
	}

	/**
	 * Fixe le Look & Feel à celui spécifié.
	 * 
	 * @param name
	 *            nom de la classe du l&f
	 */
	public static void changeLF(String name) {
		try {
			UIManager.setLookAndFeel(name);
		} catch (ClassNotFoundException e) {
			System.err.println("Classe non trouvée : " + e.getMessage());
		} catch (InstantiationException e) {
			System.err.println("Erreur d'instanciation : " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("Erreur d'accès : " + e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			System.err.println("L&F non géré : " + e.getMessage());
		}
	}

	/**
	 * Fixe le Look & Feel à celui spécifié.
	 * 
	 * @param lf
	 *            LookAndFeel à utiliser
	 */
	public static void changeLF(LookAndFeel lf) {
		try {
			UIManager.setLookAndFeel(lf);
		} catch (UnsupportedLookAndFeelException e) {
			System.err.println("L&F non géré : " + e.getMessage());
		}
	}

	/**
	 * Méthode qui utilise la librairie skinlf pour affecter le L&F particulier
	 * à l'<code>UIManager</code>
	 * 
	 * @param theme
	 *            nom du thème
	 */
	public static void changeSkin(String theme) {
		try {
			// On définit le skin à utiliser
			// C'est ça, elle lance des "Exception", mais quelle daube.
			Skin theSkinToUse = SkinLookAndFeel.loadThemePack(SKINSDIR + theme);
			SkinLookAndFeel.setSkin(theSkinToUse);

			// Maintenant, on applique le Skin sélectionné
			changeLF(new SkinLookAndFeel());
			//SwingUtilities.updateComponentTreeUI();

		} catch (Exception e) {
			System.err.println("Erreur dans le chargement du thème : "
					+ e.getMessage());
		}
	}
}