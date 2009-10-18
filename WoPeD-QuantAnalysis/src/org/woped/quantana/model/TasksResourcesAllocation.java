package org.woped.quantana.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.woped.quantana.resourcealloc.Resource;

public class TasksResourcesAllocation {
	
	private HashMap<String, ArrayList<Resource>> table;
	
	public TasksResourcesAllocation(){
		table = new HashMap<String, ArrayList<Resource>>();
	}
	
	public void addTaskResourcesPair(String task, ArrayList<Resource> list){
		table.put(task, list);
	}
	
	public ArrayList<Resource> getResources(String task){
		return table.get(task);
	}
	
	public int getNumResources(String task){
		return table.get(task).size();
	}
	
	public Set<String> getTasks(){
		return table.keySet();
	}
}
