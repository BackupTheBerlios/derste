package gui;

import java.awt.FlowLayout;

import javax.swing.JList;

import model.ListImagesModel;
import renderer.ListImagesCellRenderer;
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
        setLayout(new FlowLayout());
        setCellRenderer(new ListImagesCellRenderer());

        new ListImagesDataControler(m);
        addMouseListener(new ListImagesMouseListener());
    }
}

