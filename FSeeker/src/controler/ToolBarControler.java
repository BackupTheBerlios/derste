/*
 * Created on 19 oct. 2004
 */
package controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import misc.GU;

/**
 * @author Sted
 */
public class ToolBarControler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("PREVIOUS"))
            GU.message("Précédent !");
    }
}
