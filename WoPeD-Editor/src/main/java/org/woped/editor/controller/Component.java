package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Set;

/**
 * The logical model created in the background - this model is the basis for the work of the Layouting Algorithm
 * @author Jonathan Peuker, Frederick Rouviere, Johannes Grenzemann, Cosima Juenger, Daniel Becea
 * @version 1.0
 */

//Representation of the basic user as a Component of the logical model
class Component {
	String name = null;
	Set<Role> roleAncestor = null;
	Set<Group> groupAncestor = null;
	boolean partOfTrueSubset;
	Set<Component> neighboursInSubset = null;
	
	public Component(String name) {
		this.name=name;
		roleAncestor = new HashSet<Role>();
		groupAncestor = new HashSet<Group>();
		neighboursInSubset = new HashSet<Component>();
		partOfTrueSubset = false;
	}
	
	public void addAncestorRole(Role ancestor) {
		roleAncestor.add(ancestor);
	}
	
	public void addAncestorGroup(Group ancestor) {
		groupAncestor.add(ancestor);
	}
	
	public void clearAncestorGroups() {
		groupAncestor.clear();
	}
	
	public void clearAncestorRoles() {
		roleAncestor.clear();
	}
}