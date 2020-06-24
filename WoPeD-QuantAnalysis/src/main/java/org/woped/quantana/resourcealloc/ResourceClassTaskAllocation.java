package org.woped.quantana.resourcealloc;

import java.util.ArrayList;

public class ResourceClassTaskAllocation {
	String resClass = "";
	ArrayList<String> tasks = new ArrayList<String>();
	
	public ResourceClassTaskAllocation(String name){
		resClass = name;
	}

	public String getResClass() {
		return resClass;
	}

	public void setResClass(String resClass) {
		this.resClass = resClass;
	}

	public ArrayList<String> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<String> tasks) {
		this.tasks = tasks;
	}
}
