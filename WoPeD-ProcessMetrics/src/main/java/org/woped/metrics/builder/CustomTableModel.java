package org.woped.metrics.builder;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8152725519048713038L;
	private boolean editable = true;
	
	public CustomTableModel(Vector<? extends Vector> data, Vector<String> columns) {
		super(data, columns);
	}

	@Override
	public boolean isCellEditable(int x, int y){
		return editable;
	}
	
	public void setEditable(boolean editable){
		this.editable = editable;
	}
	
}
