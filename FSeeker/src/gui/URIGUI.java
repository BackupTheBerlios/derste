package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextField;

import model.FileSystemTreeModel;
import model.URIModel;

/**
 * Repr�sente l'URI graphiquement.
 * 
 * @author derosias
 */
public class URIGUI extends JTextField implements Observer {

    /** Le mod�le que le gui affiche */
    private URIModel m = null;

    /**
     * Le contr�leur associ� � l'�v�nement d'appui sur Entr�e.
     * 
     * @author derosias
     */
    private class OnEnterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            m.setURI(new File(getText()));
        }
    }

    /**
     * Construit un gui d'uri avec un mod�le par d�faut.
     * 
     * @param m
     *            un mod�le d'uri avec lequelle associer le gui
     */
    public URIGUI(URIModel m) {
        this.m = m;
        m.addObserver(this);
        addActionListener(new OnEnterListener());
        setColumns(20);
        update(m, null);
    }

    /**
     * Met � jour l'affichage graphique (l'uri affich�e) quand le mod�le change.
     */
    public void update(Observable o, Object arg) {
        System.out.println("URIGUI.update() / " + o);
        
        if (o instanceof URIModel) {
            URIModel urim = (URIModel) o;
            setText(urim.getURI().getAbsolutePath());
        
        } else if (o instanceof FileSystemTreeModel) {
            FileSystemTreeModel fstm = (FileSystemTreeModel) o;
            m.setURI(fstm.getCurrentSelection());
        }
        
    }
}