/*
 * Created on 20 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;

/**
 * @author derosias
 */
public class FSeekerModel extends Observable {
    protected File uri = null;
    
    public File getURI() {
        return uri;
    }
    
    public FSeekerModel() {
    }
    
    public void setURI(File uri) {
        this.uri = uri;
        setChanged();
        notifyObservers();
    }
   
}
