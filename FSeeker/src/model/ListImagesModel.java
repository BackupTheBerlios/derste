/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import misc.file.CompareByType;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

    /** Le supra-mod�le */
    protected FSeekerModel fsm = null;

    /** Le comparateur utilis� */
    private Comparator currentComparator = CompareByType.get();

    /** L'ensemble tri� contenant les fichiers du r�pertoire courant */
    protected Set liste = new TreeSet(currentComparator);

    /**
     * Construit un mod�le de liste de fichiers.
     * 
     * @param fsm
     *            le supra-mod�le
     */
    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        fsm.addObserver(this);
        refreshListFiles();
    }

    /**
     * Retourne le supra-mod�le.
     * 
     * @return supra-mod�le
     */
    public FSeekerModel getModel() {
        return fsm;
    }

    public void update(Observable o, Object caller) {
        // Le mod�le peut s'auto changer, il est � la fois mod�le et contr�leur
        // !
        refreshListFiles();
        setChanged();
        notifyObservers(caller);
    }

    /**
     * Met � jour l'attribut <code>liste</code> (contenant les noms de
     * fichiers du r�pertoire courant).
     */
    protected void refreshListFiles() {
        liste.clear();
        File[] files = fsm.getURI().listFiles();
        if (files != null)
            for (int i = 0; i < files.length; i++)
            	if (!files[i].isHidden() || getModel().showHidden())
            		liste.add(files[i]);
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier courant.
     * 
     * @return nombre de fichiers dans le dossier courant
     */
    public int getSize() {
        return liste.size();
    }

    /**
     * Renvoie l'�l�ment � l'index <code>index</code> dans la liste.
     * 
     * @param index l'index de l'objet d�sir�
     * @return l'objet � l'index d�sir�
     */
    public Object getElementAt(int index) {
        return liste.toArray()[index];
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }

}