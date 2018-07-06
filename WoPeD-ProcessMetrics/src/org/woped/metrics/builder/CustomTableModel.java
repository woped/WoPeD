package org.woped.metrics.builder;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8152725519048713038L;
	private boolean editable = true;
	
	public CustomTableModel(Vector<?> object, Vector<String> columns) {
		super();
	}

	@Override
	public boolean isCellEditable(int x, int y){
		return editable;
	}
	
	public void setEditable(boolean editable){
		this.editable = editable;
	}
	
}
