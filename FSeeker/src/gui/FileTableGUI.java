/*
 * Cr�� le 20 oct. 2004
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
 * Classe permettant la repr�sentation d'un syst�me de fichier sous forme d'un
 * tableau, en affichant les d�tails de ces fichiers.
 * 
 * @author aitelhab
 * @author Sted
 */
public class FileTableGUI extends JTable {

	// private final static int ROWMARGIN = 5;

	/** Trieur de colonnes */
	private TableHeaderSorter sorter = null;

	/** La hauteur d'une ligne */
	private final static int ROWHEIGHT = 30;

	/** Le mod�le associ� */
	private FileTableModel m = null;

	/**
	 * Construit une vue de d�tails � partir d'un mod�le.
	 * 
	 * @param m
	 *            un mod�le
	 */
	public FileTableGUI(FileTableModel m) {
		super(m);
		this.m = m;
		getTableHeader().setReorderingAllowed(false);

		FileTableCellRenderer renderer = new FileTableCellRenderer(m);
		this.setDefaultRenderer(Object.class, renderer);
		this.setDefaultRenderer(Long.class, renderer);
		this.setDefaultRenderer(Date.class, renderer);
		this.setDefaultRenderer(File.class, renderer);

		// JHeader pour le tri
		if (m.getMode() == FileTableModel.SIMPLE_MODE)
			sorter = new TableHeaderSorter(this, m.getModel());

		// propri�t�s de la JTable
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

}