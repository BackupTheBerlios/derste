/*
 * Created on 7 nov. 2004
 */
package preview;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import misc.file.FileUtilities;

/**
 * Représentation dans un composant JPanel des informations concernant un
 * fichier telle(s) que le nom, le type, la taille, la date de derniére
 * modfification
 * 
 * @author brahim
 * @author Sted
 */
public class FileInfo extends JPanel {
	public FileInfo(File file) {
		add(new JLabel(FileUtilities.getToolTip(file)));
	}
}