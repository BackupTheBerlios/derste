/*
 * Created on 20 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;

/**
 * Le supra-mod�le qui contient l'URI courante.
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
        // le setSelectionPath dans le JTree provoque 2 d�clenchages ici �
        // chaque s�lection autre que sur le JTree (la JList, etc)
        // C'est normal car � chaque changement de s�l, le supramodel change
        // donc le jtree setselectionpath, mais cette m�thode d�clenche l'�v�nement
        // valuechanged qui _refait_ un setURI sur la selection justement (donc la m�me)

        System.out.println("FSeekerModel.setURI(" + uri.getAbsolutePath() + ")");
        this.uri = uri;
        setChanged();
        notifyObservers(src);
    }

    public void setURI(File uri) {
        setURI(uri, null);
    }

}