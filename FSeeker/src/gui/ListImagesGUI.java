package gui;

/** liste des imports * */
import javax.swing.*;



import java.awt.*;
import misc.*;

import model.ListImagesModel;

import java.io.*;

//import javax.swing.event.*;

public class ListImagesGUI extends JPanel  {

	private int DefaultVisibleRow = 5;

	private final ListImagesModel m;

	private JList list;

	/**
	 * 
	 * @param listData
	 *            Elements to display in the JList
	 *  
	 */
	public ListImagesGUI(ListImagesModel m) {
		this.m = m;
		list = new JList(m);
		ImagesListDataListener  l1 = new ImagesListDataListener(m, list);
		list.addMouseListener(new ListMouseListener(list));
		list.setCellRenderer(new ImageCellRenderer());
		list.setVisibleRowCount(DefaultVisibleRow);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		JScrollPane sp = new JScrollPane(list);
		add(sp);
		

	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("test JList");
		JPanel jp = new JPanel();

		ListImagesModel model = new ListImagesModel(new File("/"));
		ListImagesGUI jli = new ListImagesGUI(model);
		jf.getContentPane().add(jli);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.pack();
	}



}

class ImageCellRenderer extends JLabel implements ListCellRenderer {

	//TODO gérer les extensions de fichier (images associées) àl'aide d'une
	// HashMap
	ImageIcon textIcon = GU.createImg("txt.png");

	int space = 1;

	Color backColor = Color.black;

	Color foreColor = Color.white;

	/*
	 * @returns un composant gérant le rendu d'une cellule pour un fichier
	 * 
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, // value
			// to
			// display
			int index, // cell index
			boolean isSelected, // is the cell selected
			boolean cellHasFocus) { // the list and the cell have the focus
		File file = (File) value;
		String s = file.getName();
		setIcon(textIcon);
		setText(s);
		String newLine = "\n";
		setToolTipText("Name : " + file + newLine + file.length());
		setIconTextGap(space);//espace entre image et texte
		setVerticalTextPosition(JLabel.BOTTOM);//Texte en dessous de
		// l'image
		setHorizontalTextPosition(JLabel.CENTER);
		setBackground(isSelected ? backColor : foreColor);
		setForeground(isSelected ? foreColor : backColor);
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true); //Ne pas retirer
		return this;
	}
}



