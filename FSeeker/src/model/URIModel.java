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
     * Créé un modèle de d'URI avec une location par défaut.
     * 
     * @param uri
     *            l'uri par défaut
     */
    public URIModel(File uri) {
        this.uri = uri;
    }

    /**
     * Modifie l'uri en cours, et prévient les observers.
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