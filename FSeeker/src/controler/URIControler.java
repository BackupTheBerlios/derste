/*
 * Created on 21 oct. 2004
 */
package controler;

import gui.URIGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import model.URIModel;

/**
 * @author sted
 */
public class URIControler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        URIGUI ugui = (URIGUI) e.getSource();
        String f = ugui.getText();
        URIModel m = ugui.getModel();
        m.setURI(new File(f));
    }

}