/*
 * Créé le 20 oct. 2004
 */
package gui;

import java.io.File;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import model.FileTableModel;
import renderer.FileTableCellRenderer;
import controler.FileTableControler;

/**
 * Classe permettant la représentation d'un système de fichier sous forme d'un
 * tableau, en affichant les détails de ces fichiers.
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableGUI extends JTable {

	// private final static int ROWMARGIN = 5;
    private TableHeaderSorter sorter;
	private final static int ROWHEIGHT = 30;

	private FileTableModel m = null;

	public FileTableGUI(FileTableModel m) {
		super(m);
		this.m = m;
		getTableHeader().setReorderingAllowed(false);
		

		FileTableCellRenderer renderer = new FileTableCellRenderer();
		this.setDefaultRenderer(Object.class, renderer);
		this.setDefaultRenderer(Long.class, renderer);
		this.setDefaultRenderer(Date.class, renderer);
		this.setDefaultRenderer(File.class, renderer);

		//JHeader pour le tri
		if(m.getMode() == FileTableModel.SIMPLE_MODE){
		    sorter = new TableHeaderSorter(this, m.getModel());
		  
		}
		
		
		
		// propriétés de la JTable
		setShowGrid(false);
		// setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Fixe la taille des lignes et colonnes
		setRowHeight(ROWHEIGHT);
		// packColumns(this, ROWMARGIN);

		setDragEnabled(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		FileTableControler ftc = new FileTableControler(m, this);
		addMouseListener(ftc);
		addKeyListener(ftc);
		m.addSelectionChangedListener(ftc);
		
	}

	/*
	public void packColumns(JTable table, int margin) {
		for (int c = 0; c < table.getColumnCount(); c++) {
			packColumn(table, c, 2);
		}
	}
	
	public void packColumn(JTable table, int vColIndex, int margin) {
		TableModel model = table.getModel();
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
				.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;

		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(table, col
				.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		// Get maximum width of column data
		for (int r = 0; r < table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(table, table
					.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		// Add margin
		width += 2 * margin;

		// Set the width
		col.setPreferredWidth(width);
	}*/

}