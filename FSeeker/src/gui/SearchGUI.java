/*
 * Created on 7 nov. 2004
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import misc.file.FileUtilities.FileDetails;
import renderer.FileTableCellRenderer;

/**
 * Panel de recherche.
 * 
 * @author Sted
 */
public class SearchGUI extends JPanel {

	/** Le pattern (motif � rechercher) */
	protected JTextField pattern = new JTextField();

	/** Le chemin d'o� commencer */
	protected JTextField where = new JTextField();

	/** Bouton rechercher */
	protected JButton ok = new JButton("Rechercher");

	/** Bouton arr�ter la rechercher */
	protected JButton stop = new JButton("Arr�ter");

	/** Le path de la recherche (where.getText()) */
	protected String searchPath = null;

	/** La fen�tre principale */
	protected FSeeker fs = null;

	/** Les colonnes dans le mod�le */
	protected static Vector colNames = new Vector(5);
	static {
		colNames.addElement("Nom du fichier");
		colNames.addElement("Chemin");
		colNames.addElement("Type");
		colNames.addElement("Taille");
		colNames.addElement("Date de Modification");
	}

	/**
	 * Effectue une recherche � partir d'un fichier.
	 * 
	 * @author Sted
	 */
	class Search {

		/** Le pattern */
		protected String pattern = null;

		/** A partir de o� ? */
		protected File where = null;

		/** On continue dans le thread ou pas ? (il s'arr�te le cas �ch�ant) */
		private boolean continueSearch = true;

		/** Le mod�le de table qui contiendra les r�sultats */
		protected DefaultTableModel dtm = null;

		/**
		 * Construit une recherche � partir d'un fichier de d�part et d'un
		 * pattern.
		 * 
		 * @param dtm
		 *            le mod�le o� ajouter les r�sultats
		 * @param where
		 *            de o� on commence
		 * @param pattern
		 *            le mod�le � chercher
		 */
		public Search(DefaultTableModel dtm, File where, String pattern) {

			// Chemin de d�part
			this.where = where;

			// Le mod�le dans lequel on va ajouter les r�sultats
			this.dtm = dtm;

			// On �chappe tous les caract�res foireux
			String pfinal = "";
			for (int i = 0; i < pattern.length(); i++) {
				if (pattern.charAt(i) == '*')
					pfinal += ".*";
				else if (!Character.isLetterOrDigit(pattern.charAt(i)))
					pfinal += "\\" + pattern.charAt(i);
				else
					pfinal += pattern.charAt(i);
			}

			// PAN, on a notre pattern �chapp�
			this.pattern = pfinal;

		}

		/**
		 * D�marre la recherche.
		 */
		public void start() {
			continueSearch = true;
			new SearchThread(where).start();
		}

		/**
		 * Arr�te la recherche.
		 */
		public void stop() {
			continueSearch = false;
		}

		/**
		 * Le thread qui fait la recherche. Un thread pour �viter de freezer
		 * Swing.
		 */
		class SearchThread extends Thread {

			/** De o� on commence */
			protected File where = null;

			/**
			 * Construit le thread avec un fichier de d�part
			 * 
			 * @param where
			 *            de o� on commence
			 */
			public SearchThread(File where) {
				super();
				this.where = where;
			}

			/**
			 * Fonction qui sera appel� r�cursivement, cherche le mod�le dans le
			 * fichier in.
			 * 
			 * @param in
			 *            o� chercher
			 */
			public void searchIn(File in) {
				if (in == null)
					return;
				File[] files = in.listFiles();
				if (files == null)
					return;
				int nbChildren = files.length;

				for (int i = 0; i < nbChildren; i++) {

					if (!continueSearch)
						return;

					File f = files[i];

					// Attention, ca peut arriver (et c'est d�j� arriv�..!)
					if (f == null)
						continue;

					// On a un _fichier_ ou on a un _r�pertoire_
					// TODO lien symbolique, ne pas y aller :\\
					if (!f.isDirectory()) {

						if (f.getName().matches(".*" + pattern + ".*")) {
							Vector v = new Vector(5);
							// "Nom du fichier" "Chemin" "Type" "Taille" "Date
							// de Modification"
							FileDetails fd = new FileDetails(f);
							v.add(f);
							v.add(f.getAbsolutePath());
							v.add(fd.getType());
							v.add(fd.getSize());
							v.add(fd.getLastModified());
							addResult(v);
						}

					} else {
						searchIn(f);
					}
				}
			}

			/**
			 * Au lancement du thread, on cherche au chemin de d�part.
			 */
			public void run() {
				searchIn(where);
			}
		}

		/**
		 * Ajoute un r�sultat dans le mod�le.
		 * 
		 * @param data
		 *            le vector avec la ligne de r�sultats
		 */
		protected void addResult(Vector data) {
			dtm.addRow(data);
		}
	}

	/**
	 * Ajoute un composant comp, dans le panel p, avec les contraintes c.
	 * 
	 * @param comp
	 *            un composant
	 * @param c
	 *            une contrainte
	 * @param p
	 *            un panel o� mettre le composant
	 */
	protected void make(JComponent comp, GridBagConstraints c, JPanel p) {
		GridBagLayout gridbag = (GridBagLayout) p.getLayout();
		gridbag.setConstraints(comp, c);
		p.add(comp);
	}

	/**
	 * Modifie le chemin de d�part � f. Appel� par FSeeker pour mettre
	 * automatiquement ce chemin � celui en cours de vue.
	 * 
	 * @param f
	 *            un fichier
	 */
	public void setSearchPath(File f) {
		searchPath = f.toString();
		where.setText(searchPath);
	}

	public SearchGUI(FSeeker fs) {
		super(new BorderLayout());
		this.fs = fs;

		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		JPanel p = new JPanel(layout);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(6, 6, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;

		// Champs recherche
		make(new JLabel("Recherche : "), c, p);
		c.ipady = 0;
		make(pattern, c, p);

		// Espace
		c.ipady = 20;
		make(new JLabel(), c, p);
		c.ipady = 0;
		make(new JLabel("A partir de : "), c, p);

		// Champs ou�reee
		make(where, c, p);

		c.gridwidth = 1;
		make(ok, c, p);
		c.gridwidth = GridBagConstraints.REMAINDER;
		make(stop, c, p);

		add(p, BorderLayout.NORTH);

		// LISTENERS //////////////////////////////////////

		stop.setEnabled(false);

		// Quand on fera <entr�e>, ca lancera la recherche, pas besoin de
		// cliquer
		pattern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok.doClick();
			}
		});

		// Go !
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok.setEnabled(false);
				stop.setEnabled(true);

				// Un nouveau mod�le vide
				DefaultTableModel dtm = new DefaultTableModel(colNames, 0);
				JTable results = new JTable(dtm);

				// Le renderer associ�
				FileTableCellRenderer renderer = new FileTableCellRenderer();
				results.setDefaultRenderer(Object.class, renderer);
				results.setDefaultRenderer(File.class, renderer);

				// On ajoute un nouveau tab dans FSeeker
				SearchGUI.this.fs.addTab("Recherche : " + searchPath, null,
						new JScrollPane(results), "R�sultats de la recherche");

				// On lance la recherche !
				final Search search = new Search(dtm,
						new File(where.getText()), pattern.getText());
				search.start();

				// Permettra de tout arr�ter
				stop.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						search.stop();
						stop.setEnabled(false);
						ok.setEnabled(true);
					}
				});
			}
		});
	}
}