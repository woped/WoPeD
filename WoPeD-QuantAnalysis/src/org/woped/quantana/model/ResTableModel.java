package org.woped.quantana.model;

import javax.swing.table.AbstractTableModel;

public class ResTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID	= 23L;
	
	private String[] columnNames;
	private Object[][] data;
	
	public ResTableModel(String[] cols, Object[][] obj){
		columnNames = cols;
		data = obj;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
