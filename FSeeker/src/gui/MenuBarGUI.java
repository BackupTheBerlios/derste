/*
 * Created on 6 nov. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import misc.LookAndFeels;
import misc.PopupManager;

/**
 * @author sted
 */
public class MenuBarGUI extends JMenuBar {

    /**
     * Renvoie la barre de menu par défaut.
     * 
     * @return la barre de menu
     */
    public MenuBarGUI(final FSeeker fs) {
        JMenu menu;
        JMenuItem menuItem;

        menu = new JMenu("Fichier");
        menuItem = new JMenuItem("Quitter", KeyEvent.VK_Q);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                fs.dispose();
                System.exit(0);
            }
        });
        menu.add(menuItem);
        add(menu);

        /*menu = new JMenu("Edition");
        menuItem = new JMenuItem("Annuler", KeyEvent.VK_C);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("annuler");
            }
        });
        menu.add(menuItem);
        menu.addSeparator();*/
        
        
        add(menu);

        PopupManager pm = new PopupManager(null, fs.getModel());
        menu = pm.getDisplay();
        menu.add(LookAndFeels.getLookAndFeelsMenu(fs));
        add(menu);

        add(Box.createHorizontalGlue());
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
                new About(fs);
            }
        });
        menu.add(menuItem);
        add(menu);
    }
	
}
