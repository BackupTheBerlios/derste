/*
 * Created on 1 nov. 2004
 * Classe (source d'origine http://www.javazoom.net/jlgui/developerguide.html
 * 
 * TODO REMMETRE EN ORDRE, EN EFFET, C'EST LE BORDEL ET COMMENTER EN FRENCH 
 *
 */
package misc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

/*Libraire qui utilise JLayer et qui gére un lecteur de base */
import javazoom.jlgui.basicplayer.*;



/**
 * @author brahim
 *  
 */
public class SoundPlayer implements BasicPlayerListener {

    /** Le fichier son à lire (mp3 ou wav)  */
    private File file;
    
    
    /** Le player */
    BasicPlayer player ;
    
    /** Le controleur qui gére le thread associé au son (arret, pause ..) */
    BasicController control;
    
    /**
     * 
     * @param file Le fichier à lire
     * 7 nov. 2004
     */
    public SoundPlayer(File file) {
        this.file = file;              
        player  = new BasicPlayer();
        //      	BasicPlayer is a BasicController.
        control = (BasicController) player;
        //     Register BasicPlayerTest to BasicPlayerListener events.
        //     It means that this object will be notified on BasicPlayer
        //     events such as : opened(...), progress(...), stateUpdated(...)
        player.addBasicPlayerListener(this);
        
    }


    /**
     *  Lance la lecture du fichier 
     **/
    public void play() {
        try { // Open file, or URL or Stream (shoutcast, icecast) to play.
            control.open(file);

            //     control.open(new URL("http://yourshoutcastserver.com:8000"));
            //     Start playback in a thread. control.play();
            //     If you want to pause/resume/pause the played file then
            //     write a Swing player and just call control.pause(),
            //     control.resume() or control.stop().
            //     Use control.seek(bytesToSkip) to seek file
            //     (i.e. fast forward and rewind). seek feature will
            //     work only if underlying JavaSound SPI implements
            //     skip(...). True for MP3SPI (JavaZOOM) and SUN SPI's
            //     (WAVE, AU, AIFF). // Set Volume (0 to 1.0).
            // control.setGain(0.85);
            //     Set Pan (-1.0 to 1.0).
            control.setPan(0.0);
            control.play(); //Lance la lecture
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Arrête la lecture
     */
    public void stop(){
        try { // Open file, or URL or Stream (shoutcast, icecast) to play.
            control.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }
    
  
    /**
     * Met en pause la lecture du son
     */
    public void pause(){
        try { // Open file, or URL or Stream (shoutcast, icecast) to play.
            control.pause();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * reprend la lecture du son arès une mise en pause
     */
    public void resume(){
        try { // Open file, or URL or Stream (shoutcast, icecast) to play.
            control.resume();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open callback, stream is ready to play.
     * 
     * properties map includes audio format dependant features such as bitrate,
     * duration, frequency, channels, number of frames, vbr flag, ...
     * 
     * @param stream
     *            could be File, URL or InputStream
     * @param properties
     *            audio stream properties.
     */
    public void opened(Object stream, Map properties) {
        //     Pay attention to properties. It's useful to get duration,
        //     bitrate, channels, even tag such as ID3v2.
        //display("opened : " + properties.toString());
    }

    /**
     * * Progress callback while playing.
     * 
     * This method is called severals time per seconds while playing. properties
     * map includes audio format features such as instant bitrate, microseconds
     * position, current frame number, ...
     * 
     * @param bytesread
     *            from encoded stream.
     * @param microseconds
     *            elapsed ( <b>reseted after a seek ! </b>).
     * @param pcmdata
     *            PCM samples.
     * @param properties
     *            audio stream parameters.
     */
    public void progress(int bytesread, long microseconds, byte[] pcmdata,
            Map properties) {
        //     Pay attention to properties. It depends on underlying JavaSound SPI
        //     MP3SPI provides mp3.equalizer.
        display("progress : " + properties.toString());
        
    }

    /**
     * Notification callback for basicplayer events such as opened, eom ...
     * 
     * @param event
     */
    public void stateUpdated(BasicPlayerEvent event) {
        //     Notification of BasicPlayer states (opened, playing, end of media,
        // ...)
        display("stateUpdated : " + event.toString());
        
    }

    /**
     * A handle to the BasicPlayer, plugins may control the player through the
     * controller (play, stop, ...)
     * 
     * @param controller :
     *            a handle to the player
     */
    public void setController(BasicController controller) {
        display("setController : " + controller);
        
    }

    public void display(String msg) {       
            System.out.println(msg);
    }
    
    
    /**
     * Méthode de test pour cette classe
     * 
     * @param args Inutilisé
     *             .
     */
    public static void main(String[] args) {
        SoundPlayer test = new SoundPlayer(new File(
                "/home/brahim/055.mp3"));
        test.play();
    }
}