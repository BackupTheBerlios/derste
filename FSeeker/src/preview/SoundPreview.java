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

import misc.GU;
import misc.SoundPlayer;

/**
 * @author brahim
 *  
 */
public class SoundPreview extends JPanel implements Preview, ActionListener {

    /** Fichier mp3 ou wav ou PCM dont on souhaite effectuer la lecture */
    private File file;

    /** Le player utilisé pour gérer les sons */
    private SoundPlayer player;

    /**
     * Les quelques boutons qui servont à lancer les sons, mettre en pause,
     * reprendre la lacture et enfin l'arretée
     */
    JButton[] controls = new JButton[4];;

    /**
     *  Constructeur */   
    public SoundPreview(File file) {
        this.file = file;
        player = new SoundPlayer(file);
        /*preview();*/
    }

    /**
     * Lance la prévisualisation
     */
    public void preview() {
        controls[0] = new JButton("Play");
        controls[1] = new JButton("Stop");
        controls[2] = new JButton("Pause");
        //TODO setEnabled(false) quand la lecture est deja mise en pause
        controls[3] = new JButton("Reprendre");
        for (int i = 0; i < controls.length; i++) {
            add(controls[i]);
            controls[i].addActionListener(this);
        }
    }

    /** gestion d'événements TODO Optimiser */
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource().equals(controls[0]))
            player.play();
        else if (ev.getSource() == controls[1])
            player.stop();
        else if (ev.getSource() == controls[2])
            player.pause();
        else if (ev.getSource() == controls[3])
            player.resume();
    }

    /**
     * 
     *  Méthode de test de la classe
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