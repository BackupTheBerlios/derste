/*
 * Created on 4 nov. 2004
 */
package model;

import java.io.File;
import java.util.EventObject;

/**
 * @author sted
 */
public class SelectionChangedEvent extends EventObject {

	protected File selection = null;

	/**
	 * @param source
	 */
	public SelectionChangedEvent(Object source, File selection) {
		super(source);
		this.selection = selection;
	}

	public File getSelection() {
		return selection;
	}

}