package gui;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

/*
 * Created on 12 oct. 2004
 * 
 * TODO: Utiliser JToolBar ?
 */

/**
 * Cr�er une barre d'�tat, contenant des labels, des barres de progression etc. Le tout �tant
 * redimensionn� automatiquement si les dimensions de la barre d'�tat est modifi�e.
 * 
 * @author Sted
 */
public class StatusBar extends JPanel {
	/** Le layout contenu dans la barre (pour la redimension auto) */
	protected GridBagLayout gb = new GridBagLayout();

	/** Les contraintes associ�es */
	protected GridBagConstraints constraints = new GridBagConstraints();

	/** J4F */
	protected Timer timer = null;

	/**
	 * Cr�� une nouvelle barre d'�tat vide.
	 */
	public StatusBar() {
		super();
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		setLayout(gb);
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
	}

	/**
	 * Ajoute un composant � la barre d'�tat.
	 * 
	 * @param c composant � ajouter
	 * @deprecated 
	 */
	private void addComponent(Component c) {
		add(c);
		gb.setConstraints(c, constraints);
	}

	/**
	 * Ajoute un label dans la barre d'�tat.
	 * 
	 * @param s le texte du label
	 * @return une r�f�rence sur le label ajout�
	 */
	public JLabel addLabel(String s) {
		return addLabel(s, SwingConstants.CENTER);
	}

	/**
	 * Ajoute un label dans la barre d'�tat associ� � des propri�t�s (centr�, � droite etc)
	 * 
	 * @param s le texte du label
	 * @param propriete une propri�t� de JLabel
	 * @return une r�f�rence sur le label ajout�
	 */
	public JLabel addLabel(String s, int propriete) {
		JLabel label = new JLabel(" " + s + " ", propriete);
		label.setFont(new Font("Helvetica", Font.PLAIN, 12));

		addComponent(label);
		return label;
	}

	/**
	 * Ajoute une barre de progression d�finie de <code>min</code> � <code>max</code>.
	 * 
	 * @param min la valeur minimal de la barre de progression
	 * @param max la valeur maximal de la barre de progression
	 * @return une r�f�rence sur la barre de progression
	 */
	public JProgressBar addProgressBar(int min, int max) {
		final JProgressBar pb = new JProgressBar(min, max);

		// On veut rien afficher, comme �a, quand elle est � 0%, elle est invisible
		// tout en occupant l'espace (PAS de setVisible(false))
		pb.setBorderPainted(false);
		pb.setStringPainted(false);

		pb.setValue(0);

		addComponent(pb);

		// PIPOT TEST pour la faire bouger //////////////////////////////
		timer = new Timer(30, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (pb.getValue() == 100) {
					//timer.stop();
					pb.setValue(0);
					//System.out.println("stop");
				} else {
					pb.setValue(pb.getValue() + 1);
				}
			}
		});
		timer.start();

		// PIPOT///////////////////////////////////

		return pb;
	}

}