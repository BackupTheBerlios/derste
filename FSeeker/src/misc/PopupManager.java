/*
 * Created on 4 nov. 2004
 */
package misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileSystemView;

import misc.file.CompareByLastModified;
import misc.file.CompareByName;
import misc.file.CompareBySize;
import misc.file.CompareByType;
import misc.file.FileUtilities;
import model.FSeekerModel;

/**
 * Classe qui g�re les popups de FSeeker.
 * 
 * @author Sted
 */
public class PopupManager {

	/** Le fichier en cours de couper/copier */
	protected static File ccFile = null;

	/** C'est une copie ou un cut ? */
	protected static boolean cut = false;

	/** Les pr�f�rences ! */
	protected static Preferences pref = Preferences.userRoot();

	/** Cr�er un menu � cocher */
	public static JCheckBoxMenuItem createCheckboxMenuItem(String s,
			ActionListener al, boolean startState) {
		JCheckBoxMenuItem cb = new JCheckBoxMenuItem(s, startState);
		cb.addActionListener(al);
		return cb;
	}

	/** Cr�er un menu simple */
	public static JMenuItem createMenuItem(String s, ActionListener al) {
		JMenuItem jmi = new JMenuItem(s);
		jmi.addActionListener(al);
		return jmi;
	}

	/** Cr�er un menu radio */
	public static JRadioButtonMenuItem createRadioMenuItem(String s,
			ActionListener al, boolean startState, ButtonGroup bg) {
		JRadioButtonMenuItem radio = new JRadioButtonMenuItem(s, startState);
		radio.addActionListener(al);
		bg.add(radio);
		return radio;
	}

	/**
	 * Quand l'utilisateur clique droit, on affiche un popup avec diff�rentes
	 * commandes.
	 * 
	 * @param e
	 *            l'�v�nement associ�
	 * @param popup
	 *            le popup � afficher
	 */
	public static void showPopup(MouseEvent e, JPopupMenu popup) {
		if (popup != null)
			popup.show(e.getComponent(), e.getX(), e.getY());
	}

	/** Le fichier en s�lection */
	protected File f = null;

	/** Le supra-mod�le */
	protected FSeekerModel fsm = null;

	/**
	 * Construit un manager de popup avec f, la s�lection courante.
	 * 
	 * @param f
	 *            s�lection courante
	 * @param fsm
	 *            supra-mod�le
	 */
	public PopupManager(File f, FSeekerModel fsm) {
		this.f = f;
		this.fsm = fsm;
	}

	/** Retourne le menu de copie */
	public JMenuItem getCopy() {
		return createMenuItem("Copier", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cut = false;
				ccFile = f;
			}
		});
	}

	/** Retourne le menu de cr�ation de r�pertoire */
	public JMenuItem getCreateDirectory() {
		return createMenuItem("Cr�er un dossier", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileUtilities.createDirectory(f);
				fsm.update();
			}
		});
	}

	/** Retourne le menu de cr�ation de fichier */
	public JMenuItem getCreateFile() {
		return createMenuItem("Cr�er un fichier", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileUtilities.createFile(f);
				fsm.update();
			}
		});
	}

	/** Retourne le menu de cut */
	public JMenuItem getCut() {
		return createMenuItem("Couper", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cut = true;
				ccFile = f;
			}
		});
	}

	/** Retourne le menu de suppression */
	public JMenuItem getDelete() {
		return createMenuItem("Supprimer", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FileUtilities.delete(f))
					fsm.update();
			}
		});
	}

	/** Retourne le menu d'affichage */
	public JMenu getDisplay() {
		JMenu menu = new JMenu("Affichage");
		menu.add(createCheckboxMenuItem("Afficher les fichiers cach�s",
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						pref.putBoolean("showHidden", !fsm.showHidden());
						fsm.showHidden(!fsm.showHidden());
					}
				}, fsm.showHidden()));
		menu.add(createCheckboxMenuItem("Activer les bulles d'infos",
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						pref.putBoolean("tooltips", !ToolTipManager
								.sharedInstance().isEnabled());
						ToolTipManager.sharedInstance().setEnabled(
								!ToolTipManager.sharedInstance().isEnabled());
					}
				}, ToolTipManager.sharedInstance().isEnabled()));
		menu.addSeparator();

		JMenu sortBy = new JMenu("Trier par");

		ButtonGroup bg = new ButtonGroup();
		sortBy.add(createRadioMenuItem("Nom", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pref.put("comparator", "name");
				fsm.setComparator(CompareByName.get());
			}
		}, fsm.getComparator() == CompareByName.get(), bg));
		sortBy.add(createRadioMenuItem("Type", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pref.put("comparator", "type");
				fsm.setComparator(CompareByType.get());
			}
		}, fsm.getComparator() == CompareByType.get(), bg));
		sortBy.add(createRadioMenuItem("Taille", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pref.put("comparator", "size");
				fsm.setComparator(CompareBySize.get());
			}
		}, fsm.getComparator() == CompareBySize.get(), bg));
		sortBy.add(createRadioMenuItem("Date de derni�re modification",
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						pref.put("comparator", "lastmodified");
						fsm.setComparator(CompareByLastModified.get());
					}
				}, fsm.getComparator() == CompareByLastModified.get(), bg));

		menu.add(sortBy);

		return menu;
	}

	/** Retourne le menu avec le nom du fichier en s�lection gris� */
	public JMenuItem getFileName() {
		JMenuItem jit = createMenuItem(FileSystemView.getFileSystemView()
				.getSystemDisplayName(f), null);
		jit.setEnabled(false);
		return jit;
	}

	/** Retourne le menu ouvrir */
	public JMenuItem getOpen() {
		return createMenuItem("Ouvrir", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (f.isDirectory())
					fsm.setURI(f);
				else
					FileUtilities.openFile(f);
			}
		});
	}

	/** Retourne le menu coller */
	public JMenuItem getPaste() {
		JMenuItem foo = createMenuItem("Coller", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File cc = null;

				// On veut le r�pertoire courant, pas celui s�lectionn�.
				// Cf. gestion du clic droit dans les s�lections (f = fichier
				// s�lectionn�, on prend donc son parent), et hors s�lection (on
				// est dans la partie o� y'a aucun �l�ment, l'appelant prend pas
				// d�faut l'uri courante, donc on ne la modifie pas.

				if (!f.equals(fsm.getURI()))
					f = f.getParentFile();

				if ((cc = FileUtilities.copy(ccFile, f)) != null) {
					if (cut) {
						FileUtilities.delete(ccFile, true);
						cut = false;
						ccFile = null;
					}
					// En copie, on ne met pas la source � null pour faire de
					// multiple copie

					fsm.setSelection(cc);
				}
			}
		});

		if (ccFile == null)
			foo.setEnabled(false);

		return foo;
	}

	/** Retourne le menu des propri�t�s */
	public JMenuItem getProperties() {
		return createMenuItem("Propri�t�s", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileUtilities.showProperties(f);
			}
		});
	}

	/** Retourne le menu du rafra�chissement */
	public JMenuItem getRefresh() {
		return createMenuItem("Rafra�chir", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fsm.update();
			}
		});
	}

	/** Retourne le menu de renommage */
	public JMenuItem getRename() {
		return createMenuItem("Renommer", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File newf = null;
				if ((newf = FileUtilities.rename(f)) != null)
					fsm.setSelection(newf);
			}
		});
	}

}