/*
 * Created on 24 oct. 2004
 */
package misc;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author sted
 */
public class FileDetails {
	public static String getDetails(File f) {
		StringBuffer sb = new StringBuffer(50);
		sb.append("<html>");
		sb.append("<center><img src=\"images/dot.gif\"></center><br>");
		
		sb.append("<b>" + (f.isDirectory() ? "Doss" : "Fich") + "ier</b> : "+ f.getName() + "<br>");
		sb.append("<b>Taille</b> : " + f.length() + " octets<br>");
		
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
		Date d = new Date(f.lastModified());		
		sb.append("<b>Dernière modification</b> : " + dateFormat.format(d) + "<br>");

		File[] foo = f.listFiles();
		if (foo != null && foo.length > 0)
			sb.append("<b>Contient</b> : " + foo.length + " fichiers");
		
		sb.append("</html>");
		
		return sb.toString();
	}
}
