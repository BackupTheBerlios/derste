package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;

import misc.GU;
import misc.ImagesMap;
import misc.LookAndFeels;
import misc.PopupManager;
import model.FSeekerModel;
import model.FileSystemTreeModel;
import model.FileTableModel;
import model.ListImagesModel;
import model.URIModel;
import controler.ToolBarControler;

/*
 * Created on 15 oct. 2004
 */

/**
 * Classe de lancement de FSeeker.
 * 
 * @author Sted
 * @author brahim
 */

public class FSeeker {

    /** La version du software */
    public final static String VERSION = "0.1b";

    private final static File ROOT = ((System.getProperty("os.name"))
            .indexOf("dows") >= 0 ? File.listRoots()[1] : File.listRoots()[0]);

    private JFrame f = null;

    private FSeekerModel fsm = null;

    private FileSystemTreeModel fstm = null;

    private ListImagesModel lim = null;

    private URIModel uriModel = null;

    private FileSystemTreeGUI fstgui = null;

    private ListImagesGUI ligui = null;

    private ListImagesGUI lgui = null;

    /* Vue table normal */
    private FileTableGUI ftgui = null;

    /* Vue sp�ciale */
    private FileTableGUI ftguiSpecial = null;

    /* Mod�le pour la vue normal */
    private FileTableModel ftmodel = null;

    /* Mod�le pour la vue sp�cial */
    private FileTableModel ftmodelSpecial = null;

    private URIGUI ugui = null;

    //private JPopupMenu popup = null;

    /** Singleton contenant la vue principale par d�faut */
    private JPanel defaultView = null;

    /** Singleton contenant la vue � la "macos" */
    private JPanel macosView = null;

    /** Le contr�leur de la barre d'outils */
    private ToolBarControler toolBarControler = null;

    /** Le panel central, principal, destin�es � accueillir les diff�rentes vues */
    private JPanel main = null;

    /** Construit la fen�tre par d�faut de FSeeker */
    public FSeeker(FSeekerModel fsm) {
        this.fsm = fsm;

        f = new JFrame("FSeeker v" + VERSION);
        Container cp = f.getContentPane();

        toolBarControler = new ToolBarControler(this, fsm);

        ToolTipManager.sharedInstance().setInitialDelay(1500);
        ToolTipManager.sharedInstance().setReshowDelay(1000);

        // GU.changeLF();
        f.setJMenuBar(getDefaultMenuBar());

        // La vue par d�faut (tree + ic�nes/d�tails/liste/etc.)
        setView(getDefaultView());

        // Un border pour toolbar (en haut), statusbar (en bas), main (central)
        cp.setLayout(new BorderLayout());
        cp.add(getDefaultToolBar(), BorderLayout.NORTH);
        cp.add(main, BorderLayout.CENTER);
        cp.add(getDefaultStatusBar(), BorderLayout.SOUTH);

        // Une taille raisonnable au d�part (dans un fichier de config apr�s
        // sans doute) Mode maximis� ?
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

            // Des look 'n feels balance une NullPointerException avec �a. :D
            // ie: Tonic
            splitpane.setOneTouchExpandable(true);

            ///////////////////////////////////////
            // La sous vue � gauche (arbre + combo)
            JPanel left = new JPanel(new BorderLayout());

            // La liste des paths qu'on affichera dans le combo
            Vector paths = new Vector();
            paths.add(new File(System.getProperty("user.home")));
            for (int i = 0; i < File.listRoots().length; i++)
                paths.add(File.listRoots()[i]);

            final JComboBox comboPaths = new JComboBox(paths);
            comboPaths.setEditable(false);
            comboPaths.setSelectedItem(ROOT);
            // Ca sent le ComboBoxModel (favoris rajout� � la main, pan)

            comboPaths.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fsm.setURI((File) comboPaths.getSelectedItem());
                }
            });

            left.add(comboPaths, BorderLayout.NORTH);

            // L'arbre
            fstm = new FileSystemTreeModel(fsm);
            fstgui = new FileSystemTreeGUI(fstm);

            left.add(new JScrollPane(fstgui), BorderLayout.CENTER);

            ///////////////////////////////////////////////
            // La sous vue � droite avec les sous-sous-vues

            lim = new ListImagesModel(fsm);
            ligui = new ListImagesGUI(lim);

            lgui = new ListImagesGUI(lim, true);

            //La vue d�tail (mode normal)
            ftmodel = new FileTableModel(fsm, FileTableModel.SIMPLE_MODE);
            ftgui = new FileTableGUI(ftmodel);

            //La vue sp�ciale (style MacOs)
            ftmodelSpecial = new FileTableModel(fsm,
                    FileTableModel.SPECIAL_MODE);
            ftguiSpecial = new FileTableGUI(ftmodelSpecial);

            JDesktopPane desktop = new JDesktopPane();
            JLabel l = new JLabel("pwet");
            l.setIcon(ImagesMap.getDefault());
            l.setSize(100, 100);
            l.setLocation(70, 70);
            desktop.add(l);
            l.setVisible(true);

            MJInternalFrame in = new MJInternalFrame("test", false, false,
                    false, false);
            MJInternalFrame in2 = new MJInternalFrame("test", false, false,
                    false, false);
            MJInternalFrame in3 = new MJInternalFrame("test", false, false,
                    false, false);

            desktop.add(in);
            desktop.add(in2);
            desktop.add(in3);
            desktop.setVisible(true);

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

            tabs.addTab("Test", null, new JScrollPane(desktop));
            tabs.addTab("Ic�nes", ImagesMap.get("view_icon.png"), viewIcon,
                    "Vue avec les ic�nes");
            tabs.addTab("Liste", ImagesMap.get("view_list.png"), viewList,
                    "Vue en liste");
            tabs.addTab("D�tails", ImagesMap.get("view_details.png"),
                    viewTable, "Vue de d�tails");
            tabs.addTab("D�tails", ImagesMap.get("view_details.png"),
                    viewTableSpecial, "Vue Speciale");

            // Assemblage et Pan !
            splitpane.setTopComponent(left);
            splitpane.setBottomComponent(tabs);
            defaultView.add(splitpane);
        }

        return defaultView;
    }

    public class MJInternalFrame extends JInternalFrame {
        public final Icon image = ImagesMap.getDefault();

        public MJInternalFrame(String titre, boolean a, boolean b, boolean c,
                boolean d) {
            super(titre, a, b, c, d);
            setSize(getPreferredSize());
            setVisible(true);
        }

        protected void paintComponent(Graphics g) {
            image.paintIcon(this, g, 0, 0);
            g.drawString("test", 0, image.getIconHeight() + 10);
        }

        protected void paintBorder(Graphics g) {
            return;
        }

        protected void paintChildren(Graphics g) {
            return;
        }

        public void paintComponents(Graphics g) {
            return;
        }

        public Dimension getPreferredSize() {
            return new Dimension(image.getIconWidth(),
                    image.getIconHeight() + 20);
        }

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

    public void setView(JPanel main) {
        if (this.main != null)
            f.remove(this.main);

        f.getContentPane().add(main, BorderLayout.CENTER);
        this.main = main;

        main.repaint();
        main.revalidate();
    }

    /**
     * Renvoie la barre d'outils par d�faut.
     * 
     * @return la barre d'outils
     */
    private JToolBar getDefaultToolBar() {
        JToolBar tb = new JToolBar(JToolBar.HORIZONTAL);
        LayoutManager layout = new BorderLayout();

        tb.setLayout(layout);
        tb.setFloatable(false);
        tb.setRollover(true);

        JPanel p = null;
        JButton b = null;

        p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        b = new JButton();
        b.setActionCommand("PREVIOUS");
        b.addActionListener(toolBarControler);
        b.setBorder(null);
        b.setIcon(ImagesMap.get("previous.gif"));
        b.setToolTipText("Pr�c�dent");
        p.add(b);

        b = new JButton();
        b.setActionCommand("NEXT");
        b.addActionListener(toolBarControler);
        b.setBorder(null);
        b.setIcon(ImagesMap.get("next.gif"));
        b.setToolTipText("Suivant");
        p.add(b);

        b = new JButton();
        b.setActionCommand("PARENT");
        b.addActionListener(toolBarControler);
        b.setBorder(null);
        b.setIcon(ImagesMap.get("parent.gif"));
        b.setToolTipText("R�pertoire parent");
        p.add(b);

        b = new JButton("Rechercher");
        b.setActionCommand("SEARCHVIEW");
        b.addActionListener(toolBarControler);
        b.setBorder(null);
        b.setIcon(ImagesMap.getDefault());
        b.setToolTipText("Rechercher un fichier ou un r�pertoire");
        p.add(b);

        b = new JButton();
        b.setActionCommand("TREEVIEW");
        b.addActionListener(toolBarControler);
        b.setBorder(null);
        b.setIcon(ImagesMap.getDefault());
        b.setToolTipText("Affichage avec l'arborescence sous forme d'arbre");
        p.add(b);

        b = new JButton();
        b.setActionCommand("MACOSVIEW");
        b.addActionListener(toolBarControler);
        b.setBorder(null);
        b.setIcon(ImagesMap.getDefault());
        b.setToolTipText("Affichage par th�me � la MacOS");
        p.add(b);
        tb.add(p, BorderLayout.NORTH);

        uriModel = new URIModel(fsm);
        ugui = new URIGUI(uriModel);
        tb.add(ugui, BorderLayout.SOUTH);

        return tb;
    }

    /**
     * Renvoie la barre d'�tat par d�faut.
     * 
     * @return la barre d'�tat
     */
    private JToolBar getDefaultStatusBar() {
        class TestToolBar extends JToolBar implements Observer {
            protected JLabel l = new JLabel("FSeeker " + VERSION);
            
        	public TestToolBar() {
        		fsm.addObserver(this);
                add(l);
                setFloatable(false);
        	}
        	
			public void update(Observable o, Object arg) {
				if (fsm.isChanged(FSeekerModel.SELECTION)) 
					l.setText(fsm.getSelection().toString());
			}
        }

        /*Timer t = new Timer();
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (pb.getValue() == 100)
                    pb.setValue(0);
                else
                    pb.setValue(pb.getValue() + 1);
            }
        }, 0, 100);*/
        
        return new TestToolBar();

    }

    /**
     * Renvoie la barre de menu par d�faut.
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
        
        
        mb.add(menu);

        PopupManager pm = new PopupManager(null, fsm);
        menu = pm.getDisplay();
        menu.add(LookAndFeels.getLookAndFeelsMenu(f));
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