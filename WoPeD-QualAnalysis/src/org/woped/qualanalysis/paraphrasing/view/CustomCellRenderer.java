package org.woped.qualanalysis.paraphrasing.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class CustomCellRenderer extends JTextArea implements TableCellRenderer {
	

	private static final long serialVersionUID = 1L;
    public CustomCellRenderer() {
    	setLineWrap(true);
		setWrapStyleWord(true);
		setMargin(new Insets(0, 5, 0, 5));
   }

    
  public Component getTableCellRendererComponent(JTable table, Object
          value, boolean isSelected, boolean hasFocus, int row, int column) {
	  setText((String)value);
	  
      setSize(table.getColumnModel().getColumn(column).getWidth(),
              getPreferredSize().height);
      if (table.getRowHeight(row) != getPreferredSize().height) {
              table.setRowHeight(row, getPreferredSize().height);
      }
            
      if (isSelected) {
          setBorder(BorderFactory.createLineBorder(Color.BLACK));
          setBackground(Color.YELLOW);
       }
      else {
          setBorder(BorderFactory.createLineBorder(table.getBackground()));
          setBackground(Color.WHITE);
       }

      this.setToolTipText((String) table.getValueAt(row, 0));
      
      return this;
  }
  
}