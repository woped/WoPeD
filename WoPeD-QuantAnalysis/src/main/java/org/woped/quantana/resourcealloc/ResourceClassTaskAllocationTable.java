package org.woped.quantana.resourcealloc;

import java.util.ArrayList;

public class ResourceClassTaskAllocationTable {
	ArrayList<ResourceClassTaskAllocation> table = new ArrayList<ResourceClassTaskAllocation>();

	public ArrayList<ResourceClassTaskAllocation> getTable() {
		return table;
	}

	public void setTable(ArrayList<ResourceClassTaskAllocation> table) {
		this.table = table;
	}
}
