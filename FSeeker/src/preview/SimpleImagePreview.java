/*
 * Created on 7 nov. 2004
 *
 * Classe qui permet la représentation d'un fichier quelconque (utilise misc.ImagesMap)
 */
package preview;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import misc.ImagesMap;

/**
 * @author brahim
 *
 */
public class SimpleImagePreview extends JPanel implements Preview{
    
    /** Label ou sera ise l'image */
    private JLabel imgLabel;
    
    /** fichier image */
    private File file;
    
    public SimpleImagePreview(File file){
        imgLabel = new JLabel();
        this.file = file;
    }
    
    /**
     * Lance la preview du fichier
     */
    public void preview(){
        // On fixe l'image au label
        imgLabel.setIcon(ImagesMap.get(file));
        //on fixe le label au panel
        add(imgLabel);
        
    }

}
