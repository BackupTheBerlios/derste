package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import misc.FileSystemTree;
import misc.GU;
import misc.ImagesMap;
import model.FileSystemTreeModel;
import model.FileTableModel;
import model.ListImagesModel;
import model.URIModel;
import controler.FSeekerControler;
import controler.ToolBarControler;

/*
 * Created on 15 oct. 2004
 */

/**
 * Classe de lancement de FSeeker.
 * 
 * @author Sted
 */

public class FSeeker {

	/** La version du software */
	public final static String VERSION = "0.2a";

	private final static File ROOT = (System.getProperty("os.name").contains(
			"Linux") ? File.listRoots()[0] : File.listRoots()[1]);

	private JFrame f = null;

	private FileSystemTreeModel fstm = null;

	private ListImagesModel lim = null;

	private URIModel uriModel = null;

	private FileSystemTreeGUI fstgui = null;

	private ListImagesGUI ligui = null;

	private ListImagesGUI lgui = null;

	private FileTableGUI ftgui = null;

	private FileTableModel ftmodel = null;

	private URIGUI ugui = null;

	//private JPopupMenu popup = null;

	/** Singleton contenant la vue principale par défaut */
	private JPanel defaultView = null;

	/** Singleton contenant la vue à la "macos" */
	private JPanel macosView = null;

	/** Le contrôleur de la barre d'outils */
	private final ToolBarControler toolBarControler = new ToolBarControler(this);

	/** Le panel central, principal, destinées à accueillir les différentes vues */
	private JPanel main = null;

	/** Construit la fenêtre par défaut de FSeeker */
	public FSeeker() {
		f = new JFrame("FSeeker v" + VERSION);
		Container cp = f.getContentPane();

		// GU.changeLF();
		f.setJMenuBar(getDefaultMenuBar());

		// La vue pas défaut (tree + icônes/détails/liste/etc.)
		setView(getDefaultView());
		File home = new File(System.getProperty("user.home"));
		fstgui.setSelectionPath(FileSystemTree.getTreePath(home));

		// Un border pour toolbar (en haut), statusbar (en bas), main (central)
		cp.setLayout(new BorderLayout());
		cp.add(getDefaultToolBar(), BorderLayout.NORTH);
		cp.add(main, BorderLayout.CENTER);
		cp.add(getDefaultStatusBar(), BorderLayout.SOUTH);

		// Une taille raisonnable au départ (dans un fichier de config après
		// sans doute) Mode maximisé ?
		f.setSize(new Dimension(800, 600));
		GU.center(f);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	public JPanel getDefaultView() {
		
		// Singleton
		if (defaultView == null) {
			
			// Coupure en deux
			defaultView = new JPanel(new GridLayout(1, 2));

			// Barre de split
			JSplitPane splitpane = new JSplitPane();
			splitpane.setDividerLocation(200);
			splitpane.setOneTouchExpandable(true);

			///////////////////////////////////////
			// La sous vue à gauche (arbre + combo)
			JPanel left = new JPanel(new BorderLayout());

			// La liste des paths qu'on affichera dans le combo
			Vector paths = new Vector();
			paths.add(new File(System.getProperty("user.home")));
			for (int i = 0; i < File.listRoots().length; i++)
				paths.add(File.listRoots()[i]);
			
			final JComboBox comboPaths = new JComboBox(paths);
			comboPaths.setEditable(false);
			comboPaths.setSelectedItem(ROOT);
			// Ca sent le ComboBoxModel (favoris rajouté à la main, pan)
			
			comboPaths.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fstm.setCurrentDirectory((File) comboPaths.getSelectedItem());
				}
			});
			
			
			left.add(comboPaths, BorderLayout.NORTH);

			// L'arbre
			fstm = new FileSystemTreeModel(ROOT);
			fstgui = new FileSystemTreeGUI(fstm);
			left.add(new JScrollPane(fstgui), BorderLayout.CENTER);

			///////////////////////////////////////////////
			// La sous vue à droite avec les sous-sous-vues

			lim = new ListImagesModel(ROOT);
			ligui = new ListImagesGUI(lim);

			lgui = new ListImagesGUI(lim, true);

			ftmodel = new FileTableModel(ROOT, "prout");
			ftgui = new FileTableGUI(ftmodel);

			JTabbedPane tabs = new JTabbedPane();
			JScrollPane viewIcon = new JScrollPane(ligui,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			JScrollPane viewList = new JScrollPane(lgui,
					JScrollPane.VERTICAL_SCROLLBAR_NEVER,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			JScrollPane viewTable = new JScrollPane(ftgui,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			tabs.addTab("Icônes", ImagesMap.get("view_icon.png"), viewIcon,
					"Vue avec les icônes");
			tabs.addTab("Liste", ImagesMap.get("view_list.png"),
					viewList, "Vue en liste");
			tabs.addTab("Détails", ImagesMap.get("view_details.png"),
					viewTable, "Vue de détails");

			// Assemblage et Pan !
			splitpane.setTopComponent(left);
			splitpane.setBottomComponent(tabs);
			defaultView.add(splitpane);
		}

		return defaultView;
	}

	/**
	 * Construit la fenêtre avec en démarrage, la vue ouvert au noeud
	 * <code>uri</code> (pas le root)
	 * 
	 * @param uri
	 */
	public FSeeker(String start) {
		// TODO implémenter ça + gérer si on a passé http://pwetpwet => HTML, ou
		// (file:///)rep/truc.html local
	}

	/**
	 * Renvoie le JPanel central.
	 * 
	 * @return le panel central courant
	 */
	public JPanel getMain() {
		return main;
	}

	/**
	 * Permet de fixer le panel central.
	 * 
	 * @param main
	 *            le nouveau panel central
	 */

	public void setView(JPanel main) {
		if (this.main != null)
			f.remove(this.main);

		f.getContentPane().add(main, BorderLayout.CENTER);
		this.main = main;

		main.revalidate();
	}

	/**
	 * Renvoie la barre d'outils par défaut.
	 * 
	 * @return la barre d'outils
	 */
	private JToolBar getDefaultToolBar() {
		JToolBar tb = new JToolBar(JToolBar.HORIZONTAL);
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setVgap(0);
		tb.setLayout(layout);
		tb.setFloatable(false);

		JButton b = null;

		b = new JButton();
		b.setActionCommand("PREVIOUS");
		b.addActionListener(toolBarControler);
		b.setIcon(ImagesMap.get("previous.gif"));
		b.setToolTipText("Précédent");
		tb.add(b);

		b = new JButton();
		b.setActionCommand("NEXT");
		b.addActionListener(toolBarControler);
		b.setIcon(ImagesMap.get("next.gif"));
		b.setToolTipText("Suivant");
		tb.add(b);

		b = new JButton();
		b.setActionCommand("PARENT");
		b.addActionListener(toolBarControler);
		b.setIcon(ImagesMap.get("parent.gif"));
		b.setToolTipText("Répertoire parent");
		tb.add(b);

		tb.addSeparator();

		b = new JButton("Rechercher");
		b.setActionCommand("SEARCHVIEW");
		b.addActionListener(toolBarControler);
		b.setIcon(ImagesMap.getDefault());
		b.setToolTipText("Rechercher un fichier ou un répertoire");
		tb.add(b);

		b = new JButton();
		b.setActionCommand("TREEVIEW");
		b.addActionListener(toolBarControler);
		b.setIcon(ImagesMap.getDefault());
		b.setToolTipText("Affichage avec l'arborescence sous forme d'arbre");
		tb.add(b);

		b = new JButton();
		b.setActionCommand("MACOSVIEW");
		b.addActionListener(toolBarControler);
		b.setIcon(ImagesMap.getDefault());
		b.setToolTipText("Affichage par thème à la MacOS");
		tb.add(b);

		tb.addSeparator();

		tb.add(new JLabel("Adresse : "));
		uriModel = new URIModel(ROOT);
		ugui = new URIGUI(uriModel);
		tb.add(ugui);

		fstm.addObserver(ugui);
		uriModel.addObserver(fstgui);

		return tb;
	}

	/**
	 * Renvoie la barre d'état par défaut.
	 * 
	 * @return la barre d'état
	 */
	private JToolBar getDefaultStatusBar() {
		JToolBar sb = new JToolBar();
		JPanel p = new JPanel(new GridLayout(1, 2));
		sb.add(p);
		JLabel l = new JLabel("FSeeker " + VERSION);
		p.add(l);
		final JProgressBar pb = new JProgressBar(0, 100);

		sb.addSeparator(new Dimension(10, 10));
		Timer t = new Timer();
		new Timer().schedule(new TimerTask() {
			public void run() {
				if (pb.getValue() == 100)
					pb.setValue(0);
				else
					pb.setValue(pb.getValue() + 1);
			}
		}, 0, 100);
		p.add(pb);
		sb.setFloatable(false);
		return sb;

	}

	/**
	 * Renvoie la barre de menu par défaut.
	 * 
	 * @return la barre de menu
	 */
	private JMenuBar getDefaultMenuBar() {
		JMenuBar mb = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;

		menu = new JMenu("Fichier");
		menuItem = new JMenuItem("Quitter", KeyEvent.VK_Q);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				f.dispose();
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
		mb.add(Box.createHorizontalGlue());

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
			public void actionPerformed(ActionEvent e) {
				new About(f);
			}
		});
		menu.add(menuItem);
		mb.add(menu);

		return mb;
	}

	/**
	 * Démarrage absolu de l'application.
	 * 
	 * @param args
	 *            URI de départ
	 */
	public static void main(String args[]) {
		FSeeker fs = null;

		if (args.length == 0) {
			fs = new FSeeker();
		} else {
			fs = new FSeeker(args[0]);
		}

		new FSeekerControler(fs);
	}

}