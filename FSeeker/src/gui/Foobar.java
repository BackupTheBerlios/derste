
package gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import misc.GU;

/*
 * Created on 12 oct. 2004
 */

/**
 * @author Sted
 */
public class Foobar extends JFrame {
	
	private static JMenuBar getMenu() {
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("Fichier");

		JMenuItem menuItem = new JMenuItem("Quitter", KeyEvent.VK_Q);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		menu.add(menuItem);
		mb.add(menu);
		return mb;
	}
	
	public Foobar() {
		// Faudrait créer une méthode qui fous le panel déjà existant en NORTH
		// et rajoute une statusbar en SOUTH, genre
		// StatusBar sb = new StatusBar(this); et pan !
		
		Container c = getContentPane();
		setJMenuBar(getMenu());
		
		//////////////////////////////////////////////////
		StatusBar sb = new StatusBar();
		JLabel label = new JLabel("Pwet, lalala");
		
		c.setLayout(new BorderLayout());
		c.add(label, BorderLayout.NORTH);
		c.add(sb, BorderLayout.SOUTH);

		JLabel contenu = sb.addLabel("384 fichiers dont 37 répertoires", SwingConstants.LEFT);
		JLabel restant = sb.addLabel("154 Mo restants");
		JLabel taille = sb.addLabel("6.24 Ko");
		JProgressBar pb = sb.addProgressBar(0, 100);
		JLabel path = sb.addLabel("/root/derosias", SwingConstants.RIGHT);
		GU.setIcon(path, "dot.jpg");

		////////////////////////////////////////
		
		setSize(new Dimension(600, 200));
		GU.center(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Foobar();
	}
}