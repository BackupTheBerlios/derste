package misc;

import java.io.*;
import javazoom.jl.player.*;
import javazoom.jl.player.advanced.*;
import javazoom.jl.decoder.*;

/**
 * 
 * @author brahim
 *
 */
public class MP3Previewer {

    protected AudioDevice dev;

    protected AdvancedPlayer player;

    protected File file;
    
    protected Play p;
    protected boolean test = true;

    public MP3Previewer(File file) throws JavaLayerException {
        this.file = file;
        try {            
            dev = FactoryRegistry.systemRegistry().createAudioDevice();
            player = new AdvancedPlayer(new FileInputStream(file), dev);
        } catch (FileNotFoundException fnfe) {
            GU.warn("Impossible de trouver le fichier : " + file);           
        } 
    }

    public void play() throws JavaLayerException {
       (new Play()).start();
        
    }

    public void stop() {
        (new Stop()).start();
    }

    class Play extends Thread {
        public void run() {
            try {             
                player.play();
            } catch (JavaLayerException jle) {
                jle.printStackTrace();
            }
        }        
        
    }

    class Stop extends Thread {
        public void run() {           
            //player.stop();
            dev.close();
            dev = null;            
        }
    }

}