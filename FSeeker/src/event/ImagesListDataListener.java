package event;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import model.ListImagesModel;

/**
 * @author brahim
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImagesListDataListener implements ListDataListener {

	private ListImagesModel m;
	private JList list;
	public ImagesListDataListener(ListImagesModel m, JList list){
		this.m = m;
		this.list = list;
		m.addListDataListener(this);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
	 */
	public void contentsChanged(ListDataEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Changement");		
		list.clearSelection();
		list.ensureIndexIsVisible(0);//utile ?
		list.setModel(m);
		list.revalidate();
		list.repaint();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
	 */
	public void intervalAdded(ListDataEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
	 */
	public void intervalRemoved(ListDataEvent e) {
		// TODO Auto-generated method stub

	}

}
