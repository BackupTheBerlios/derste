/*
 * Created on 1 nov. 2004
 *
 * Classe qui permet la prévisualisation d'un fichier texte
 */
package preview;


import misc.GU;

import java.awt.Color;
import java.awt.Image;
import java.awt.AWTException;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


/**
 * 
 * @author brahim
 *
 */
public class TextPreview extends JPanel implements Preview {

    /** Le fichier texte à previsualiser */
    private File file;
    
    /** Le label dans lequel on affiche le texte */
    private JLabel textLabel;
    
    /** Permet la lecture d'un fichie text */
    private TextReader reader;
    
    /** La couleur de fond */
    private Color back = new Color(180, 205, 255);

    /**
     * 
     * @param file Le fichier texte à prévisualiser
     * 7 nov. 2004
     */
    public TextPreview(File file) {
        this.file = file;
        reader = new TextReader(file);
        textLabel = new JLabel();
        textLabel.setOpaque(true);
        textLabel.setBackground(back);
        textLabel.setBorder (BorderFactory.createEmptyBorder (5, 5, 5, 5));
        //.setBorder(BorderFactory.createLineBorder (Color.blue, 2));
        add(textLabel);        
    }

    /**
     * Lance la preview
     */
    public void preview(){         
        String[] result = reader.getArray();
        String txt = "<html>";
        for (int i = 0; i < result.length; i++)
            if(!result[i].equals(""))
                txt+=result[i]+"<br>";
            //System.out.println(result[i]);        
        txt+="<html>";        
        textLabel.setText(txt);
    }
    
    
 
    /**
     * Méthode de test pour cette classe
     * @param args
     */
    public static void main(String [] args)throws AWTException{
        TextPreview tp =  new TextPreview(new File("/home/brahim/test.txt"));          
        JFrame jf = new JFrame("Test de TextPreview");
        tp.preview();
        jf.getContentPane().add(tp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        jf.pack();
     
        
    }
    
    
    
}