/*
 * Créé le 20 oct. 2004
 */
package gui;

import javax.swing.*;
import javax.swing.table.*;

import controler.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import model.*;
import renderer.*;

/**
 * @author aitelhab
 *  
 */
public class FileTableGUI extends JTable /* implements Observer */{

	protected FileTableModel m = null;

	public FileTableGUI(FileTableModel m) {
		super(m);
		this.m = m;
		//m.addObserver(this);

		getTableHeader().setReorderingAllowed(false);

		setDefaultRenderer(Object.class, new FileTableCellRenderer());

		sortAllRows();

		setShowGrid(false);
		setShowHorizontalLines(true);
		setGridColor(Color.LIGHT_GRAY);

		setRowHeight(25);
		setRowMargin(5);

		setDragEnabled(true);

		//setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//setCellSelectionEnabled(false);
		//setLayout(new FlowLayout());

		addMouseListener(new TableModelControler(m));
	}

	// dans le modèle ca
	public void sortAllRows() {
		Vector data = m.getDataVector();
		Collections.sort(data, new Comparator() {
			public int compare(Object a, Object b) {
				Vector v1 = (Vector) a;
				Vector v2 = (Vector) b;
				Object o1 = v1.get(0);
				Object o2 = v2.get(0);

				return m.getModel().getComparator().compare(o1, o2);
			}
		});

		m.fireTableStructureChanged();
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/*public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		Component c = super.prepareRenderer(renderer, row, column);

		// Have fun !
		if (row % 2 == 0 && !isCellSelected(row, column)) {
			c.setBackground(Color.CYAN);
		} else if (isCellSelected(row, column)) {
			c.setBackground(Color.BLUE);
		} else {
			c.setBackground(getBackground());
		}

		return c;
	}*/

	/*
	 * public void update(Observable o, Object arg) {
	 * System.out.println("update() de la JTable appelé"); //Mettre à jour la
	 * vue revalidate(); repaint(); }
	 */

	public static void main(String[] args) {
		FSeekerModel fsm = new FSeekerModel(new File("/home/brahim/"));
		FileTableModel model = new FileTableModel(fsm);
		FileTableGUI vue = new FileTableGUI(model);
		JFrame frame = new JFrame("Test vue détail");

		JScrollPane jsp = new JScrollPane(vue);
		frame.getContentPane().add(jsp);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}