package controler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import model.ListImagesModel;

/**
 * @author brahim
 */
public class ListImagesDataControler extends MouseAdapter {

    private ListImagesModel m = null;;

    public ListImagesDataControler(ListImagesModel m) {
        this.m = m;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            File f = (File) ((JList) e.getSource()).getSelectedValue();
            if (f.isDirectory() && f.canRead())
                m.getModel().setURI(f, e.getSource());
        }
    }

}