package gui;


import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.ListImagesModel;
import renderer.ImagesListCellRenderer;
import controler.ListImagesDataControler;
import controler.ListImagesMouseListener;

/**
 * @author brahim
 */
public class ListImagesGUI extends JPanel {

    private ListImagesModel m = null;

    private JList list = null;

    public ListImagesGUI(ListImagesModel m) {
        this.m = m;
        list = new JList(m);
        
        new ListImagesDataControler(m, list);
        list.addMouseListener(new ListImagesMouseListener(this));
        list.setCellRenderer(new ImagesListCellRenderer());
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        
        JScrollPane sp = new JScrollPane(list);
        add(sp);

    }

    public JList getList() {
        return list;
    }

}

