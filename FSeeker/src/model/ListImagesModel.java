/*
 * Created on 16 oct. 2004
 */
package model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * @author Sted
 * @author brahim
 */
public class ListImagesModel extends Observable implements ListModel, Observer {

    protected FSeekerModel fsm = null;

    protected File parent = null;

    public FSeekerModel getModel() {
        return fsm;
    }

    public File getParent() {
        return parent;
    }

    public void setParent(File parent) {
        this.parent = parent;
    }

    public void update(Observable o, Object caller) {
        // Le modèle peut s'auto changer, il est à la fois modèle et contrôleur
        // !
        setParent(getModel().getURI().getParentFile());
        setChanged();
        notifyObservers(caller);
    }

    public ListImagesModel(FSeekerModel fsm) {
        this.fsm = fsm;
        this.parent = fsm.getURI();
        fsm.addObserver(this);
    }

    /**
     * Renvoie le nombre de fichiers dans le dossier + 1 (parent).
     */
    public int getSize() {
        // Le null ne devrait jamais arriver, mais ça l'était avec un bug, donc
        // laissé..
        if (fsm.getURI().list() == null)
            return 1;
        return fsm.getURI().list().length + 1;
    }

    public Object getElementAt(int index) {
        if (index == 0) {
            if (fsm.getURI().getParentFile() == null)
                return fsm.getURI();
            return fsm.getURI().getParentFile();
        }
        File[] files = fsm.getURI().listFiles();
        return files[index - 1];
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }
}


///////////

class 
SortingListModel 
extends 
AbstractListModel
{
// Define a SortedSet
TreeSet model;

private static Comparator USEFUL_COMPARATOR 
    = new Comparator()
        {
            public int compare( Object o1, Object o2 )
            {
                String str1 = o1.toString();
                String str2 = o2.toString();
                Collator collator = Collator.getInstance();
                int result = collator.compare( str1, str2 );
                return result;
            }
        };

public SortingListModel()
{
    model = new TreeSet( USEFUL_COMPARATOR );
}

// ListModel methods
public int getSize()
{
    // Return the model size
    return model.size();
}

public Object getElementAt( int index )
{
    // Return the appropriate element
    return model.toArray()[index];
}

// Other methods
public void addElement( Object element )
{
    
    if( model.add( element ))
    {
        fireContentsChanged( this, 0, getSize() );
    }
}

public void addAll( Object elements[] )
{
    Collection c = Arrays.asList(elements);
    model.addAll(c);
    fireContentsChanged( this, 0, getSize() );
}

public void clear()
{
    model.clear();
    fireContentsChanged( this, 0, getSize() );
}

public boolean contains( Object element )
{
    return model.contains( element );
}

public Object firstElement()
{
    // Return the appropriate element
    return model.first();
}

public Iterator iterator()
{
    return model.iterator();
}

public Object lastElement()
{
    // Return the appropriate element
    return model.last();
}

public boolean removeElement( Object element )
{
    boolean removed = model.remove( element );
    if( removed )
    {
        fireContentsChanged( this, 0, getSize() );
    }
    return removed;
}
}
//End of SortingListModel