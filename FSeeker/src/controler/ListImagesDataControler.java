package controler;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import model.ListImagesModel;

/**
 * @author brahim
 */
public class ListImagesDataControler implements ListDataListener {

	private ListImagesModel m;
	
	public ListImagesDataControler(ListImagesModel m){
		this.m = m;
		m.addListDataListener(this);
	}

	public void contentsChanged(ListDataEvent e) {
		JList list = (JList) e.getSource();
		list.clearSelection();
		list.ensureIndexIsVisible(0);
		list.setModel(m);
		list.revalidate();
	}

	public void intervalAdded(ListDataEvent e) {

	}

	public void intervalRemoved(ListDataEvent e) {

	}

}
