/*
 * Created on 7 nov. 2004
 *
 *
 */
package preview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.rsasign.i;

import misc.ImagesMap;
import misc.file.FileUtilities;
import model.FSeekerModel;

/**
 * @author brahim
 *  
 */
public class PreviewManager extends JPanel implements Observer {

    /** Le supra-modèle */
    protected FSeekerModel fsm = null;

    /** Largeur des images à previsualiser */
    private int width = 120;

    /** Hauteur des images à prévisualiser */
    private int height = 90;
    
    /** Couleur d'arriére plan pour la preview */
    Color BACKG = Color.WHITE;

    /**
     * Constructeur
     * 
     * @param fsm
     *            Le supra-modéle
     *  
     */
    public PreviewManager(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
        // Layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
    }

    /**
     * Méthode appellée quand le modéle change
     */
    public void update(Observable o, Object arg) {

        // Selon le fichier on met à jour la preview nécessaire
        File selection = fsm.getSelection();

        // On sort si la séléction est null
        if (selection == null)
            return;
        removeAll();//Sécurité
        
        Preview p = null;
        String mime = "";
        String ext = "";

        if (selection.canRead()) {
            mime = FileUtilities.getMIMEType(selection);

            //On récupére l'extension
            String s = selection.getName();
            int index = s.lastIndexOf('.');
            // On vérifie la valeur de l(index recupéré
            if (!(index == -1 || index + 1 >= s.length()))
                ext = s.substring(index + 1);
        }

        if (selection.isDirectory()) {
            //Répértoire
            p = new SimpleImagePreview(selection);
        } else if (mime.startsWith("image/")) {
            // Image
            p = new ImagePreview(selection, width, height);
        } else if (mime.equals("text/plain")) {
            // Fichier texte
            p = new TextPreview(selection);
        } else if (ext.equals("mp3") || ext.equals("wav")) {
            // Fichier mp3 ou wav
            p = new SoundPreview(selection);
        } else {
            // Fichier par défaut
            p = new SimpleImagePreview(selection);
        }

             
        
        // On lance la preview
        p.preview();
        JPanel preview = (JPanel) p;
        preview.setOpaque(true);
        preview.setBackground(BACKG);
        add(preview);
       
        // Maitenant on s'occupe de mettre les infos
        JPanel info = new FileInfo(selection);
        info.setOpaque(true);
        info.setBackground(BACKG);
        add(info);
        
        // On ajoute le panel contenant tout les autres au panel courant 
        //add(main);
        
        repaint();
        revalidate();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * méthode de test pour la classe PreviewManager
     * 
     * @param args
     */
    public static void main(String[] args) {
    }
}