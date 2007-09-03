package org.woped.quantana.resourcealloc;

import java.awt.Color;
import java.util.ArrayList;

public class Resource {
	private String name;
	private ArrayList<String> roles = new ArrayList<String>();
	private ArrayList<String> groups = new ArrayList<String>();
	private double busyTime = 0.0;
	private double lastStartTime = 0.0;
	
	private Color color = Color.WHITE;
	
	public double getBusyTime() {
		return busyTime;
	}

	public void setBusyTime(double busy) {
		this.busyTime = busy;
	}

	public Resource(String name){
		this.name = name;
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

	public double getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(double lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void updStats(double time){
		busyTime += time - lastStartTime;
	}
	
	public void reset(){
		busyTime = 0;
		lastStartTime = 0;
	}
}
