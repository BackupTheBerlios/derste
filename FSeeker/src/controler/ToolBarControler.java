/*
 * Created on 19 oct. 2004
 */
package controler;

import gui.FSeeker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import misc.GU;
import model.FSeekerModel;

/**
 * @author Sted
 */
public class ToolBarControler implements ActionListener {
    protected FSeeker f = null;
    protected FSeekerModel fsm = null;

    public ToolBarControler(FSeeker f, FSeekerModel fsm) {
        this.f = f;
        this.fsm = fsm;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("PREVIOUS")) {
        
            GU.message("Précédent !");
        
        } else if (e.getActionCommand().equals("PARENT")) {
            
            fsm.gotoParent();
            
        } else if (e.getActionCommand().equals("SEARCHVIEW")) {
            
            JPanel p = new JPanel(new BorderLayout());

            JPanel psearch = new JPanel();
            psearch.add(new JLabel("Recherche"));
            psearch.add(new JTextField(20));

            p.add(psearch, BorderLayout.WEST);
            p.add(new JTextArea(20, 20), BorderLayout.EAST);
            f.setView(p);

        } else if (e.getActionCommand().equals("MACOSVIEW")) {

            f.setView(new JPanel());
            
        } else if (e.getActionCommand().equals("TREEVIEW")) {
            
            f.setView(f.getDefaultView());
            
        }
    }
}