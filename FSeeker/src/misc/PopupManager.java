/*
 * Created on 4 nov. 2004
 */
package misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

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
 * @author sted
 */
public class PopupManager {
    public static JMenuItem createMenuItem(String s, ActionListener al) {
        JMenuItem jmi = new JMenuItem(s);
        jmi.addActionListener(al);
        return jmi;
    }

    public static JCheckBoxMenuItem createCheckboxMenuItem(String s,
            ActionListener al, boolean startState) {
        JCheckBoxMenuItem cb = new JCheckBoxMenuItem(s, startState);
        cb.addActionListener(al);
        return cb;
    }

    public static JRadioButtonMenuItem createRadioMenuItem(String s,
            ActionListener al, boolean startState, ButtonGroup bg) {
        JRadioButtonMenuItem radio = new JRadioButtonMenuItem(s, startState);
        radio.addActionListener(al);
        bg.add(radio);
        return radio;
    }

    protected File f = null;

    protected static File ccFile = null;

    protected static boolean cut = false;

    protected FSeekerModel fsm = null;

    public PopupManager(File f, FSeekerModel fsm) {
        this.f = f;
        this.fsm = fsm;
    }

    public JMenuItem getCreateDirectory() {
        return createMenuItem("Créer un dossier", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileUtilities.createDirectory(f);
                fsm.update();
            }
        });
    }

    public JMenuItem getCreateFile() {
        return createMenuItem("Créer un fichier", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileUtilities.createFile(f);
                fsm.update();
            }
        });
    }

    public JMenuItem getProperties() {
        return createMenuItem("Propriétés", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileUtilities.showProperties(f);
            }
        });
    }

    public JMenuItem getFileName() {
        JMenuItem jit = createMenuItem(FileSystemView.getFileSystemView()
                .getSystemDisplayName(f), null);
        jit.setEnabled(false);
        return jit;
    }

    public JMenu getDisplay() {
        JMenu menu = new JMenu("Affichage");
        menu.add(createCheckboxMenuItem("Afficher les fichiers cachés",
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fsm.showHidden(!fsm.showHidden());
                    }
                }, fsm.showHidden()));
        menu.add(createCheckboxMenuItem("Activer les bulles d'infos",
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ToolTipManager.sharedInstance().setEnabled(
                                !ToolTipManager.sharedInstance().isEnabled());
                    }
                }, ToolTipManager.sharedInstance().isEnabled()));
        menu.addSeparator();

        JMenu sortBy = new JMenu("Trier par");

        ButtonGroup bg = new ButtonGroup();
        sortBy.add(createRadioMenuItem("Nom", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fsm.setComparator(CompareByName.get());
            }
        }, fsm.getComparator() == CompareByName.get(), bg));
        sortBy.add(createRadioMenuItem("Type", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fsm.setComparator(CompareByType.get());
            }
        }, fsm.getComparator() == CompareByType.get(), bg));
        sortBy.add(createRadioMenuItem("Taille", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fsm.setComparator(CompareBySize.get());
            }
        }, fsm.getComparator() == CompareBySize.get(), bg));
        sortBy.add(createRadioMenuItem("Date de dernière modification",
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fsm.setComparator(CompareByLastModified.get());
                    }
                }, fsm.getComparator() == CompareByLastModified.get(), bg));

        menu.add(sortBy);

        return menu;
    }

    public JMenuItem getCut() {
        return createMenuItem("Couper", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cut = true;
                ccFile = f;
            }
        });
    }

    public JMenuItem getCopy() {
        return createMenuItem("Copier", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cut = false;
                ccFile = f;
            }
        });
    }

    public JMenuItem getPaste() {
        JMenuItem foo = createMenuItem("Coller", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File cc = null;

                // On veut le répertoire courant, pas celui sélectionné.
                // Cf. gestion du clic droit dans les sélections (f = fichier
                // sélectionné, on prend donc son parent), et hors sélection (on
                // est dans la partie où y'a aucun élément, l'appelant prend pas
                // défaut l'uri courante, donc on ne la modifie pas.

                if (!f.equals(fsm.getURI()))
                    f = f.getParentFile();

                if ((cc = FileUtilities.copy(ccFile, f)) != null) {
                    if (cut) {
                        FileUtilities.delete(ccFile, true);
                        cut = false;
                        ccFile = null;
                    }
                    // En copie, on ne met pas la source à null pour faire de
                    // multiple copie

                    fsm.setSelection(cc);
                }
            }
        });

        if (ccFile == null)
            foo.setEnabled(false);

        return foo;
    }

    public JMenuItem getDelete() {
        return createMenuItem("Supprimer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (FileUtilities.delete(f))
                    fsm.update();
            }
        });
    }

    public JMenuItem getRename() {
        return createMenuItem("Renommer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File newf = null;
                if ((newf = FileUtilities.rename(f)) != null)
                    fsm.setSelection(newf);
            }
        });
    }

    public JMenuItem getRefresh() {
        return createMenuItem("Rafraîchir", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fsm.update();
            }
        });
    }

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

    /**
     * Quand l'utilisateur clique droit, on affiche un popup avec différentes
     * commandes.
     * 
     * @param e
     *            l'événement associé
     * @param popup
     *            le popup à afficher
     */
    public static void showPopup(MouseEvent e, JPopupMenu popup) {
        if (popup != null)
            popup.show(e.getComponent(), e.getX(), e.getY());
    }

}