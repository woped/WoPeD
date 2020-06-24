package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Set;

/**
 * The logical model created in the background - this model is the basis for the work of the Layouting Algorithm
 * @author Jonathan Peuker, Frederick Rouviere, Johannes Grenzemann, Cosima Juenger, Daniel Becea
 * @version 1.0
 */

// The five elements of the logical model: Compound Role, Compound Group, Role, Group & Component
			  
//Representation of the Compound Role
class CompoundRole {
	String name = null;
	Set<Role> children=null;
	boolean isSuperSet;
	int trueSubsetCount;
	
	public CompoundRole(String name) {
		this.name=name;
		children = new HashSet<Role>();
		isSuperSet=false;
		trueSubsetCount=0;
	}
	
	public void addRole (Role Role2add) {
		children.add(Role2add);
	}
	
	public void removeAllRoles() {
		children.clear();
	}
}
