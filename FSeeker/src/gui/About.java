/*
 * Created on 17 oct. 2004
 */
package gui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Boîte de dialogue "A propos".
 * @author sted
 */
public class About extends JDialog {

	private JButton close = new JButton("Fermer");
	{
		
	}
	public About(Frame owner) {
		super(owner, "A propos", true);
		setLayout(new FlowLayout());
		getContentPane().add(close);
		
		/*JOptionPane.showMessageDialog(frame,
	    	"Eggs aren't supposed to be green.");*/
		
		final JOptionPane optionPane = new JOptionPane(
                "FSeeker v" + FSeeker.VERSION + " par Stéphane D. tout seul.");
		setContentPane(optionPane);
		pack();
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
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
