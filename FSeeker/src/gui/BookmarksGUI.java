/*
 * Created on 7 nov. 2004
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import misc.GU;
import misc.PopupManager;
import model.FSeekerModel;

/**
 * Construit une liste de bookmarks.
 * 
 * @author Sted
 */
public class BookmarksGUI extends JList {

	/** Le omdèle contenant les bookmarks */
	protected DefaultListModel m = new DefaultListModel();

	/** La couleur de fond de la liste (pour colorer le panel du renderer avec) */
	protected static Color listColor = null;

	/** Le nom du fichier où sauvegarder les bookmarks */
	protected String BOOKMARKS_FILE = "bookmarks";

	/** Le délimiteur utilisé dans le fichier des bookmarks */
	protected String BOOKMARK_DELIMITER = "$@$";

	/** Le supra-modèle */
	protected FSeekerModel fsm = null;

	/**
	 * Ajoute un bookmark à la liste.
	 * 
	 * @param t
	 *            le titre du bookmark
	 * @param f
	 *            son emplacement
	 * @return le bookmark ajouté
	 */
	public Bookmark addBookmark(String t, File f) {
		Bookmark b = new Bookmark(t, f);
		m.addElement(b);
		return b;
	}

	/**
	 * Écrit les bookmarks dans le fichier dédié.
	 */
	public void writeBookmarks() {
		PrintWriter file = null;
		try {
			// A chaque fois une boucle pour ne pas se prender la tête avec les
			// deletes
			Bookmark b = null;
			file = new PrintWriter(new BufferedWriter(new FileWriter(
					BOOKMARKS_FILE)));
			for (int i = 0; i < m.getSize(); i++) {
				b = (Bookmark) m.get(i);
				file.println(b.getTitle() + BOOKMARK_DELIMITER + b.getFile());
			}
		} catch (IOException e) {
		} finally {
			file.close();
		}
	}

	/**
	 * Charge les bookmarks du fichier.
	 */
	public void loadBookmarks() {
		if (!new File(BOOKMARKS_FILE).exists())
			return;

		BufferedReader file = null;
		try {
			String ligne;
			file = new BufferedReader(new FileReader(BOOKMARKS_FILE));

			while ((ligne = file.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(ligne,
						BOOKMARK_DELIMITER);
				if (st.hasMoreTokens()) {
					String title = st.nextToken();
					if (st.hasMoreTokens())
						addBookmark(title, new File(st.nextToken()));
				}
			}

		} catch (FileNotFoundException exc) {
		} catch (IOException e) {
			System.err.println("Erreur durant la lecture du fichier : "
					+ BOOKMARKS_FILE);
		} finally {
			try {
				file.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Construit la liste des bookmarks en liaison avec le supra-modèle pour
	 * pouvoir cliquer sur un bookmark dans la liste, et s'y rendre directement.
	 * 
	 * @param fsm
	 *            le supra-modèle
	 */
	public BookmarksGUI(FSeekerModel fsm) {
		super();
		this.fsm = fsm;

		listColor = getBackground();
		setCellRenderer(new BookmarksRenderer());
		setModel(m);

		loadBookmarks();

		// Gère <entrée> et <del>
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					m.removeElement(getSelectedValue());
					writeBookmarks();
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Bookmark b = (Bookmark) getSelectedValue();
					if (b != null)
						BookmarksGUI.this.fsm.setURI(b.getFile());
				}
			}
		});

		// On gère le clic
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)
						&& e.getClickCount() == BookmarksGUI.this.fsm
								.getClickCount()) {
					Bookmark b = (Bookmark) getSelectedValue();
					if (b == null)
						return;
					BookmarksGUI.this.fsm.setURI(b.getFile());
				} else if (SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu popup = new JPopupMenu();
					JMenuItem menuItem = new JMenuItem("Ouvrir");
					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Bookmark b = (Bookmark) getSelectedValue();
							if (b != null)
								BookmarksGUI.this.fsm.setURI(b.getFile());
						}
					});
					popup.add(menuItem);
					menuItem = new JMenuItem("Supprimer");
					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (getSelectedValue() != null) {
								m.removeElement(getSelectedValue());
								writeBookmarks();
							}
						}
					});
					popup.add(menuItem);
					PopupManager.showPopup(e, popup);
				}
			}
		});

		/* Récupère un drop et l'ajoute aux bookmarks */
		DropTarget dt = new DropTarget(this, new DropTargetAdapter() {
			public void drop(DropTargetDropEvent dtde) {
				Transferable tr = dtde.getTransferable();
				if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					try {
						String file = (String) tr
								.getTransferData(DataFlavor.stringFlavor);

						/*
						 * Dans une table, c'est : fichier\ttype\ttaille etc.
						 * Dans la liste, y'a pas de \t mais y'a quand même un
						 * token !
						 */
						StringTokenizer st = new StringTokenizer(file, "\t");
						if (!st.hasMoreTokens()) {
							dtde.rejectDrop();
							return;
						}

						file = st.nextToken();
						File f = new File(file);
						String title;
						while ((title = GU.input("Titre du bookmark :"))
								.equals(""))
							;

						addBookmark(title, f);
						writeBookmarks();
						dtde.dropComplete(true);

					} catch (UnsupportedFlavorException e) {
						dtde.rejectDrop();
					} catch (IOException e) {
						dtde.rejectDrop();
					}
				} else
					dtde.rejectDrop();

			}
		});

		dt.setActive(true); // Obligatoire
		setDropTarget(dt);
	}

	/**
	 * Un bookmark est un fichier destination, et un titre, pour mieux le
	 * repérer.
	 */
	private class Bookmark {
		/** Le fichier cible */
		protected File f = null;

		/** Son titre */
		protected String t = null;

		/**
		 * Créer un bookmark.
		 * 
		 * @param t
		 *            un titre
		 * @param f
		 *            un fichier
		 */
		public Bookmark(String t, File f) {
			this.t = t;
			this.f = f;
		}

		/**
		 * Retourne le fichier cible.
		 * 
		 * @return le fichier cible
		 */
		public File getFile() {
			return f;
		}

		/**
		 * Retourne le titre.
		 * 
		 * @return le titre
		 */
		public String getTitle() {
			return t;
		}
	}

	/**
	 * Le renderer pour représenter la liste des bookmarks.
	 */
	private static class BookmarksRenderer implements ListCellRenderer {
		/** Le panel où sera affiché le titre + chemin */
		private static JPanel p = new JPanel(new GridLayout(2, 1));

		/** Le label avec le fichier cible */
		private static JLabel dir = new JLabel();

		/** Le titre */
		private static JLabel name = new JLabel();
		static {
			name.setHorizontalAlignment(SwingConstants.LEFT);
			name.setFont(new Font("Dialog", Font.BOLD, 18));

			dir.setForeground(Color.LIGHT_GRAY);
			dir.setHorizontalAlignment(SwingConstants.LEFT);
			dir.setFont(new Font("Dialog", Font.ITALIC, 12));

			p.setBackground(listColor);
			p.add(name);
			p.add(dir);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			// Si on a bien un bookmark
			if (value != null && value instanceof Bookmark) {
				if (isSelected) {
					p.setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));
					p.setBackground(new Color(200, 210, 250));
					dir.setForeground(Color.GRAY);
				} else {
					p.setBorder(null);
					p.setBackground(listColor);
					dir.setForeground(Color.LIGHT_GRAY);
				}

				Bookmark b = (Bookmark) value;
				name.setText(b.getTitle());
				dir.setText(b.getFile().getAbsolutePath());
				return p;
			}

			// Ne devrait jamais arriver
			return new JLabel(value.toString());
		}
	}

}