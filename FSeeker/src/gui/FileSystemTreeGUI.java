/*
 * Created on 14 oct. 2004
 */
package gui;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import model.FileSystemTreeModel;
import model.URIModel;
import renderer.FileSystemTreeCellRenderer;
import controler.FileSystemTreeControler;

/**
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

        FileSystemTreeControler fstc = new FileSystemTreeControler(m);
        addTreeSelectionListener(fstc);

        /*
         * addTreeSelectionListener(new TreeSelectionListener() { public void
         * valueChanged(TreeSelectionEvent e) { File f = (File)
         * e.getPath().getLastPathComponent(); String s = f.getAbsolutePath();
         * 
         * setText("Path : " + s + "\nFichier : " +
         * e.getPath().getLastPathComponent().toString()
         *  + "\nTaille : " + ((File)
         * e.getPath().getLastPathComponent()).length() + " octets"); } });
         * JSplitPane sp = new JSplitPane(); JScrollPane scrollTop = new
         * JScrollPane(tree); JScrollPane scrollBottom = new JScrollPane(text);
         * sp.setTopComponent(scrollTop); sp.setBottomComponent(scrollBottom);
         * add(sp);
         */

    }

    public void update(Observable o, Object arg) {
        System.out.println("FileSystemTreeGUI.update() / " + o);
        if (o instanceof FileSystemTreeModel) {
            FileSystemTreeModel fstm = (FileSystemTreeModel) o;
            setDirectory(fstm.getCurrentDirectory());
        } else if (o instanceof URIModel) {
            URIModel urim = (URIModel) o;
            setDirectory(urim.getURI());
        }

    }
    
    public void setDirectory(File dir) {
        setSelectionPath(new TreePath(dir));
        expandPath(new TreePath(dir));
        
        System.out.println("FileSystemTreeGUI.setDirectory() / " + dir);
    }

}