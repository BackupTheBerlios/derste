/*
 * Created on 17 oct. 2004
 */
package gui;

import java.awt.Frame;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import misc.ImagesMap;

/**
 * Boîte de dialogue "A propos".
 * 
 * @author sted
 */
public class About {
	public About(Frame owner) {
		Icon fseeker = ImagesMap.getLogo();
		JOptionPane.showMessageDialog(owner, "FSeeker a été réalisé par Stéphane D. tout seul, car il vaut bien", "A propos", JOptionPane.INFORMATION_MESSAGE, fseeker);
	}
}
