package gui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;

import model.ListImagesModel;
import renderer.ListImagesCellRenderer;
import controler.ListImagesDataControler;

/**
 * @author brahim
 * @author Sted
 */
public class ListImagesGUI extends JList implements Observer {

    private ListImagesModel m = null;

    // TODO le truc du false / true sera à virer quand le jdesktoppane sera ok
    public ListImagesGUI(ListImagesModel m) {
        this(m, false);
    }

    public ListImagesGUI(ListImagesModel m, boolean simple) {
        this.m = m;
        m.addObserver(this);

        setModel(m);
        setDragEnabled(true);
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setLayout(new FlowLayout());
        setCellRenderer(new ListImagesCellRenderer(m, simple));
        addMouseListener(new ListImagesDataControler(m));
        addMouseMotionListener(new ListImagesMouseMotionListener(this, m));
    }

    public void update(Observable o, Object caller) {
        if (caller != this) {
            revalidate();
            repaint();
        }
    }
}

class ListImagesMouseMotionListener extends MouseMotionAdapter {
    protected ListImagesModel lim = null;
    protected ListImagesGUI ligui = null;
    
    public ListImagesMouseMotionListener(ListImagesGUI ligui, ListImagesModel lim) {
        this.ligui = ligui;
        this.lim = lim;     
    }
    
    public void mouseMoved(MouseEvent e) {
        int index = ligui.locationToIndex(e.getPoint());
        File hovered = (File) lim.getElementAt(index);
        
        FileUtilities.getDetails(file);
    }
}

