package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Set;

/**
 * The logical model created in the background - this model is the basis for the work of the Layouting Algorithm
 * @author Jonathan Peuker, Frederick Rouviere, Johannes Grenzemann, Cosima Juenger, Daniel Becea
 * @version 1.0
 */

// The five elements of the logical model: Compound Role, Compound Group, Role, Group & Component
			  
//Representation of the Compound Group
class CompoundGroup {
	String name = null;
	Set<Group> children = null;
	boolean isSuperGroup;
	int trueSubGroupsCount;
	
	public CompoundGroup(String name) {
		this.name = name;
		children = new HashSet<Group>();
		isSuperGroup = false;
		trueSubGroupsCount = 0;
	}
	
	public void addGroup (Group Group2add) {
		children.add(Group2add);
	}
	
	public void removeAllGroups() {
		children.clear();
	}
}