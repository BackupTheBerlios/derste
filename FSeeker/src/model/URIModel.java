package model;

import java.util.Observable;
import java.util.Observer;

/**
 * Contient l'URI en cours de vue.
 * 
 * @author derosias
 */
public class URIModel extends Observable implements Observer {
    protected FSeekerModel fsm = null;
    
    public URIModel(FSeekerModel fsm) {
        this.fsm = fsm;
    }

    public FSeekerModel getModel() {
        return fsm;
    }
    
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }

}