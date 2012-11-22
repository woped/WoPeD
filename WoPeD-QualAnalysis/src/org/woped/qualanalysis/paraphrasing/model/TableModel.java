package org.woped.qualanalysis.paraphrasing.model;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

	
	private static final long serialVersionUID = 1L;


	Object[][] data = null;
	
	
	String[] columnName = {"Id", "Text"};
	
	public TableModel(Object[][] data){
		super();
		this.data = data;
	}
	
	public String[] getColumnName(){
		return this.columnName;
	}
	
	public int getRowCount(){
		return this.data.length;
	}
	
	public boolean isCellEditable(int x, int y){
		return true;
	}
	
	public Object getValueAt(int x, int y){
		return this.data[x][y];
	}
	
	public void setValueAt(Object obj, int x, int y){
		data[x][y] = obj;
		fireTableDataChanged();
	}
	
	public Class<? extends Object> getColumnClass(int num){
		Object obj = getValueAt(0,num);
		return obj.getClass();
	}

	public int getColumnCount() {
		return this.columnName.length;
	}
	

	
}
