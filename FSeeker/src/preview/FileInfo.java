/*
 * Created on 7 nov. 2004
 *
 * Représentation dans un composant JPanel des informations concernant un fichier
 * telle(s) que le nom, le type, la taille, la date de derniére modfification  
 *
 */
package preview;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import misc.GU;
import misc.file.FileUtilities;

/**
 * @author brahim
 *
 */
public class FileInfo extends JPanel {
    
    /** Le label dans lequel on mettra les informations sur le fichier */
    private JLabel infoLabel;
    
    /** Le fichier dont on souhaite afficher les informations */
    private File file;
    
    public  FileInfo(File file){
        this.file = file;
        infoLabel = new JLabel(FileUtilities.getToolTip(file));
        add(infoLabel);      
    }  
    

}
