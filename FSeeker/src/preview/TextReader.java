/*
 * Créé le 25 oct. 2004
 */

package preview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe qui permet la lecture d'un fichier texte et la récupération d'une
 * partie du texte
 * 
 * @author aitelhab
 * @author Sted
 */
public class TextReader {

	/** Fichier texte à prévisualiser */
	private File file;

	/** Nombre de ligne à lire */
	private int nbLine = 10;

	/** Nombre de caractères à lire par ligne */
	private int nbChar = 25;

	private String[] text;

	public TextReader(File file) {
		this.file = file;
		text = new String[nbLine];
		init(); // on initialise le vecteur à ""
	}

	/**
	 * Retourne le tableau de String contenant les nbLine à prévisualiser.
	 */
	public String[] getArray() {
		try {
			int index = 0;
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String line;
			while ((line = buffer.readLine()) != null && index < nbLine) {
				text[index] = line.substring(0,
						(line.length() > nbChar) ? nbChar : line.length());
				index++;
			}
			
			buffer.close();
		} catch (FileNotFoundException fnfe) {
			System.err.println(file + " : File Not Found !");
		} catch (IOException ioe) {
			System.err.println(file + " : IO Exception !");
		}
		return text;

	}

	/** Initialise les valeurs du tableau de String à "" */
	private void init() {
		for (int i = 0; i < text.length; i++)
			text[i] = "";
	}
}