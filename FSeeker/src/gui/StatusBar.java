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
 * Créer une barre d'état, contenant des labels, des barres de progression etc. Le tout étant
 * redimensionné automatiquement si les dimensions de la barre d'état est modifiée.
 * 
 * @author Sted
 */
public class StatusBar extends JPanel {
	/** Le layout contenu dans la barre (pour la redimension auto) */
	protected GridBagLayout gb = new GridBagLayout();

	/** Les contraintes associées */
	protected GridBagConstraints constraints = new GridBagConstraints();

	/** J4F */
	protected Timer timer = null;

	/**
	 * Créé une nouvelle barre d'état vide.
	 */
	public StatusBar() {
		super();
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		setLayout(gb);
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
	}

	/**
	 * Ajoute un composant à la barre d'état.
	 * 
	 * @param c composant à ajouter
	 * @deprecated 
	 */
	private void addComponent(Component c) {
		add(c);
		gb.setConstraints(c, constraints);
	}

	/**
	 * Ajoute un label dans la barre d'état.
	 * 
	 * @param s le texte du label
	 * @return une référence sur le label ajouté
	 */
	public JLabel addLabel(String s) {
		return addLabel(s, SwingConstants.CENTER);
	}

	/**
	 * Ajoute un label dans la barre d'état associé à des propriétés (centré, à droite etc)
	 * 
	 * @param s le texte du label
	 * @param propriete une propriété de JLabel
	 * @return une référence sur le label ajouté
	 */
	public JLabel addLabel(String s, int propriete) {
		JLabel label = new JLabel(" " + s + " ", propriete);
		label.setFont(new Font("Helvetica", Font.PLAIN, 12));

		addComponent(label);
		return label;
	}

	/**
	 * Ajoute une barre de progression définie de <code>min</code> à <code>max</code>.
	 * 
	 * @param min la valeur minimal de la barre de progression
	 * @param max la valeur maximal de la barre de progression
	 * @return une référence sur la barre de progression
	 */
	public JProgressBar addProgressBar(int min, int max) {
		final JProgressBar pb = new JProgressBar(min, max);

		// On veut rien afficher, comme ça, quand elle est à 0%, elle est invisible
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