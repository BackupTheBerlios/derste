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
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

/**
 * @author sted
 */
public class BookmarksGUI extends JList {
	protected DefaultListModel m = new DefaultListModel();

	protected static Color listColor = null;

	public BookmarksGUI() {
		super();
		setCellRenderer(new BookmarksRenderer());
		setModel(m);
		Bookmark b = new Bookmark("Racine", new File("/"));
		m.addElement(b);
		b = new Bookmark("Home", new File("/home/sted"));
		m.addElement(b);
		listColor = getBackground();

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