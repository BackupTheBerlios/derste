package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import misc.GU;
import misc.ImagesMap;
import model.FileSystemModel;
import model.ListImagesModel;
import controler.ToolBarControler;

/*
 * Created on 15 oct. 2004
 */

/**
 * Classe de lancement de FSeeker.
 * 
 * @author Sted
 */
public class FSeeker extends JFrame {
    /** La version du software */
    public final static String VERSION = "0.1a";

    /** Le contrôleur de la barre d'outils */
    private final static ToolBarControler toolBarControler = new ToolBarControler();
    
    /** Le panel central, principal, destinées à accueillir les différentes vues */
    private JPanel main = new JPanel();

    /** Construit la fenêtre par défaut de FSeeker */
    public FSeeker() {
        super("FSeeker v" + VERSION);
        Container cp = getContentPane();

        GU.changeLF();
        setJMenuBar(getDefaultMenuBar());

		main.setLayout(new GridLayout(1, 2));
	
		// Foutre ça complétement à part après
		JTree tree = new JTree(new FileSystemModel("c:\\"));
		final ListImagesModel model = new ListImagesModel(new File("c:\\"));
		ListImagesGUI jli = new ListImagesGUI(model);
		
		JPanel rightPanel = new JPanel(new BorderLayout());
	    rightPanel.add(jli);
	
		
		JSplitPane sp = new JSplitPane();
		JScrollPane scrollTop = new JScrollPane(tree);
		JScrollPane scrollBottom = new JScrollPane(rightPanel);
		
		// Propriétés de la barre de split
		sp.setDividerLocation(200);
		sp.setOneTouchExpandable(true);
		
		sp.setTopComponent(scrollTop);
		sp.setBottomComponent(scrollBottom);

		main.add(sp);

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
			    File f = (File) e.getPath().getLastPathComponent();
				model.setDirectory(f);					
			}
		});
		////////////////////////////
        
        // Un border pour toolbar (en haut), statusbar (en bas), main (central)
        cp.setLayout(new BorderLayout());
        cp.add(getDefaultToolBar(), BorderLayout.NORTH);
        cp.add(main, BorderLayout.CENTER);
        cp.add(getDefaultStatusBar(), BorderLayout.SOUTH);

        // Une taille raisonnable au départ (dans un fichier de config après
        // sans doute)
        setSize(new Dimension(640, 480));

        GU.center(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Construit la fenêtre avec en démarrage, la vue ouvert au noeud
     * <code>uri</code> (pas le root)
     * 
     * @param uri
     */
    public FSeeker(String uri) {
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
    public void setMain(JPanel main) {
        this.main = main;
        invalidate();
        validate();
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
        b.setBorder(null);
        b.setActionCommand("PREVIOUS");
        b.addActionListener(toolBarControler);
        b.setIcon(ImagesMap.get("previous.gif"));
        b.setToolTipText("Précédent");
        tb.add(b);
        
        b = new JButton();
        b.setBorder(null);
        b.setActionCommand("NEXT");
        b.addActionListener(toolBarControler);
        b.setIcon(ImagesMap.get("next.gif"));
        b.setToolTipText("Suivant");
        tb.add(b);
        
        b = new JButton();
        b.setBorder(null);
        b.setActionCommand("PARENT");
        b.addActionListener(toolBarControler);
        b.setIcon(ImagesMap.get("parent.gif"));
        b.setToolTipText("Répertoire parent");
        tb.add(b);
        
        tb.addSeparator();
        
        b = new JButton("Rechercher");
        tb.add(b);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel p = new JPanel(new BorderLayout());
				
				JPanel psearch = new JPanel();
				psearch.add(new JLabel("Recherche"));
				psearch.add(new JTextField(20));
				
				p.add(psearch, BorderLayout.WEST);
				p.add(new JTextArea(20, 20), BorderLayout.EAST);
				System.out.println("on met " + p);
				setMain(p);
			}
		});
        
		b = new JButton();
        b.setActionCommand("AFFICHAGE");
        b.addActionListener(toolBarControler);
        b.setIcon(ImagesMap.get("dot.gif"));
        b.setToolTipText("Affichage");
        tb.add(b);

        tb.addSeparator(new Dimension(10, 10));
        tb.add(new JLabel("URI : "));
        tb.add(new JTextField(10));

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
		
		/*StatusBar sb = new StatusBar();
        JLabel l = sb.addLabel("FSeeker " + VERSION);
        GU.setIcon(l, "dot.jpg");
        sb.addProgressBar(0, 100);
        return sb;*/
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
                new About(FSeeker.this);
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
        if (args.length == 0)
            new FSeeker();
        else
            new FSeeker(args[0]);
    }

}