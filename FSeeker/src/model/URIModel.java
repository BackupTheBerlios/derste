package model;

import java.io.File;
import java.util.Observable;

/**
 * Contient l'URI en cours de vue.
 * 
 * @author derosias
 */
public class URIModel extends Observable {
    /** L'URI en cours */
    protected File uri = null;

    /**
     * Cr�� un mod�le de d'URI avec une location par d�faut.
     * 
     * @param uri
     *            l'uri par d�faut
     */
    public URIModel(File uri) {
        this.uri = uri;
    }

    /**
     * Modifie l'uri en cours, et pr�vient les observers.
     * 
     * @param uri
     *            la nouvelle uri
     */
    public void setURI(File uri) {
        this.uri = uri;
        setChanged();
        notifyObservers();

    }

    /**
     * Retourne l'uri en cours.
     * 
     * @return l'uri en cours
     */
    public File getURI() {
        return uri;
    }

}