package preview;

/*
 * Créé le 25 oct. 2004
 *
 * Classe qui permet la lecture d'un fichier texte et la récupération d'une partie du texte 
 */
import java.io.*;


/**
 * @author aitelhab
 *  
 */
public class TextReader  {

    /* Fichier texte à previsualiser */
    private File file;

    /* Nombre de ligne à lire */
    private int nbLine = 10;

    /* Nombre de caractéres à lire par ligne */
    private int nbChar = 20;

    String[] text;

    public TextReader(File file) {
        this.file = file;
        text = new String[nbLine];
        init();//on initialise le vecteur à vide
    }


    public String[] getArray() {

        try {
            int index = 0;
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            while (buffer.ready() && index < nbLine) {
                String line = buffer.readLine();
              
                //Si la ligne non vide on met ses nbChar dans text à renvoyer
                if (line != null) {
                    text[index] = line.substring(0,
                            (line.length() > nbChar) ? nbChar : line
                                    .length() );
                    //On décrement l'index
                    index++;
                }
            }
            buffer.close();
        } catch (FileNotFoundException fnfe) {
            System.err.println(file + " : File Not Found !");
        } catch (IOException ioe) {
            System.err.println(file + " : IO Exception !");
        }
        return text;

    }

    /* On initialisze les valeurs du tableau de String */
    public void init() {
        for (int i = 0; i < text.length; i++)
            text[i] = "";
    }

    public static void main(String[] args) {
        TextReader tp = new TextReader(new File("/home/brahim/test.txt"));
        String[] result = tp.getArray();
        for (int i = 0; i < result.length; i++)
            System.out.println(result[i]);

    }

}