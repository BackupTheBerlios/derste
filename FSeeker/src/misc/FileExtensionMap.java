/*
 * Created on 17 oct. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package misc;

import java.util.HashMap;
import java.util.Iterator;
import java.io.*;

import javax.swing.*;

/**
 * @author brahim
 * 
 * Cete classe permet de faire le lien entre une extension de fichier et une
 * Image particuliére se trouvant sur le disque. Elle evite ainsi une certaine
 * redondance dans les représentations diverses faisant appel à cette
 * fonctionnalité
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FileExtensionMap extends HashMap {

	private HashMap extImg = new HashMap();

	private File file;

	private boolean DEBUG = true;
	private static final String deflt = "file.png";//fichier par défaut

	private static final String dir = "directory.png";//répértoire

	private static final String txt = "txt.png";

	private static final String java = "java.png";

	private static final String gif = "gif.png";

	private static final String classe = "class.png";

	private static final String xml = "xml.png";

	/**
	 * 
	 * @param file
	 *            Le fichier dont on souhaite analyser l'extension
	 * 
	 * 17 oct. 2004
	 */
	public FileExtensionMap(File file) {
		this.file = file;
		//On initialise la HashMap avec toutes les valeurs connues
		extImg.put("dir", dir);
		extImg.put("txt", txt);
		extImg.put("java", java);
		extImg.put("class", classe);
		extImg.put("gif", gif);
		extImg.put("xml", xml);

	}

	protected ImageIcon img(String path) {
		return GU.createImg(path);
	}

	/**
	 * 
	 * @param dir
	 *            Répertoire ou seront trouver les fichiers. En effet, on peut
	 *            penser que des fichiers de memes noms soient placés à des
	 *            endroits différents selon leur utilité ex : un dossier 32*32
	 *            contenant les fichiers de cette taille pour les icones d'un
	 *            arbres par exemple , un autre 64*64 contenant les dossiers de
	 *            cette taile ....
	 * @return l'image associée à ce type de fichier, avec dir le dossier des
	 *         images
	 */
	public ImageIcon getIcon(String dir) {
		ImageIcon fileIcon = null;
		String fileStr = null;
		if (file != null)
			fileStr = file.toString();
		if(DEBUG) out("fileStr = " + fileStr);		
		//String ext = fileStr.substring(fileStr.lastIndexOf("."));
		if (file.isDirectory()) {				
			fileIcon = img(dir /*+ GU.SEP*/ + extImg.get("dir"));
			if(DEBUG) out(file + " est un répertoire et fileIcon = " + fileIcon);
		} else {
			Iterator it = extImg.keySet().iterator();
			boolean ok = false;
			while (it.hasNext()) {
				String ext = (String) it.next();
				if (fileStr.toString().endsWith(ext)) {
					img((String) extImg.get(ext));
					ok = true;
					break;
				}
			}
			if (!ok)//on prend le fichier image par défaut
				img((String) extImg.get(deflt));
		}		

		return fileIcon;

	}
	
	protected void out(String s){
		System.out.println(s);
	}

	/**
	 * 
	 * @param args Argument de la lgine de commande (inutile)
	 */
	public static void main(String[] args) {
		FileExtensionMap testMap = new FileExtensionMap(new File("/"));
		JPanel jp = new JPanel();
		System.out.println("iamge = "+testMap.getIcon("").toString());
		jp.add(new JLabel(testMap.getIcon("")));
		GU.createGUI("FileExtensionMap Test class", jp);

	}
}