package gui;


import javax.swing.JList;

import model.ListImagesModel;
import renderer.ImagesListCellRenderer;
import controler.ListImagesDataControler;
import controler.ListImagesMouseListener;

/**
 * @author brahim
 */
public class ListImagesGUI extends JList {

    private ListImagesModel m = null;

    public ListImagesGUI(ListImagesModel m) {
        this.m = m;
        setModel(m);
        
        setDragEnabled(true);
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setCellRenderer(new ImagesListCellRenderer());
        
        new ListImagesDataControler(m, this);
        addMouseListener(new ListImagesMouseListener(this));
    }
}

