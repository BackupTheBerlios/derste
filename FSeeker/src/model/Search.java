/*
 * Créé le 13 oct. 2004
 */

package model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Effectue une recherche à partir d'une racine d'arborescence.
 * 
 * @author derosias
 */
public class Search {

	private final static int MAX_THREADS = 5;

	protected FileSystemModel root = null;

	protected String pattern = null;

	protected List results = new ArrayList();

	protected SearchGUI gui = null;

	protected ThreadGroup searchThreads = new ThreadGroup("search");

	private boolean continueSearch = true;

	public Search(SearchGUI gui, String root, String pattern) {
		this.root = new FileSystemModel(root);
		this.gui = gui;

		// On échappe tous les caractères foireux
		String pfinal = "";
		for (int i = 0; i < pattern.length(); i++) {
			if (pattern.charAt(i) == '*')
				pfinal += ".*";
			else if (!Character.isLetterOrDigit(pattern.charAt(i)))
				pfinal += "\\" + pattern.charAt(i);
			else
				pfinal += pattern.charAt(i);
		}

		// PAN, on a notre pattern échappé
		this.pattern = pfinal;

	}

	protected LinkedList threadsInWaiting = new LinkedList();

	public void search() {
		SearchThread mainThread = new SearchThread(searchThreads, root);
		continueSearch = true;
		mainThread.start();

		while (searchThreads.activeCount() > 0)
			Thread.yield();
	}

	public void stop() {
		//System.out.println("stop");
		continueSearch = false;
	}

	class SearchThread extends Thread {

		protected FileSystemModel root = null;

		public SearchThread(ThreadGroup tg, FileSystemModel root) {
			super(tg, root.getRoot().toString());
			this.root = root;
		}

		/**
		 * Méthode principale effectuant la recherche.
		 */
		public void run() {
			gui.setSearchPath(root.getRoot().toString());

			int nbChildren = root.getChildCount(root.getRoot());

			for (int i = 0; i < nbChildren; i++) {

				if (!continueSearch)
					return;

				File f = ((File) root.getChild(root.getRoot(), i));

				// Attention, ca peut arriver (et c'est déjà arrivé..!)
				if (f == null)
					continue;

				// On a un _fichier_ ou on a un _répertoire_
				// TODO lien symbolique, ne pas y aller :\\
				if (f.isFile()) {

					if (f.getName().matches(".*" + pattern + ".*"))
						addResult(f.getAbsolutePath());

				} else {

					String s = f.getAbsolutePath();
					SearchThread t = new SearchThread(searchThreads,
							new FileSystemModel(s));

					// Si on a déjà MAX threads actifs, on le mets en file
					// d'attente
					if (searchThreads.activeCount() > MAX_THREADS) {
						System.out.println("attente : " + t);
						threadsInWaiting.addFirst(t);
					} else {
						System.out.println("lancement : " + t);
						t.start();
					}
				}
			}

			System.out.println("fin : " + Thread.currentThread());

			// On active le prochain dans la file
			if (!threadsInWaiting.isEmpty()) {
				Thread t = (Thread) threadsInWaiting.removeLast();
				System.out.println("sortie de file : " + t);
				t.start();
			}
		}
	}

	protected void addResult(final Object o) {
		gui.addResult(o.toString());
		//System.out.println("Ajout : " + o);
		results.add(o);
	}

	public Object[] getResults() {
		return results.toArray();
	}

	public String toString() {
		return results.toString();
	}

}