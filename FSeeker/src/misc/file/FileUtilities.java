/*
 * Created on 24 oct. 2004
 */
package misc.file;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

/**
 * Classe utilitaire portant sur les fichiers.
 * 
 * @author Sted
 */
public class FileUtilities {

	public static class FileDetails {

		// protected static SimpleDateFormat dateFormat = new
		// SimpleDateFormat("EEEEEEEE dd MMMMMMMMM yyyy '�' hh:mm:ss",
		// Locale.getDefault());

		public final static DateFormat dateFormat = DateFormat
				.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);;

		protected File f = null;

		protected String type = null;

		protected String size = null;

		protected String lastModified = null;

		protected String name = null;

		public Font dirFont = new Font("Serif", Font.BOLD, 12);

		public Font fileFont = new Font("Serif", Font.PLAIN, 12);

		public String getType() {
			if (type == null)
				// TODO si fichier, �valuer le type (jpg, avi, exe etc)
				type = (f.isDirectory() ? "Dossier" : "Fichier");
			return type;
		}

		public String getSize() {
			if (size == null) {
				// On affiche la taille pour un fichier, le nombre d'�lement
				// contenu pour un dossier
				if (f.isFile()) {
					// On affiche pas 3445345134 octets, on simplifie un peu..
					long octets = f.length();
					String unite = "octets";
					if (octets > 2048) {
						octets /= 1024;
						unite = "Ko";
					}
					if (octets > 2028) {
						octets /= 1024;
						unite = "Mo";
					}
					if (octets > 2028) {
						octets /= 1024;
						unite = "Go";
					}

					size = octets + " " + unite;

				} else {
					// R�pertoire
					File[] foo = f.listFiles();
					if (foo != null && foo.length > 0)
						size = foo.length + " fichier"
								+ (foo.length > 1 ? "s" : "");
					else
						size = "";
				}

			}

			return size;
		}

		public String getName() {
			if (name == null)
				name = FileSystemView.getFileSystemView().getSystemDisplayName(
						f);
			return name;
		}

		public String getLastModified() {
			if (lastModified == null)
				lastModified = dateFormat.format(new Date(f.lastModified()));
			return lastModified;
		}

		public FileDetails(File f) {
			this.f = f;
		}

		public Font getFont() {
			if (f.isDirectory())
				return dirFont;
			return fileFont;
		}

		public String getToolTip() {
			StringBuffer sb = new StringBuffer(50);
			sb.append("<html>");

			// Une jolie image pour faire sta�lle
			sb.append("<center><img src=\"/images/dot.gif\"></center><br>");

			// Le type et le nom (pour les aveugles)
			sb.append("<b>" + getType() + "</b> : " + getName() + "<br>");

			if (f.isDirectory()) {
				if (getSize() != null)
					sb.append("<b>Contient</b> : " + getSize() + "<br>");
			} else
				sb.append("<b>Taille</b> : " + getSize() + "<br>");

			sb.append("<b>Derni�re modification</b> : " + getLastModified()
					+ "<br>");

			return sb.append("</html>").toString();
		}

	}

	/**
	 * Retourne un texte descriptif d'un fichier.
	 * 
	 * @param f
	 *            le fichier � d�crire.
	 * @return le texte descriptif
	 */
	public static String getToolTip(File f) {
		return new FileDetails(f).getToolTip();
	}

	/**
	 * Copie un fichier.
	 * 
	 * @param src
	 *            fichier source
	 * @param dst
	 *            fichier destination
	 * @return <code>true</code> si la copie a r�ussi
	 */
	public static boolean copy(File src, File dst) {
		// On n'�crase pas (� faire du c�t� de l'appelant �a)
		if (dst.exists())
			return false;

		boolean resultat = false;

		// Declaration des flux
		FileInputStream sourceFile = null;
		FileOutputStream destinationFile = null;

		try {
			// Cr�ation du fichier
			dst.createNewFile();

			// Ouverture des flux
			sourceFile = new FileInputStream(src);
			destinationFile = new FileOutputStream(dst);

			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			int nbLecture;

			while ((nbLecture = sourceFile.read(buffer)) != -1)
				destinationFile.write(buffer, 0, nbLecture);

			// Copie r�ussie
			resultat = true;
		} catch (FileNotFoundException f) {

		} catch (IOException e) {

		} finally {
			// Quoi qu'il arrive, on ferme les flux
			try {
				sourceFile.close();
			} catch (IOException e) {
			}
			try {
				destinationFile.close();
			} catch (IOException e) {
			}
		}

		return resultat;
	}

	/**
	 * D�place un fichier.
	 * 
	 * @param src
	 *            fichier source
	 * @param dst
	 *            fichier destination
	 * @return <code>true</code> si le d�placement a r�ussi
	 */
	public static boolean move(File src, File dst) {
		// On n'�crase pas, et on essaye avec renameTo ou avec delete + copy
		return !dst.exists()
				&& (src.renameTo(dst) || (copy(src, dst) && src.delete()));
	}

	/**
	 * Supprime un fichier ou r�pertoire (en r�cursif pour celui-ci).
	 * 
	 * @param file
	 *            le fichier ou r�pertoire � supprimer
	 * @return <code>true</code> si la suppression a �t� correctement
	 *         effectu�e
	 */
	public static boolean delete(File file) {
		// Cas triviaux
		if (!file.exists())
			return false;

		if (file.isFile())
			return file.delete();

		// On a un r�pertoire, r�cursivit� powa
		boolean resultat = true;

		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++)
			if (files[i].isDirectory())
				resultat &= delete(files[i]);
			else
				resultat &= files[i].delete();

		resultat &= file.delete();

		return resultat;
	}

	/**
	 * Renvoie un tableau contenant les noms des fichiers d'un tableau de File.
	 * 
	 * @param files
	 *            tableau de Files dont retourn�s les noms
	 * @return un tableau de Strings ne contenant que les noms des fichiers
	 */
	public static String[] toStrings(File[] files) {
		if (files == null)
			return null;

		int num = files.length;
		String[] dirs = new String[num];
		for (int i = 0; i < num; i++)
			dirs[i] = files[i].getName();

		return dirs;
	}

}