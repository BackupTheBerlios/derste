package gui;

import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;

import model.ListImagesModel;
import renderer.ListImagesCellRenderer;
import controler.ListImagesDataControler;
import controler.ListImagesMouseListener;

/**
 * @author brahim
 * @author Sted
 */
public class ListImagesGUI extends JList implements Observer {

    private ListImagesModel m = null;

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
        setCellRenderer(new ListImagesCellRenderer(simple));

        new ListImagesDataControler(m, this);
        addMouseListener(new ListImagesMouseListener());
    }
    
	public void update(Observable o, Object arg) {
		System.out.println("ListImagesGUI.update()");
	}
}

