/*
 * Created on 31 oct. 2004
 */
package controler;

import java.io.File;
import java.util.EventObject;

/**
 * @author sted
 */
public class URIChangedEvent extends EventObject {
	protected File uri = null;
	
	public URIChangedEvent(Object source, File uri) {
		super(source);
		this.uri = uri;
	}
	
	public File getURI() {
		return uri;
	}
}
