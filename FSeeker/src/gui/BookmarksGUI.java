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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import model.FSeekerModel;

/**
 * @author sted
 */
public class BookmarksGUI extends JList {
	protected DefaultListModel m = new DefaultListModel();

	protected static Color listColor = null;

	protected FSeekerModel fsm = null;

	public void addBookmark(String t, File f) {
		m.addElement(new Bookmark(t, f));
	}

	
	private class Hu implements Serializable {
		public Object[] bookmarks = null;
		public Hu(Object[] bookmarks) {
			this.bookmarks = bookmarks;
		}
		
	}
	
	public void write() {
		try {
			FileOutputStream fos = new FileOutputStream("bookmarks");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			Hu hu = new Hu(m.toArray());
			oos.writeObject(hu);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public Object[] read() {
		try {
			FileInputStream fis = new FileInputStream("bookmarks");
			System.out.println("lol");
			ObjectInputStream ois = new ObjectInputStream(fis);
			System.out.println("lol2");
			Object[] bookmarks = ((Hu) ois.readObject()).bookmarks;
			System.out.println("lol3");
			ois.close();
			return bookmarks;
		} catch (FileNotFoundException e) {
			System.out.println("aucun favoris");
		} catch (IOException e) {
			System.out.println("erreur d'io");
		} catch (ClassNotFoundException e) {
			System.out.println("classe introuvable");
		}
		return null;
	}

	public BookmarksGUI(FSeekerModel fsm) {
		super();
		this.fsm = fsm;

		listColor = getBackground();
		setCellRenderer(new BookmarksRenderer());
		setModel(m);
		
		Object bookmarks[] = read();
		if (bookmarks != null) {
			System.out.println("récup");
			for (int i = 0; i < bookmarks.length; i++)
				m.addElement(bookmarks[i]);
		}
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)
						&& e.getClickCount() == BookmarksGUI.this.fsm
								.getClickCount()) {
					Bookmark b = (Bookmark) getSelectedValue();
					if (b == null)
						return;
					BookmarksGUI.this.fsm.setURI(b.getFile());
				}
			}
		});

		DropTarget dt = new DropTarget(this, new DropTargetAdapter() {
			public void drop(DropTargetDropEvent dtde) {
				Transferable tr = dtde.getTransferable();
				if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					try {
						String file = (String) tr
								.getTransferData(DataFlavor.stringFlavor);
						StringTokenizer st = new StringTokenizer(file, "\t");
						if (!st.hasMoreTokens()) {
							dtde.rejectDrop();
							return;
						}

						file = st.nextToken();
						File f = new File(file);
						addBookmark(f.getName(), f);
						dtde.dropComplete(true);
						write();

					} catch (UnsupportedFlavorException e) {
						dtde.rejectDrop();
					} catch (IOException e) {
						dtde.rejectDrop();
					}
				} else
					dtde.rejectDrop();

			}
		});

		dt.setActive(true);
		setDropTarget(dt);
	}

	private class Bookmark {
		protected File f = null;

		protected String t = null;

		public Bookmark(String t, File f) {
			this.t = t;
			this.f = f;
		}

		public File getFile() {
			return f;
		}

		public String getTitle() {
			return t;
		}

		public String toString() {
			return t + " [" + f.getAbsolutePath() + "]";
		}
	}

	private static class BookmarksRenderer implements ListCellRenderer {
		private static JPanel p = new JPanel(new GridLayout(2, 1));

		private static JLabel dir = new JLabel();

		private static JLabel name = new JLabel();
		static {
			name.setHorizontalAlignment(SwingConstants.CENTER);
			name.setFont(new Font("Dialog", Font.BOLD, 18));

			dir.setForeground(Color.LIGHT_GRAY);
			dir.setHorizontalAlignment(SwingConstants.RIGHT);
			dir.setFont(new Font("Dialog", Font.ITALIC, 12));

			p.setBackground(listColor);
			p.add(name);
			p.add(dir);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

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

			return new JLabel(value.toString());
		}
	}

}