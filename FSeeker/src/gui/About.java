/*
 * Created on 17 oct. 2004
 */
package gui;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Boîte de dialogue "A propos".
 * @author sted
 */
public class About extends JOptionPane {
	
	public About(Frame owner) {
		super();
		JDialog d = createDialog(owner, "Pwet");
		d.setVisible(true);
	    //super(owner, "A propos");
				
		/*final JOptionPane optionPane = new JOptionPane(
                "FSeeker v" + FSeeker.VERSION + " par Stéphane D. tout seul.");*/
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.add(new JLabel("CECI EST UN TEST"));
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new About(f).setVisible(true);
		
	}

}
