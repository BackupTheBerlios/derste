/*
 * Created on 14 oct. 2004
 */
package gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.ToolTipManager;

import misc.FileSystemTree;
import misc.file.FileUtilities;
import model.FileSystemTreeModel;
import renderer.FileSystemTreeCellRenderer;
import controler.FileSystemTreeControler;

/**
 * @author Sted
 * @author brahim
 */
public class FileSystemTreeGUI extends JTree implements Observer {
    protected FileSystemTreeModel m = null;

    public FileSystemTreeGUI(FileSystemTreeModel m) {
        this.m = m;
        m.addObserver(this);

        setModel(m);
        setEditable(true);
        setCellRenderer(new FileSystemTreeCellRenderer());
        
        // Par défaut, le JTree ne s'enregistre pas
        ToolTipManager.sharedInstance().registerComponent(this);

        FileSystemTreeControler fstc = new FileSystemTreeControler(m);
        addTreeSelectionListener(fstc);
        addKeyListener(fstc);
    }

    public void update(Observable o, Object caller) {
        if (caller != this)
            setDirectory(m.getModel().getURI());
    }

    public void setDirectory(File dir) {
        setSelectionPath(FileSystemTree.getTreePath(dir));
    }

    /**
     * Affiche le tooltip évalué dynamiquement. Appelée automatiquement par
     * Swing.
     */
    public String getToolTipText(MouseEvent event) {
        Point clic = event.getPoint();
        if (getRowForLocation(clic.x, clic.y) != -1) {
            File f = (File) getPathForLocation(clic.x, clic.y)
                    .getLastPathComponent();
            return FileUtilities.getDetails(f);
        }
        return null;
    }

}