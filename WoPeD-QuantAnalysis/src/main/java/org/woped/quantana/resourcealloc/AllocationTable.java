package org.woped.quantana.resourcealloc;

import java.util.ArrayList;
import java.util.HashMap;

public class AllocationTable {
	HashMap<String, AllocationTableItem> table = new HashMap<String, AllocationTableItem>();

	public HashMap<String, AllocationTableItem> getTable() {
		return table;
	}

	public void setTable(HashMap<String, AllocationTableItem> table) {
		this.table = table;
	}

	public String[] getGroupRoles(String[] tasks){
		String[] sa = new String[tasks.length];
		String group = "";
		String role = "";
		for (int i = 0; i < tasks.length; i++){
			ArrayList<String> g = table.get(tasks[i]).getGroups();
			ArrayList<String> r = table.get(tasks[i]).getRoles();
			
			if (g.size() > 0) group = g.get(0);
			else group = "none";
			if (r.size() > 0) role = r.get(0);
			else role = "none";
			
			sa[i] = group + "/" + role;
		}
		
		return sa;
	}
}
