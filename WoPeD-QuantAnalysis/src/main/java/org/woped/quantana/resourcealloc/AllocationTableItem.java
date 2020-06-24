package org.woped.quantana.resourcealloc;

import java.util.ArrayList;

public class AllocationTableItem {
	String task = "";
	ArrayList<String> roles = new ArrayList<String>();
	ArrayList<String> groups = new ArrayList<String>();
	
	public AllocationTableItem(String t, ArrayList<String> r, ArrayList<String> g){
		task = t;
		roles = r;
		groups = g;
	}

	public ArrayList<String> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	public String toString(){
		return task + ": (" + roles + ", " + groups + ")";
	}
}
