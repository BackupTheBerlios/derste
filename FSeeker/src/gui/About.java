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
		Icon fseeker = ImagesMap.get(ImagesMap.FSEEKER_LOGO);
		JOptionPane.showMessageDialog(owner, "<html><h2>FSeeker v" + FSeeker.VERSION + " </h2><br><hr><br>Réalisé par <ul><li>Stéphane D. <img src=\"moi.jpg\"></li><li>Brahim A. E. <img src=\"lui.jpg\"></li></ul></html>", "A propos", JOptionPane.INFORMATION_MESSAGE, fseeker);
	}
}
