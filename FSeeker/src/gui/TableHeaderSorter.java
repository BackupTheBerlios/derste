/*
 * Created on 10 nov. 2004
 *
 *
 */
package gui;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import misc.file.CompareByLastModified;
import misc.file.CompareByName;
import misc.file.CompareBySize;
import misc.file.CompareByType;
import model.FSeekerModel;

/**
 * @author brahim
 *
 */
public class TableHeaderSorter extends MouseAdapter{
    
    /** Le Supra-modéle */
    FSeekerModel fsm;
    /** La table */
    JTable table;
    
    public TableHeaderSorter(JTable table, FSeekerModel fsm){
        this.fsm = fsm;
        this.table = table;
        JTableHeader header = table.getTableHeader();
        header.addMouseListener(this);
    }
    
        public void mouseClicked(MouseEvent e) {
            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            if (column != -1) {
                switch(column){
                case 0:
                    fsm.setComparator(CompareByName.get());
                    break;
                case 1:
                    fsm.setComparator(CompareBySize.get());
                    
                    break;
                case 2:
                    fsm.setComparator(CompareByType.get());
                    break;
                case 3:
                    fsm.setComparator(CompareByLastModified.get());
                    break;
                    
                }   
            }
        }
    
}
