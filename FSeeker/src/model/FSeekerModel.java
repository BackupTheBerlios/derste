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

    public void setURI(File uri, Object src) {
        // TODO
        // le setSelectionPath dans le JTree provoque 2 déclenchages ici à
        // chaque sélection autre que sur le JTree (la JList, etc)
        // C'est normal car à chaque changement de sél, le supramodel change
        // donc le jtree setselectionpath, mais cette méthode déclenche l'événement
        // valuechanged qui _refait_ un setURI sur la selection justement (donc la même)

        System.out.println("FSeekerModel.setURI(" + uri.getAbsolutePath() + ")");
        this.uri = uri;
        setChanged();
        notifyObservers(src);
    }

    public void setURI(File uri) {
        setURI(uri, null);
    }

}