/*
 * Created on 6 nov. 2004
 */
package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JToolBar;

import model.FSeekerModel;

/**
 * @author sted
 */
public class StatusBarGUI extends JToolBar implements Observer {

	protected JLabel blabla = new JLabel("FSeeker " + FSeeker.VERSION);
	
	/**
	 * Renvoie la barre d'état par défaut.
	 * 	
	 * @return la barre d'état
	 */
	public StatusBarGUI(FSeeker f) {
		setFloatable(false);
		f.getModel().addObserver(this);		
		add(blabla);
	}

	public void update(Observable o, Object arg) {
		if (((FSeekerModel) o).isChanged(FSeekerModel.SELECTION))
			blabla.setText(((FSeekerModel) o).getSelection().toString());
	}
}