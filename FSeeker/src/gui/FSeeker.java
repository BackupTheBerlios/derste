package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import misc.GU;
import model.FSeekerModel;
import model.FileSystemTreeModel;
import model.FileTableModel;
import model.ListImagesModel;

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

	private JPanel subleft = null;

	private JPanel left = null;

	/** Le combobox du panel à gauche (arbo, infos, recherche etc) */
	private JComboBox cb = null;

	private FileSystemTreeGUI fstgui = null;

	private JPanel searchgui = new JPanel();

	/** Construit la fenêtre par défaut de FSeeker */
	public FSeeker(FSeekerModel fsm) {
		super("FSeeker v" + VERSION);
		this.fsm = fsm;

		Container cp = getContentPane();

		ToolTipManager.sharedInstance().setInitialDelay(1500);
		ToolTipManager.sharedInstance().setReshowDelay(1000);

		initModelsAndGuis();
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

	public FSeekerModel getModel() {
		return fsm;
	}

	public void setSubLeftPanel(JPanel p) {
		if (subleft != null)
			left.remove(subleft);
		subleft = p;
		left.add(subleft, BorderLayout.CENTER);
		left.revalidate();
		left.repaint();
	}

	public JPanel getLeftPanel() {
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

	private JPanel getSubLeftPanel() {
		JPanel p = new JPanel(new BorderLayout());

		if ("Arborescence".equals(cb.getSelectedItem())) {
			p.add(fstgui);
		} else if ("Recherche".equals(cb.getSelectedItem())) {
			p.add(searchgui);
		}

		return p;
	}

	private void initModelsAndGuis() {
		// L'arbre
		FileSystemTreeModel fstm = new FileSystemTreeModel(fsm);
		fstgui = new FileSystemTreeGUI(fstm);
	}

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

		JTabbedPane tabs = new JTabbedPane();

		JScrollPane viewIcon = new JScrollPane(ligui);
		JScrollPane viewList = new JScrollPane(lgui);
		JScrollPane viewTable = new JScrollPane(ftgui);
		JScrollPane viewTableSpecial = new JScrollPane(ftguiSpecial);

		tabs.addTab("Icônes", null/* ImagesMap.get("view_icon.png") */,
				viewIcon, "Vue avec les icônes");
		tabs.addTab("Liste", null/* ImagesMap.get("view_list.png") */,
				viewList, "Vue en liste");
		tabs.addTab("Détails", null/* ImagesMap.get("view_details.png") */,
				viewTable, "Vue de détails");
		tabs.addTab("Détails", null /* ImagesMap.get("view_details.png") */,
				viewTableSpecial, "Vue Spéciale");

		// Assemblage et Pan !
		splitpane.setTopComponent(getLeftPanel());
		splitpane.setBottomComponent(tabs);
		defaultView.add(splitpane);

		return defaultView;
	}

	/**
	 * Construit la fenêtre avec en démarrage, la vue ouvert au noeud
	 * <code>uri</code> (pas le root)
	 * 
	 * @param uri
	 */
	/*
	 * TODO public FSeeker(FSeekerModel fsm, String start) { this.fsm = fsm;
	 * fsm.setURI(new File(start)); }
	 */

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
	}

}