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
    * Ce type d'évenement est appelé lorsque le modéle de la Jlist associé change
    * dans notre cas on change de répertoire ou autre, on met donc à jour le tout
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
     *  Cette méthode n'est pas implémentée, elle ne fait rien de particulier
     */
    public void intervalAdded(ListDataEvent e) {

    }

    /**
     *  Cette méthode n'est pas implémenté, elle ne fait rien de particulier
     */
    public void intervalRemoved(ListDataEvent e) {

    }

}