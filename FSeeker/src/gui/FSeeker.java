package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import misc.GU;

/*
 * Created on 15 oct. 2004
 */

/**
 * Classe de lancement de FSeeker.
 * 
 * @author Sted
 */
public class FSeeker extends JFrame {
	private final static String VERSION = "0.1";

	private JPanel main = null;

	public FSeeker() {
		super("FSeeker v" + VERSION);

		GU.changeLF();
		setJMenuBar(getMenu());

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(getToolBar(), BorderLayout.NORTH);
		
		cp.add(getStatusBar(), BorderLayout.SOUTH);

		setPreferredSize(new Dimension(640, 480));
		pack();
		GU.center(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public JPanel getMain() {
		return main;
	}

	public void setMain(JPanel main) {
		this.main = main;
		System.out.println(main);
		getContentPane().add(main, BorderLayout.CENTER);
	}

	private JToolBar getToolBar() {
		JToolBar tb = new JToolBar(JToolBar.HORIZONTAL);
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setVgap(0);
		tb.setLayout(layout);

		tb.setFloatable(false);
		JButton b = new JButton("");
		b.setIcon(new ImageIcon("dot.gif"));
		b.setToolTipText("prout");
		tb.add(b);
		JLabel l;
		l = new JLabel("Précédent");
		GU.setImage(l, "dot.gif");
		tb.add(l);
		l = new JLabel("Suivant");
		GU.setImage(l, "dot.gif");
		tb.add(l);
		tb.addSeparator();
		l = new JLabel("Parent");
		GU.setImage(l, "dot.gif");
		tb.add(l);
		tb.addSeparator(new Dimension(10, 10));
		b = new JButton("Rechercher");
		tb.add(b);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel pp = new JPanel(new FlowLayout());
				pp.add(new JTextArea(20, 20));
				pp.add(new JLabel("pwet"));
				System.out.println(pp);
				setMain(pp);
			}
		});
		l = new JLabel("Affichage");
		GU.setImage(l, "dot.gif");
		tb.add(l);

		tb.addSeparator(new Dimension(10, 10));
		tb.add(new JLabel("URI : "));
		tb.add(new JTextField(20));

		return tb;
	}

	private StatusBar getStatusBar() {
		StatusBar sb = new StatusBar();
		JLabel l = sb.addLabel("FSeeker " + VERSION);
		GU.setImage(l, "dot.jpg");
		sb.addProgressBar(0, 100);
		return sb;
	}

	private JMenuBar getMenu() {
		JMenuBar mb = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;

		menu = new JMenu("Fichier");
		menuItem = new JMenuItem("Quitter", KeyEvent.VK_Q);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FSeeker.this.dispose();
				System.exit(0);
			}
		});
		menu.add(menuItem);
		mb.add(menu);

		menu = new JMenu("Edition");
		menuItem = new JMenuItem("Annuler", KeyEvent.VK_C);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("annuler");
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Couper", KeyEvent.VK_X);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("couper");
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Copier", KeyEvent.VK_C);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("copier");
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Coller", KeyEvent.VK_V);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("coller");
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Préférences", KeyEvent.VK_P);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("préférences");
			}
		});
		menu.add(menuItem);
		mb.add(menu);

		menu = new JMenu("Aide");
		menuItem = new JMenuItem("Sommaire", KeyEvent.VK_S);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("sommaire");
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("A propos", KeyEvent.VK_A);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("à propos");
			}
		});
		menu.add(menuItem);
		mb.add(menu);

		return mb;
	}

	public static void main(String args[]) {
		new FSeeker();
	}

}