/*
 * Created on 1 nov. 2004
 */
package preview;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe qui permet la prévisualisation d'un fichier texte
 * 
 * @author brahim
 * @author Sted
 */
public class TextPreview extends JPanel implements Preview {

	/** Le fichier texte à prévisualiser */
	private File file;

	/** Le label dans lequel on affiche le texte */
	private JLabel textLabel;

	/** Permet la lecture d'un fichier texte */
	private TextReader reader;

	/** La couleur de fond */
	private Color back = new Color(180, 205, 255);

	/**
	 * Construit la preview d'un fichier texte
	 * 
	 * @param file
	 *            Le fichier texte à prévisualiser
	 */
	public TextPreview(File file) {
		this.file = file;
		reader = new TextReader(file);
		textLabel = new JLabel();
		textLabel.setOpaque(true);
		textLabel.setBackground(back);
		textLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textLabel.setFont(new Font("Courier", Font.PLAIN, 12));
		//.setBorder(BorderFactory.createLineBorder (Color.blue, 2));
		add(textLabel);
	}

	/**
	 * Lance la preview
	 */
	public void preview() {
		String[] result = reader.getArray();
		String txt = "<html>";
		for (int i = 0; i < result.length; i++)
			if (!result[i].equals(""))
				txt += result[i] + "<br>";
		//System.out.println(result[i]);
		txt += "<html>";
		textLabel.setText(txt);
	}
}