/*
 * Created on 31 oct. 2004
 *
 * Cette classe permet la previsualisation d'une image 
 *
 */
package preview;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.*;

import misc.ImagesMap;
import misc.GU;

/**
 * @author brahim
 *
 */
public class ImagePreview extends JPanel implements Preview {

    /* Fichier correspondant au fichier image */
    private File image;
    
    /* La label ou l'on fixe l'imgage */
    private JLabel imageLabel;
    
    /* Longueur et largeur de la nouvelle image à obtenir */
    private int width, height;
    
    public ImagePreview(File image, int width, int height){
        this.image = image;
        this.width = width;
        this.height = height;
        imageLabel = new JLabel();
    }
    
    
    /*
     * Méthode à appeler pour permettre la previsualisation
     */
    public void preview(){       
        imageLabel.setIcon(getThumbnail());
        add(imageLabel);
    }
    
    

	public  Icon getThumbnail() {
	
	    Image thumbnail = Toolkit.getDefaultToolkit().getImage(""+image.getAbsolutePath());
	    System.out.println("image PREVIEW = "+image.getAbsolutePath());
		if (image.exists()) {		    
		    Icon pic = new ImageIcon(thumbnail.getScaledInstance(width, height, Image.SCALE_SMOOTH));		    
			return pic;
		}
		
		// On renvoie rien
		return null;
	}
    
    /**
     * Méthode de test pour cette classe
     * @param args
     */
    public static void main(String[] args){
        String url = "/home/brahim/image.jpg";
        ImagePreview imp = new ImagePreview(new File(url), 200, 150);
        //GU.createGUI("Teste de ImagePreview", imp);
        JFrame jf = new JFrame("Test de ImagePreview");
        /*JPanel panel = new JPanel();*/
        imp.preview();
        jf.getContentPane().add(imp);
        jf.setVisible(true);
        jf.pack();
        
    }

}
