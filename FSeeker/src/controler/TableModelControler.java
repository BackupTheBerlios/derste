/*
 * Created on 27 oct. 2004
 *
 *
 */
package controler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JTable;

import model.*;

/**
 * @author brahim
 *  
 */
public class TableModelControler extends MouseAdapter {
    private FileTableModel m = null;;

    public TableModelControler(FileTableModel m) {
        this.m = m;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            File f = null;
            JTable source = (JTable) e.getSource();
            Object value = source.getValueAt(source.getSelectedRow(), source
                    .getSelectedColumn());
            if (value instanceof File)
                f = (File) value;
            if (f != null) {
                if (f.isDirectory() && f.canRead())
                    m.getModel().setURI(f);
            }

        }
    }

    public static void main(String[] args) {
    }
}