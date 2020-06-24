package org.woped.metrics.builder;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonEditor implements TableCellRenderer  {
	  protected JButton button;


	  public ButtonEditor(JButton delete) {
	    button = delete;
	    button.setOpaque(true);
	  }


	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		    
		    return button;
	}


	}
