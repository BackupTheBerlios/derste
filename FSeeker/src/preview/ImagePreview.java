/*
 * Created on 31 oct. 2004
 *
 * Cette classe permet la previsualisation d'une image 
 *
 */
package preview;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
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
    private int thumbWidth, thumbHeight;

    public ImagePreview(File image, int thumbWidth, int thumbHeight) {
        this.image = image;
        this.thumbWidth = thumbWidth;
        this.thumbHeight = thumbHeight;
        imageLabel = new JLabel();
    }

    /*
     * Méthode à appeler pour permettre la previsualisation
     */
    public void preview() {
        imageLabel.setIcon(getThumbnail());
        add(imageLabel);
    }

    public Icon getThumbnail() {
        if (image.exists()) {
            Icon thumbnail = null;
            Image img = Toolkit.getDefaultToolkit().getImage(
                    "" + image.getAbsolutePath());

            //On ne peut pas récuperer les infos qur l'image si elle n'est pas
            // chargée
            // donc il faut utiliser cette méthode
            try {
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(img, 1);
                tracker.waitForAll(); // load it now, then we can get its width
                // & height
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            

            //          Garder la proportionalité            
            //int thumbWidth = Integer.parseInt(args[2]);
            //int thumbHeight = Integer.parseInt(args[3]);
            int imageWidth = img.getWidth(null);
            int imageHeight = img.getHeight(null);

            //Si la hauteur ou la longueur de l'image d'origine est plus grande
            // que la taille voulue
            if (imageWidth > thumbWidth || imageHeight > thumbHeight) {
                double thumbRatio = (double) thumbWidth / (double) thumbHeight;
                double imageRatio = (double) imageWidth / (double) imageHeight;
                if (thumbRatio < imageRatio) {
                    thumbHeight = (int) (thumbWidth / imageRatio);
                } else {
                    thumbWidth = (int) (thumbHeight * imageRatio);
                }                

                thumbnail = new ImageIcon(img.getScaledInstance(thumbWidth,
                        thumbHeight, Image.SCALE_SMOOTH));
            }
            //Pas besoin de redimensionner, on renvoie l'image telle qu'elle
            else
                thumbnail = thumbnail = new ImageIcon(img);

            // Affichage des infos
            //System.out.println("thumbHeight = " + thumbHeight);
            //System.out.println("thumbWidth = " + thumbWidth);
            //System.out.println("img.getWidth(null) = "
            //        + (double) img.getWidth(null));
            //System.out.println("img.getHeight(null) = "
            //        + (double) img.getHeight(null));

            return thumbnail;
        }

        // On ne renvoie rien, le fichier n'existe pas
        return null;
    }

    /**
     * Méthode de test pour cette classe
     * 
     * @param args
     */
    public static void main(String[] args) {
        String url = "/home/brahim/image.jpg";
        ImagePreview imp = new ImagePreview(new File(url), 200, 150);
        //GU.createGUI("Teste de ImagePreview", imp);
        JFrame jf = new JFrame("Test de ImagePreview");
        /* JPanel panel = new JPanel(); */
        imp.preview();
        jf.getContentPane().add(imp);
        jf.setVisible(true);
        jf.pack();

    }

}