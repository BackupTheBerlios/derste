package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import misc.GU;
import misc.ImagesMap;
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

    /** Singleton contenant la vue principale par défaut */
    private JPanel defaultView = null;

    /** Le panel central, principal, destinées à accueillir les différentes vues */
    private JPanel main = null;

    /** Construit la fenêtre par défaut de FSeeker */
    public FSeeker(FSeekerModel fsm) {
    	super("FSeeker v" + VERSION);
    	this.fsm = fsm;

    	Container cp = getContentPane();

        ToolTipManager.sharedInstance().setInitialDelay(1500);
        ToolTipManager.sharedInstance().setReshowDelay(1000);

        // GU.changeLF();
        setJMenuBar(new MenuBarGUI(this));

        // La vue par défaut (tree + icônes/détails/liste/etc.)
        setView(getDefaultView());

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

    public JPanel getDefaultView() {

        // Singleton
        if (defaultView == null) {

            // Coupure en deux
            defaultView = new JPanel(new GridLayout(1, 2));

            // Barre de split
            JSplitPane splitpane = new JSplitPane();
            splitpane.setDividerLocation(200);

            // Des look 'n feels balance une NullPointerException avec ça. :D
            // ie: Tonic
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
                    fsm.setURI((File) comboPaths.getSelectedItem());
                }
            });

            left.add(comboPaths, BorderLayout.NORTH);

            // L'arbre
            FileSystemTreeModel fstm = new FileSystemTreeModel(fsm);
            FileSystemTreeGUI fstgui = new FileSystemTreeGUI(fstm);

            left.add(new JScrollPane(fstgui), BorderLayout.CENTER);

            ///////////////////////////////////////////////
            // La sous vue à droite avec les sous-sous-vues

            ListImagesModel lim = new ListImagesModel(fsm);
            ListImagesGUI ligui = new ListImagesGUI(lim);

            ListImagesGUI lgui = new ListImagesGUI(lim, true);

            //La vue détail (mode normal)
            FileTableModel ftmodel = new FileTableModel(fsm, FileTableModel.SIMPLE_MODE);
            FileTableGUI ftgui = new FileTableGUI(ftmodel);

            //La vue spéciale (style MacOs)
            FileTableModel ftmodelSpecial = new FileTableModel(fsm,
                    FileTableModel.SPECIAL_MODE);
            FileTableGUI ftguiSpecial = new FileTableGUI(ftmodelSpecial);

            JDesktopPane desktop = new JDesktopPane();
            JLabel l = new JLabel("pwet");
            l.setIcon(ImagesMap.getDefault());
            l.setSize(100, 100);
            l.setLocation(70, 70);
            desktop.add(l);
            l.setVisible(true);

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
            JScrollPane viewTableSpecial = new JScrollPane(ftguiSpecial,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            tabs.addTab("Icônes", null/*ImagesMap.get("view_icon.png")*/, viewIcon,
                    "Vue avec les icônes");
            tabs.addTab("Liste", null/*ImagesMap.get("view_list.png")*/, viewList,
                    "Vue en liste");
            tabs.addTab("Détails", null/*ImagesMap.get("view_details.png")*/,
                    viewTable, "Vue de détails");
            tabs.addTab("Détails", null /*ImagesMap.get("view_details.png")*/,
                    viewTableSpecial, "Vue Spéciale");

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

    public void setView(JPanel main) {
        if (this.main != null)
            remove(this.main);

        getContentPane().add(main, BorderLayout.CENTER);
        this.main = main;

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