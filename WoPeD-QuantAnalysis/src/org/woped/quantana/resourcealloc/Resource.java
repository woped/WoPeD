package org.woped.quantana.resourcealloc;

import java.util.ArrayList;

public class Resource {
	private String name;
	//private String role = "none";
	//private String group = "none";
	private ArrayList<String> roles = new ArrayList<String>();
	private ArrayList<String> groups = new ArrayList<String>();
	private double busyTime = 0;
	
	public double getBusyTime() {
		return busyTime;
	}

	public void setBusyTime(double busy) {
		this.busyTime = busy;
	}

	public Resource(String name){
		this.name = name;
		roles.add("none");
		groups.add("none");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	
}
