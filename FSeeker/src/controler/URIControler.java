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
    protected URIModel m = null;
    
    public URIControler(URIModel m) {
        this.m = m;
    }
    
    public void actionPerformed(ActionEvent e) {
        URIGUI ugui = (URIGUI) e.getSource();
        String f = ugui.getText();
        m.setURI(new File(f));
    }

}