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

    /** Le supra-mod�le */
    protected FSeekerModel fsm = null;

    /** Largeur des images � previsualiser */
    private int width = 120;

    /** Hauteur des images � pr�visualiser */
    private int height = 90;
    
    /** Couleur d'arri�re plan pour la preview */
    Color BACKG = Color.WHITE;

    /**
     * Constructeur
     * 
     * @param fsm
     *            Le supra-mod�le
     *  
     */
    public PreviewManager(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
        // Layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
    }

    /**
     * M�thode appell�e quand le mod�le change
     */
    public void update(Observable o, Object arg) {

        // Selon le fichier on met � jour la preview n�cessaire
        File selection = fsm.getSelection();

        // On sort si la s�l�ction est null
        if (selection == null)
            return;
        removeAll();//S�curit�
        
        Preview p = null;
        String mime = "";
        String ext = "";

        if (selection.canRead()) {
            mime = FileUtilities.getMIMEType(selection);

            //On r�cup�re l'extension
            String s = selection.getName();
            int index = s.lastIndexOf('.');
            // On v�rifie la valeur de l(index recup�r�
            if (!(index == -1 || index + 1 >= s.length()))
                ext = s.substring(index + 1);
        }

        if (selection.isDirectory()) {
            //R�p�rtoire
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
            // Fichier par d�faut
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
     * m�thode de test pour la classe PreviewManager
     * 
     * @param args
     */
    public static void main(String[] args) {
    }
}