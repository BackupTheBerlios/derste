/*
 * Created on 6 nov. 2004
 */
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import misc.GU;
import misc.ImagesMap;
import model.URIModel;

/**
 * @author sted
 */
public class ToolBarGUI extends JToolBar {

	protected FSeeker fs = null;
	
	public ToolBarGUI(final FSeeker fs) {
		super(JToolBar.HORIZONTAL);
		this.fs = fs;
		
		setLayout(new BorderLayout());
		setFloatable(false);

		add(getIcons(), BorderLayout.NORTH);
		add(getAddress(), BorderLayout.SOUTH);
	}

	protected JPanel getAddress() {
		URIModel uriModel = new URIModel(fs.getModel());
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("Adresse : "), BorderLayout.WEST);
		p.add(new URIGUI(uriModel), BorderLayout.CENTER);

		return p;
	}

	protected JPanel getIcons() {
		// La barre d'icônes
		JPanel p = null;
		JButton b = null;

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		b = new JButton();
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GU.info("Précédent !");
			}
		});
		b.setBorder(null);
		b.setIcon(ImagesMap.get("previous.gif"));
		b.setToolTipText("Précédent");
		p.add(b);

		b = new JButton();
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GU.info("Précédent !");
			}
		});
		b.setBorder(null);
		b.setIcon(ImagesMap.get("next.gif"));
		b.setToolTipText("Suivant");
		p.add(b);

		b = new JButton();
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.getModel().gotoParent();
			}
		});
		b.setBorder(null);
		b.setIcon(ImagesMap.get("parent.gif"));
		b.setToolTipText("Répertoire parent");
		p.add(b);

		b = new JButton("Rechercher");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GU.info("Précédent !");
			}
		});
		b.setBorder(null);
		b.setIcon(ImagesMap.getDefault());
		b.setToolTipText("Rechercher un fichier ou un répertoire");
		p.add(b);

		b = new JButton();
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fs.setView(fs.getDefaultView());
			}
		});
		b.setBorder(null);
		b.setIcon(ImagesMap.getDefault());
		b.setToolTipText("Affichage avec l'arborescence sous forme d'arbre");
		p.add(b);

		return p;
	}

}

