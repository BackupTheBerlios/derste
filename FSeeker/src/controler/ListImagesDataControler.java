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
	private JList list;
	public ListImagesDataControler(ListImagesModel m, JList list){
		this.m = m;
		this.list = list;
		m.addListDataListener(this);
	}

	public void contentsChanged(ListDataEvent e) {
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
