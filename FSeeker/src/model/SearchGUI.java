/*
 * Créé le 13 oct. 2004
 */

package model;

import gui.SwingWorker;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import misc.GU;
import misc.ImagesMap;

/**
 * @author derosias
 *//*
public class SearchGUI extends JFrame {
	protected JTextField pattern = new JTextField(20);

	protected JButton ok = new JButton("Rechercher");

	protected JButton stop = new JButton("Arrêter");

	protected JButton cancel = new JButton("Fermer");

	protected JLabel searchPath = null;

	protected DefaultListModel mresults = new DefaultListModel();

	protected JList lresults = new JList(mresults);

	protected SearchModel sm = null;

	{
		stop.setEnabled(false);
		pattern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok.doClick();
			}
		});

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok.setEnabled(false);
				stop.setEnabled(true);
				mresults.clear();

				final SwingWorker worker = new SwingWorker() {
					public Object construct() {
						final Search s = new Search(SearchGUI.this, sm.getRootPath(),
								pattern.getText());

						stop.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								s.stop();
							}
						});
						
						s.search();
						return s;
					}

					public void finished() {
						ok.setEnabled(true);
						stop.setEnabled(false);

						System.out.println(java.util.Arrays.asList(
								((Search) get()).getResults()).toString());

						setSearchPath("");
					}
				};
				worker.start();

				

			}
		});

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}

	public void setSearchPath(String s) {
		searchPath.setText(s);
	}

	public void addResult(String s) {
		mresults.addElement(s);
	}

	public SearchGUI(SearchModel sm) {
		super("Recherche");

		this.sm = sm;

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		JToolBar sbt = new JToolBar();
		JLabel l = new JLabel("Favoris");
		sbt.add(l);
		l.setIcon(ImagesMap.get("dot.gif"));
		c.add(sbt, BorderLayout.NORTH);

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		JPanel pn = new JPanel();
		pn.add(new JLabel("Recherche : "));
		pn.add(pattern);
		pn.add(ok);
		pn.add(stop);
		pn.add(cancel);
		JPanel ps = new JPanel();
		p.add(pn, BorderLayout.NORTH);
		p.add(new JScrollPane(lresults), BorderLayout.SOUTH);
		c.add(p, BorderLayout.CENTER);

		JToolBar sbb = new JToolBar();
		l = new JLabel();
		sbb.add(l);
		c.add(sbb, BorderLayout.SOUTH);

		pack();
		GU.center(this);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SearchModel model = new SearchModel("/home/sted");
		SearchGUI sg = new SearchGUI(model);
	}
}*/

