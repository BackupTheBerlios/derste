package gui;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import model.ListImagesModel;


/**
 * Created on 16 oct. 2004 
 * @author brahim
 *
 */
/**
 * @author brahim
 *
 */
class ImagesListDataListener implements ListDataListener {

    private ListImagesModel m;

    private JList list;

    public ImagesListDataListener(ListImagesModel m, JList list) {
        this.m = m;
        this.list = list;
        m.addListDataListener(this);
    }

   /**
    * @param 
    * 
    * Ce type d'�venement est appel� lorsque le mod�le de la Jlist associ� change
    * dans notre cas on change de r�pertoire ou autre, on met donc � jour le tout
    */
    public void contentsChanged(ListDataEvent e) {
        System.out.println("Changement");
        list.clearSelection();
        list.ensureIndexIsVisible(0);//utile ?
        list.setModel(m);
        list.revalidate();
        list.repaint();

    }

    /**
     *  Cette m�thode n'est pas impl�ment�e, elle ne fait rien de particulier
     */
    public void intervalAdded(ListDataEvent e) {

    }

    /**
     *  Cette m�thode n'est pas impl�ment�, elle ne fait rien de particulier
     */
    public void intervalRemoved(ListDataEvent e) {

    }

}