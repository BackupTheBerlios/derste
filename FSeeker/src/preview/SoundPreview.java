/*
 * Created on 1 nov. 2004
 *
 * Classe qui permet la prévisualisation de fichier sons 
 *  Formats supportés : mp3, wav, PCM .... cf doc JLayer pour fichier ogg,
 * mettre les plugins 
 *
 */
package preview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javazoom.jl.decoder.JavaLayerException;

import misc.GU;
import misc.ImagesMap;
import misc.MP3Previewer;
import misc.SoundPlayer;

/**
 * @author brahim
 *  
 */
public class SoundPreview extends JPanel implements Preview, ActionListener {

    /** Fichier mp3 ou wav ou PCM dont on souhaite effectuer la lecture */
    private File file;

    /** Le player utilisé pour gérer les sons */
    //private SoundPlayer player;
    /** Permet la gestion de play et stop */
    private MP3Previewer pl;

    /**
     * Les quelques boutons qui servont à lancer le son et l'arretée
     */
    JButton[] controls = new JButton[2];;

    /**
     * Constructeur
     */
    public SoundPreview(File file) {
        this.file = file;
        //player = new SoundPlayer(file);      
        
        try{
        pl = new MP3Previewer(file);
        }catch (JavaLayerException jle) {
            GU.warn("Impossible de lire le fichier ! ");              
        }
        
    }

    /**
     * Lance la prévisualisation
     */
    public void preview() {
        controls[0] = new JButton(ImagesMap.get("play.png"));
       // controls[0].setIcon();
        controls[1] = new JButton(ImagesMap.get32x32("stop.png"));        
        //controls[0].setIcon();
        
        for (int i = 0; i < controls.length; i++) {
            add(controls[i]);
            controls[i].addActionListener(this);
        }
    }

    /** gestion d'événements TODO Optimiser */
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource().equals(controls[0]))
            try {
                pl.play();
            } catch (JavaLayerException jle) {
                        GU.warn("Impossible de lire le fichier ! ");              
            }
        else if (ev.getSource() == controls[1])
            pl.stop();     
    }

    /**
     * 
     * Méthode de test de la classe
     */
    public static void main(String[] args) {

        //C mon fichier a moi ca
        SoundPreview soundp = new SoundPreview(new File("/home/brahim/055.mp3"));

        soundp.preview();
        JFrame jf = new JFrame("Test de SoundPreview");
        jf.getContentPane().add(soundp);
        jf.setVisible(true);
        jf.pack();

    }
}