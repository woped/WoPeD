package org.woped.quantana.resourcealloc;

//import java.util.ArrayList;
import java.util.HashMap;

public class AllocationTable {
	//ArrayList<AllocationTableItem> table = new ArrayList<AllocationTableItem>();
	HashMap<String, AllocationTableItem> table = new HashMap<String, AllocationTableItem>();

	public HashMap<String, AllocationTableItem> getTable() {
		return table;
	}

	public void setTable(HashMap<String, AllocationTableItem> table) {
		this.table = table;
	}

	/*public ArrayList<AllocationTableItem> getTable() {
		return table;
	}

	public void setTable(ArrayList<AllocationTableItem> table) {
		this.table = table;
	}*/
}
