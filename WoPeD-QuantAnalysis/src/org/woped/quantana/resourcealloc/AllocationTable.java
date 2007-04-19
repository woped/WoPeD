package org.woped.quantana.resourcealloc;

import java.util.ArrayList;

public class AllocationTable {
	ArrayList<AllocationTableItem> table = new ArrayList<AllocationTableItem>();

	public ArrayList<AllocationTableItem> getTable() {
		return table;
	}

	public void setTable(ArrayList<AllocationTableItem> table) {
		this.table = table;
	}
}
