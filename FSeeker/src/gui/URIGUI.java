package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextField;

import model.FileSystemTreeModel;
import model.URIModel;
import controler.URIControler;

/**
 * Représente l'URI graphiquement.
 * 
 * @author derosias
 */
public class URIGUI extends JTextField implements Observer {

    /** Le modèle que le gui affiche */
    private URIModel m = null;

    /**
     * Construit un gui d'uri avec un modèle par défaut.
     * 
     * @param m
     *            un modèle d'uri avec lequelle associer le gui
     */
    public URIGUI(URIModel m) {
        this.m = m;
        m.addObserver(this);
        addActionListener(new URIControler(m));
        setColumns(20);
        update(m, null);
    }

    public URIModel getModel() {
        return m;
    }

    /**
     * Met à jour l'affichage graphique (l'uri affichée) quand le modèle change.
     */
    public void update(Observable o, Object arg) {
        System.out.println("URIGUI.update() / " + o);

        if (o instanceof URIModel) {
            URIModel urim = (URIModel) o;
            setText(urim.getURI().getAbsolutePath());

        } else if (o instanceof FileSystemTreeModel) {
            FileSystemTreeModel fstm = (FileSystemTreeModel) o;
            m.setURI(fstm.getCurrentDirectory());
        }

    }
}