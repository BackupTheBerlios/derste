/*
 * Created on 24 oct. 2004
 */
package misc.file;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import misc.GU;

/**
 * Classe utilitaire portant sur les fichiers.
 * 
 * @author Sted
 * @author aitelhab
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

		private static Map extensions = new HashMap();

		private final static String EXTENSIONS_FILE = "extensions.txt";

		private static String readExtension(String extension) {
			if (extensions.containsKey(extension))
				return (String) extensions.get(extension);

			if (!new File(EXTENSIONS_FILE).exists())
				return null;

			BufferedReader file = null;
			try {
				String ligne;
				file = new BufferedReader(new FileReader(EXTENSIONS_FILE));

				while ((ligne = file.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(ligne);
					String ext = st.nextToken();
					if (ext.equals(extension)) {
						String desc = st.nextToken("\n");
						extensions.put(extension, desc);
						return desc;
					}
				}

			} catch (FileNotFoundException exc) {
			} catch (IOException e) {
			} finally {
				try {
					file.close();
				} catch (IOException e) {
				}
			}

			return null;
		}

		public String getType() {
			if (type == null)
				if (f.isDirectory())
					type = "Dossier";
				else {
					String s = f.getName();
					int rindex = s.lastIndexOf('.');

					// Y'a pas de '.' ou bien on a un fichier genre : "truc."
					if (rindex == -1 || rindex + 1 >= s.length())
						type = "Fichier";
					else {
						String extension = s.substring(rindex + 1);
						type = readExtension(extension.toLowerCase());
						if (type == null)
							type = "Fichier";
					}
				}
			
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

		public File getFile() {
			return f;
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

			// Le type et le nom (pour les aveugles)
			sb.append("<b>Nom</b> : " + getName() + "<br>");
			sb.append("<b>Type</b> : " + getType() + "<br>");

			if (f.isDirectory()) {
				if (!"".equals(getSize()))
					sb.append("<b>Contient</b> : " + getSize() + "<br>");
			} else
				sb.append("<b>Taille</b> : " + getSize() + "<br>");

			sb.append("<b>Dernière modification</b> : " + getLastModified());

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
		boolean res = src.renameTo(dst)
				|| ((dst = copy(src, dst)) != null && src.delete());

		if (!res)
			GU.warn("Impossible de déplacer le fichier : " + src + " vers "
					+ dst);

		return (res ? dst : null);
	}

	public static File rename(File f) {
		// On regarde si la personne peut écrire dans le répertoire
		if (!f.getParentFile().canWrite()) {
			GU.warn("Impossible de renommer");
			return null;
		}
		File newf = null;
		while (true) {
			newf = new File(f.getParentFile(), JOptionPane.showInputDialog(
					null, "Nouveau nom du fichier : ", "Renommer "
							+ f.getName(), JOptionPane.QUESTION_MESSAGE));
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

	/**
	 * Renvoie le mime type d'un fichier
	 * 
	 * @param file
	 * 
	 * @return une <code>String</code> correspondant au MIME TYPE
	 */
	public static String getMIMEType(File file) {
		//if(file.isDirectory()){return "repertoire";}
		//if(!file.exists()){return "fichier inexistant";}

		try {
			URL url = file.toURL();
			URLConnection connection = url.openConnection();
			return connection.getContentType();
		} catch (MalformedURLException mue) {
			return mue.getMessage();
		} catch (IOException ioe) {
			return ioe.getMessage();
		}
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

