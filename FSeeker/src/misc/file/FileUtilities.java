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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import misc.GU;

/**
 * Classe utilitaire portant sur les fichiers.
 * 
 * @author Sted
 */
public class FileUtilities {

	public static class FileDetails {

		// protected static SimpleDateFormat dateFormat = new
		// SimpleDateFormat("EEEEEEEE dd MMMMMMMMM yyyy 'à' hh:mm:ss",
		// Locale.getDefault());

		public final static DateFormat dateFormat = DateFormat
				.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);;

		protected File f = null;

		protected String type = null;

		protected String size = null;

		protected String lastModified = null;

		protected String name = null;

		public Font dirFont = new Font("Dialog", Font.BOLD, 12);

		public Font fileFont = new Font("Dialog", Font.PLAIN, 12);

		public String getType() {
			if (type == null)
				// TODO si fichier, évaluer le type (jpg, avi, exe etc)
				type = (f.isDirectory() ? "Dossier" : "Fichier");
			//type =
			// FileSystemView.getFileSystemView().getSystemTypeDescription(f);
			return type;
		}

		public String getSize() {
			if (size == null) {
				// On affiche la taille pour un fichier, le nombre d'élement
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
					// Répertoire
					File[] foo = f.listFiles();
					if (foo != null && foo.length > 0)
						size = foo.length + " élément"
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

			// Une jolie image pour faire staïlle
			sb
					.append("<center><img src=\"../../images/dot.gif\"></center><br>");

			// Le type et le nom (pour les aveugles)
			sb.append("<b>" + getType() + "</b> : " + getName() + "<br>");

			if (f.isDirectory()) {
				if (!"".equals(getSize()))
					sb.append("<b>Contient</b> : " + getSize() + "<br>");
			} else
				sb.append("<b>Taille</b> : " + getSize() + "<br>");

			sb.append("<b>Dernière modification</b> : " + getLastModified()
					+ "<br>");

			return sb.append("</html>").toString();
		}

	}

	/**
	 * Retourne un texte descriptif d'un fichier.
	 * 
	 * @param f
	 *            le fichier à décrire.
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
	 * @return null si la copie a échoué, le fichier copié sinon
	 */
	public static File copy(File src, File dst) {
		// Si fichier => répertoire, on crée en fait "répertoire/fichier"
		if (dst.isDirectory())
			dst = new File(dst, src.getName());

		// On n'écrase pas si le fichier existe déjà
		if (dst.isFile() && dst.exists()) {
			int i = 1;
			while (dst.exists())
				dst = new File(dst.getParentFile(), dst.getName() + " (" + i++
						+ ")");
		}

		boolean resultat = false;

		// Déclaration des flux
		FileInputStream sourceFile = null;
		FileOutputStream destinationFile = null;

		try {
			// Création du fichier
			dst.createNewFile();

			// Ouverture des flux
			sourceFile = new FileInputStream(src);
			destinationFile = new FileOutputStream(dst);

			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			int nbLecture;

			while ((nbLecture = sourceFile.read(buffer)) != -1)
				destinationFile.write(buffer, 0, nbLecture);

			// Copie réussie
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

		if (!resultat)
			GU
					.warn("Impossible de copier le fichier : " + src + " vers "
							+ dst);
		else
			return dst;
	
		return null;
	}

	/**
	 * Déplace un fichier.
	 * 
	 * @param src
	 *            fichier source
	 * @param dst
	 *            fichier destination
	 * @return null si le déplacement a échoué, le fichier déplacé sinon
	 */
	public static File move(File src, File dst) {
		// Si fichier => répertoire, on crée en fait "répertoire/fichier"
		if (dst.isDirectory())
			dst = new File(dst, src.getName());

		// On n'écrase pas si le fichier existe déjà
		if (dst.isFile() && dst.exists()) {
			int i = 1;
			while (dst.exists())
				dst = new File(dst.getParentFile(), dst.getName() + " (" + i++
						+ ")");
		}
		
		// On essaye avec renameTo ou avec delete + copy
		boolean res = src.renameTo(dst) || ((dst = copy(src, dst)) != null && src.delete());

		if (!res)
			GU.warn("Impossible de déplacer le fichier : " + src + " vers "
					+ dst);
		
		return (res ? dst : null);
	}

	public static File rename(File f) {
		File newf = null;
		while (true) {
			newf = new File(f.getParentFile(), JOptionPane.showInputDialog(null, "Nouveau nom du fichier : ", "Renommer " + f.getName(), JOptionPane.QUESTION_MESSAGE));
			if (newf.exists())
				GU.warn("Ce fichier existe déjà.");
			else
				break;
		}

		if (f.renameTo(newf))
			return newf;
		else
			GU.warn("Impossible de renommer le fichier : " + f + " en " + newf);
		
		return null;		
	}
	
	/**
	 * Supprime un fichier ou répertoire (en récursif pour celui-ci) SANS
	 * demander confirmation à l'utilisateur. (utile si c'est le programme qui
	 * l'appelle lui-même, par exemple, le cas d'un couper/coller)
	 * 
	 * @param file
	 *            le fichier ou répertoire à supprimer
	 * @return <code>true</code> si la suppression a été correctement
	 *         effectuée
	 */
	public static boolean delete(File file, boolean dontConfirm) {
		// On ne prend pas ce cas
		if (!dontConfirm)
			return false;

		// Cas triviaux
		if (!file.exists()) {
			GU.warn("Le fichier : " + file + " n'existe pas.");
			return false;
		}

		if (file.isFile()) {
			if (!file.delete()) {
				GU.warn("Impossible de supprimer le fichier : " + file);
				return false;
			}
			return true;
		}

		// On a un répertoire, récursivité powa
		boolean resultat = true;

		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++)
			if (files[i].isDirectory())
				resultat &= delete(files[i]);
			else
				resultat &= files[i].delete();

		resultat &= file.delete();

		if (!resultat)
			GU.warn("Impossible de supprimer le répertoire : " + file);

		return resultat;
	}

	/**
	 * Supprime un fichier ou répertoire (en récursif pour celui-ci) en
	 * demandant confirmation à l'utilisateur.
	 * 
	 * @param file
	 *            le fichier ou répertoire à supprimer
	 * @return <code>true</code> si la suppression a été correctement
	 *         effectuée
	 */
	public static boolean delete(File file) {
		if (!GU.confirm("Êtes-vous sûr de vouloir supprimer le fichier : "
				+ file + " ?"))
			return false;
		return delete(file, true);
	}

	/**
	 * Renvoie un tableau contenant les noms des fichiers d'un tableau de File.
	 * 
	 * @param files
	 *            tableau de Files dont retournés les noms
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

	public static File createDirectory(File sel) {
		String s = "Nouveau dossier";

		// On crée le fichier au même niveau que la sélection
		File f = null;
		if (sel.getParentFile() != null)
			f = sel.getParentFile();
		else
			f = sel;

		File dir = new File(f, s);
		int i = 1;
		while (dir.exists())
			dir = new File(f, s + " (" + i++ + ")");

		if (!dir.mkdir())
			GU.warn("Impossible de créer de répertoire dans le dossier : "
					+ f.getAbsolutePath());
		else
			return dir;
		return null;
	}

	public static File createFile(File sel) {
		String s = "Nouveau fichier";

		// On crée le fichier au même niveau que la sélection
		File f = null;
		if (sel.getParentFile() != null)
			f = sel.getParentFile();
		else
			f = sel;

		File dir = new File(f, s);
		int i = 1;
		while (dir.exists())
			dir = new File(f, s + " (" + i++ + ")");

		try {
			if (dir.createNewFile())
				return dir;
		} catch (IOException e) {
		}
		GU.warn("Impossible de créer de fichier dans le dossier : "
				+ f.getAbsolutePath());
		return null;
	}

	public static void showProperties(File f) {
		GU.info("Pwet");
	}

	public static void openFile(File f) {
		if (f.isFile()) {
			JFileChooser jfc = new JFileChooser(f);
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				try {
					Runtime.getRuntime().exec(
							jfc.getSelectedFile().getAbsolutePath() + " "
									+ f.getAbsolutePath());
				} catch (IOException e) {
				}
		}
	}

}