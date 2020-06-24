package org.woped.quantana.model;

import javax.swing.table.AbstractTableModel;

public class ServerTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID	= 20L;
	
	private String[] columnNames;
	private Object[][] data;
	
	public ServerTableModel(String[] cols, Object[][] obj){
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
//		return false;
		return col == (columnNames.length - 1);
	}

	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
