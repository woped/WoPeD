package org.woped.editor.controller;

import java.util.Set;
import java.util.TreeSet;

			/**
			 * The Layout object which is handed over to the front end. It is based on the grid which is created by the Layout Algorithm.
			 * 
			 * @author Jonathan Peuker, Frederick Rouviere, Johannes Grenzemann, Daniel Becea, Cosima Juenger
			 * @version 1.0
			 */

			//The actual layout for handling in the frontend
			class Layout {
				String name;				//Name of the compound
				Component[][] grid; 		//Grid containing Components
				Set<RoleLocator> Roles; 	//Set containing all information relevant for the Roles
				Set<GroupLocator> Groups; 	//Set containing all information relevant for the Groups
				
				//Constructor
				public Layout(Component[][] elements) {
					grid = elements;
					Roles = new TreeSet<RoleLocator>();
					Groups = new TreeSet<GroupLocator>();
				}
				//Method to add Roles to the layout
				public void addRole(RoleLocator loc) {
					Roles.add(loc);
				}
				
				//Method to add Groups to the layout
				public void addGroup(GroupLocator loc) {
					Groups.add(loc);
				}
			}