/*
 * Created on 17 oct. 2004
 */
package gui;

import java.awt.Frame;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import misc.ImagesMap;

/**
 * Bo�te de dialogue "A propos".
 * 
 * @author sted
 */
public class About {
	public About(Frame owner) {
		Icon fseeker = ImagesMap.getLogo();
		JOptionPane.showMessageDialog(owner, "FSeeker a �t� r�alis� par St�phane D. tout seul, car il vaut bien", "A propos", JOptionPane.INFORMATION_MESSAGE, fseeker);
	}
}
