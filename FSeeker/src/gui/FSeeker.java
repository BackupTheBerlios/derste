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

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import preview.PreviewManager;

import misc.GU;
import misc.PopupManager;
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

	/** Le supra-mod�le */
	private FSeekerModel fsm = null;

	/** Le panel central, principal, destin�es � accueillir les diff�rentes vues */
	private JPanel main = null;

	private JComponent subleft = null;

	private JPanel left = null;

	/** Le combobox du panel � gauche (arbo, infos, recherche etc) */
	private JComboBox cb = null;

	private FileSystemTreeGUI fstgui = null;

	private SearchGUI searchgui = null;
	
	private PreviewManager preview = null;

	private JList bookmarksgui = null;
	
	private JTabbedPane tabs = null;

	/** Construit la fen�tre par d�faut de FSeeker */
	public FSeeker(FSeekerModel fsm) {
		super("FSeeker v" + VERSION);
		this.fsm = fsm;

		Container cp = getContentPane();

		ToolTipManager.sharedInstance().setInitialDelay(1500);
		ToolTipManager.sharedInstance().setReshowDelay(1000);

		initModelsAndGuis();
		setJMenuBar(new MenuBarGUI(this));

		// La vue par d�faut (tree + ic�nes/d�tails/liste/etc.)
		setMain(getDefaultView());

		// Un border pour toolbar (en haut), statusbar (en bas), main (central)
		cp.setLayout(new BorderLayout());
		cp.add(new ToolBarGUI(this), BorderLayout.NORTH);
		cp.add(main, BorderLayout.CENTER);
		cp.add(new StatusBarGUI(this), BorderLayout.SOUTH);

		// Une taille raisonnable au d�part (dans un fichier de config apr�s
		// sans doute) Mode maximis� ?
		setSize(new Dimension(800, 600));
		GU.center(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public FSeekerModel getModel() {
		return fsm;
	}

	public void addTab(String title, Icon icon, Component component, String tip) {
		tabs.addTab(title, icon, component, tip);
		tabs.setSelectedComponent(component);
	}

	public void setSubLeftPanel(JComponent c) {
		if (subleft != null)
			left.remove(subleft);
		subleft = new JScrollPane(c);
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
			searchgui.setSearchPath(fsm.getURI());
			p = searchgui;
		}else if("Information".equals(cb.getSelectedItem())){
		    preview = new PreviewManager(fsm);
		    preview.update(fsm, null);// Appel explicite sinon il faut cr�er
		    		//la preview d�s le lancement de l'application
		    p = preview;			
		} else if ("Favoris".equals(cb.getSelectedItem())) {
			p.add(bookmarksgui);
		}

		return p;
	}

	private void initModelsAndGuis() {
		// L'arbre
		FileSystemTreeModel fstm = new FileSystemTreeModel(fsm);
		fstgui = new FileSystemTreeGUI(fstm);

		// Recherche
		searchgui = new SearchGUI(this);
		
		// Bookmarks
		bookmarksgui = new BookmarksGUI();
	}

	public JPanel getDefaultView() {
		// Coupure en deux
		JPanel defaultView = new JPanel(new GridLayout(1, 2));

		// Barre de split
		JSplitPane splitpane = new JSplitPane();
		splitpane.setDividerLocation(200);

		// Des look 'n feels balance une NullPointerException avec �a. :D
		// ie: Tonic
		splitpane.setOneTouchExpandable(true);

		///////////////////////////////////////////////
		// La sous vue � droite avec les sous-sous-vues

		ListImagesModel lim = new ListImagesModel(fsm);
		ListImagesGUI ligui = new ListImagesGUI(lim);

		ListImagesGUI lgui = new ListImagesGUI(lim, true);

		// La vue d�tail (mode normal)
		FileTableModel ftmodel = new FileTableModel(fsm,
				FileTableModel.SIMPLE_MODE);
		FileTableGUI ftgui = new FileTableGUI(ftmodel);

		// La vue sp�ciale (style MacOs)
		FileTableModel ftmodelSpecial = new FileTableModel(fsm,
				FileTableModel.SPECIAL_MODE);
		FileTableGUI ftguiSpecial = new FileTableGUI(ftmodelSpecial);

		tabs = new JTabbedPane();
		tabs.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					if (tabs.getTitleAt(tabs.getSelectedIndex()).indexOf("Recherche") != 0)
						return;
					
					JPopupMenu popup = new JPopupMenu();
					popup.add(PopupManager.createMenuItem("Fermer", new ActionListener() {
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

		tabs.addTab("Ic�nes", null/* ImagesMap.get("view_icon.png") */,
				viewIcon, "Vue avec les ic�nes");
		tabs.addTab("Liste", null/* ImagesMap.get("view_list.png") */,
				viewList, "Vue en liste");
		tabs.addTab("D�tails", null/* ImagesMap.get("view_details.png") */,
				viewTable, "Vue de d�tails");
		tabs.addTab("D�tails", null /* ImagesMap.get("view_details.png") */,
				viewTableSpecial, "Vue Sp�ciale");

		// Assemblage et Pan !
		splitpane.setTopComponent(getLeftPanel());
		splitpane.setBottomComponent(tabs);
		defaultView.add(splitpane);

		return defaultView;
	}

	/**
	 * Construit la fen�tre avec en d�marrage, la vue ouvert au noeud
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
	 * D�marrage absolu de l'application.
	 * 
	 * @param args
	 *            URI de d�part
	 */
	public static void main(String args[]) {
		FSeekerModel fsm = new FSeekerModel(ROOT);
		new FSeeker(fsm);
	}

}



