package gui;

/** liste des imports * */
import event.ListMouseListener;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import misc.GU;
import model.ListImagesModel;

//import javax.swing.event.*;

/**
 * @author brahim
 */
public class ListImagesGUI extends JPanel {

	private int DefaultVisibleRow = 5;

	private final ListImagesModel m;

	private JList list;

	/**
	 * 
	 * @param m
	 *            modèle qui va permettre de représenter le systéme de fichier
	 *  
	 */
	public ListImagesGUI(ListImagesModel m) {
		this.m = m;
		list = new JList(m);
		ImagesListDataListener l1 = new ImagesListDataListener(m, list);
		list.addMouseListener(new ListMouseListener(this));
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

	public JList getList() {
		return list;
	}

}

class ImageCellRenderer extends JLabel implements ListCellRenderer {

	// TODO gérer les extensions de fichier (images associées) à l'aide d'une
	// HashMap -> voir TODO
	private Icon textIcon = GU.getImage("txt.png");

	private int space = 1;

	private Color backColor = Color.black;

	private Color foreColor = Color.white;

	/*
	 * @returns un composant gérant le rendu d'une cellule pour un fichier
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		File file = (File) value;
		String s = file.getName();
		setIcon(textIcon);
		setText(s);
		String newLine = "\n";
		setToolTipText("Name : " + file + newLine + file.length());
		setIconTextGap(space);
		
		// Texte en dessous de l'image
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalTextPosition(JLabel.CENTER);
		
		setBackground(isSelected ? backColor : foreColor);
		setForeground(isSelected ? foreColor : backColor);
		
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);
		return this;
	}
}

