package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import misc.GU;
import misc.ImagesMap;
import misc.PopupManager;
import model.FSeekerModel;
import model.FileSystemTreeModel;
import model.FileTableModel;
import model.ListImagesModel;
import preview.PreviewManager;

/*
 * Created on 15 oct. 2004
 */

/**
 * Classe de lancement de FSeeker.
 * 
 * @author Sted
 * @author brahim
 */

public class FSeeker extends JFrame {

	/** La version du software */
	public final static String VERSION = "0.1b";

	/** La racine */
	private final static File ROOT = ((System.getProperty("os.name"))
			.indexOf("dows") >= 0 ? File.listRoots()[1] : File.listRoots()[0]);

	/** Le supra-modèle */
	private FSeekerModel fsm = null;

	/** Le panel central, principal, destinées à accueillir les différentes vues */
	private JPanel main = null;

	/** Le composant sous le combobox à gauche (favoris, arbo etc) */
	private JComponent subleft = null;

	/** Le panel avec le combo + le subleft */
	private JPanel left = null;

	/** Le combobox du panel à gauche (arbo, infos, recherche etc) */
	private JComboBox cb = null;

	/** L'arbre dans le subleft */
	private FileSystemTreeGUI fstgui = null;

	/** Le panel de recherche dans le subleft */
	private SearchGUI searchgui = null;

	/** Le panel de preview dans le subleft */
	private PreviewManager preview = null;

	/** Le panel de bookmark dans le subleft */
	private BookmarksGUI bookmarksgui = null;

	/** Les tabulations dans la vue de droite */
	private JTabbedPane tabs = null;

	protected static Preferences pref = Preferences.userRoot();

	/** Construit la fenêtre par défaut de FSeeker */
	public FSeeker(FSeekerModel fsm) {
		super("FSeeker v" + VERSION);
		this.fsm = fsm;

		Container cp = getContentPane();

		// Préférences
		ToolTipManager.sharedInstance().setEnabled(
				pref.getBoolean("tooltips", true));
		ToolTipManager.sharedInstance().setInitialDelay(1500);
		ToolTipManager.sharedInstance().setReshowDelay(1500);

		setJMenuBar(new MenuBarGUI(this));

		// La vue par défaut (tree + icônes/détails/liste/etc.)
		setMain(getDefaultView());

		// Un border pour toolbar (en haut), statusbar (en bas), main (central)
		cp.setLayout(new BorderLayout());
		cp.add(new ToolBarGUI(this), BorderLayout.NORTH);
		cp.add(main, BorderLayout.CENTER);
		cp.add(new StatusBarGUI(this), BorderLayout.SOUTH);

		// Une taille raisonnable au départ (dans un fichier de config après
		// sans doute) Mode maximisé ?
		setSize(new Dimension(800, 600));
		GU.center(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Retourne le supra-modèle
	 * 
	 * @return le supra-modèle
	 */
	public FSeekerModel getModel() {
		return fsm;
	}

	public JComboBox getCB() {
		return cb;
	}

	/**
	 * Ajoute une tabulation.
	 * 
	 * @param title
	 *            titre de la tabulation
	 * @param icon
	 *            icône de la tabulation
	 * @param component
	 *            le composant contenu dans la tabulation
	 * @param tip
	 *            le tip associé
	 */
	public void addTab(String title, Icon icon, Component component, String tip) {
		tabs.addTab(title, icon, component, tip);
		tabs.setSelectedComponent(component);
	}

	/**
	 * Retourne le gui des bookmarks.
	 * 
	 * @return le gui des bookmarks.
	 */
	public BookmarksGUI getBookmarksGUI() {
		return bookmarksgui;
	}

	/**
	 * Change la sous-vue de gauche (favoris, recherche etc) en affichant le
	 * composant c.
	 * 
	 * @param c
	 *            le composant
	 */
	public void setSubLeftPanel(JComponent c) {
		if (subleft != null)
			left.remove(subleft);
		subleft = new JScrollPane(c);
		left.add(subleft, BorderLayout.CENTER);
		left.revalidate();
		left.repaint();
	}

	/**
	 * Retourne le panel de gauche (combo + sous-vue).
	 * 
	 * @return le panel de gauche
	 */
	public JPanel getLeftPanel() {
		// Initialisation des différents modèles et différentes vues

		// L'arbre
		FileSystemTreeModel fstm = new FileSystemTreeModel(fsm);
		fstgui = new FileSystemTreeGUI(fstm);

		// Recherche
		searchgui = new SearchGUI(this);

		// Bookmarks
		bookmarksgui = new BookmarksGUI(fsm);

		// La prévisualisation
		preview = new PreviewManager(fsm);
		// Appel explicite sinon il faut créer la preview dés le lancement de
		// l'application
		preview.update(fsm, null);

		// Création du combo

		left = new JPanel(new BorderLayout());

		// Pas un [] car, le jcombobox transforme en vector..! alors autant
		// directement faire avec
		Vector choices = new Vector(Arrays.asList(new String[] {
				"Arborescence", "Recherche", "Favoris", "Information" }));
		cb = new JComboBox(choices);

		cb.setSelectedIndex(0);
		cb.setEditable(false);
		cb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					setSubLeftPanel(getSubLeftPanel());
			}
		});

		left.add(cb, BorderLayout.NORTH);
		setSubLeftPanel(getSubLeftPanel());

		return left;
	}

	/**
	 * Retourne le sous-panel à gauche (favoris, arborescence etc).
	 * 
	 * @return le sous-panel à gauche
	 */
	private JPanel getSubLeftPanel() {
		JPanel p = null;

		if ("Arborescence".equals(cb.getSelectedItem())) {
			p = new JPanel(new BorderLayout());
			p.add(fstgui);
		} else if ("Recherche".equals(cb.getSelectedItem())) {
			searchgui.setSearchPath(fsm.getURI());
			p = searchgui;
		} else if ("Information".equals(cb.getSelectedItem())) {
			p = preview;
		} else if ("Favoris".equals(cb.getSelectedItem())) {
			p = new JPanel(new BorderLayout());
			p.add(bookmarksgui);
		}

		return p;
	}

	/**
	 * Retourne la vue de droite par défaut. (tab avec toutes les vues
	 * intégrées.
	 * 
	 * @return la vue de droite
	 */
	public JPanel getDefaultView() {
		// Coupure en deux
		JPanel defaultView = new JPanel(new GridLayout(1, 2));

		// Barre de split
		JSplitPane splitpane = new JSplitPane();
		splitpane.setDividerLocation(200);

		// Des look 'n feels balance une NullPointerException avec ça. :D
		// ie: Tonic
		splitpane.setOneTouchExpandable(true);

		///////////////////////////////////////////////
		// La sous vue à droite avec les sous-sous-vues

		ListImagesModel lim = new ListImagesModel(fsm);
		ListImagesGUI ligui = new ListImagesGUI(lim);

		ListImagesGUI lgui = new ListImagesGUI(lim, true);

		// La vue détail (mode normal)
		FileTableModel ftmodel = new FileTableModel(fsm,
				FileTableModel.SIMPLE_MODE);
		FileTableGUI ftgui = new FileTableGUI(ftmodel);

		// La vue spéciale (style MacOs)
		FileTableModel ftmodelSpecial = new FileTableModel(fsm,
				FileTableModel.SPECIAL_MODE);
		FileTableGUI ftguiSpecial = new FileTableGUI(ftmodelSpecial);

		tabs = new JTabbedPane();
		tabs.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					if (tabs.getTitleAt(tabs.getSelectedIndex()).indexOf(
							"Recherche") != 0)
						return;

					JPopupMenu popup = new JPopupMenu();
					popup.add(PopupManager.createMenuItem("Fermer",
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									tabs.remove(tabs.getSelectedIndex());
								}
							}));
					PopupManager.showPopup(e, popup);
				}
			}
		});

		JScrollPane viewIcon = new JScrollPane(ligui);
		JScrollPane viewList = new JScrollPane(lgui);
		JScrollPane viewTable = new JScrollPane(ftgui);
		JScrollPane viewTableSpecial = new JScrollPane(ftguiSpecial);

		tabs.addTab("Icônes", ImagesMap.get16x16("view_icon.png"), viewIcon,
				"Vue avec les icônes");
		tabs.addTab("Liste", ImagesMap.get16x16("view_list.png"), viewList,
				"Vue en liste");
		tabs.addTab("Détails", ImagesMap.get16x16("view_details.png"),
				viewTable, "Vue de détails");
		tabs.addTab("Développement", ImagesMap
				.get16x16("view_developpement.png"), viewTableSpecial,
				"Vue en développement");

		// Assemblage et Pan !
		splitpane.setTopComponent(getLeftPanel());
		splitpane.setBottomComponent(tabs);
		defaultView.add(splitpane);

		return defaultView;
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

	public void setMain(JPanel main) {
		this.main = main;
		getContentPane().add(main, BorderLayout.CENTER);
		main.repaint();
		main.revalidate();
	}

	/**
	 * Démarrage absolu de l'application.
	 * 
	 * @param args
	 *            URI de départ
	 */
	public static void main(String args[]) {
		FSeekerModel fsm = new FSeekerModel(ROOT);
		new FSeeker(fsm);
		if (args.length > 0)
			fsm.setURI(new File(args[0]));
	}

}

