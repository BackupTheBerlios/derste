/*
 * Created on 20 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;

/**
 * Le supra-modèle qui contient l'URI courante.
 * 
 * @author Sted
 */
public class FSeekerModel extends Observable {
    protected File uri = null;
    
    public File getURI() {
        return uri;
    }
    
    public FSeekerModel(File uri) {
        this.uri = uri;
    }
    
    public void setURI(File uri) {
        System.out.println("FSeekerModel.setURI(" + uri + ")");
        this.uri = uri;
        setChanged();
        notifyObservers();
    }
   
}
