package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.woped.editor.controller.Role;
import org.woped.editor.controller.GroupLayoutGrid;
import org.woped.editor.controller.Layout;
import org.woped.editor.controller.RoleLayoutGrid;
import org.woped.gui.translations.Messages;

			//*****************************Layout algorithm**********************
			//*******************************************************************
			
			/**
			 * The layout algorithm which creates a sorted grid out of the Logical model.
			 * 
			 * @author Jonathan Peuker, Cosima Juenger, Daniel Becea, Fredick Rouviere, Johannes Grenzemann
			 * @version 1.0
			 */

			class LayoutAlgorithm {
				Set<Layout> RoleLayoutSet = new HashSet<Layout>();
				Set<Layout> GroupLayoutSet = new HashSet<Layout>();
				
				public void createLayout(LogicalModel lm) {
					
					//Execute algorithm for the compound Roles
					if (lm.allRoles.size() > 0) {
						
						if (lm.allCompoundRoles.size() == 1) {
							boolean unrelatedRole = false;
							Iterator<?> it = lm.allRoles.iterator();
							while (it.hasNext()) {
								Role currentRole = (Role) it.next();
								Iterator<?> subit = currentRole.ancestors.iterator();
								while (subit.hasNext()) {
									CompoundRole currentCompound = (CompoundRole) subit.next();
									if (currentCompound.name == "") {
										unrelatedRole = true;
										break;
									}
								}
								if (unrelatedRole == true ) {
									break;
								}
							}
							if (unrelatedRole == true) {
								CompoundRole allRoles = new CompoundRole(Messages.getString("PetriNet.Resources.AllCompoundRolesName"));
								allRoles.children.addAll(lm.allRoles);
								it = lm.allRoles.iterator();
								while (it.hasNext()) {
									Role currentRole = (Role) it.next();
									currentRole.ancestors.add(allRoles);
								}
								lm.allCompoundRoles.add(allRoles);
							}
						}
						
						else if (lm.allCompoundRoles.size() == 0) {
							CompoundRole cr = new CompoundRole(Messages.getString("PetriNet.Resources.DefaultCompoundRoleName"));
							cr.children.addAll(lm.allRoles);
							Iterator<?> it = lm.allRoles.iterator();
							while (it.hasNext()) {
								Role currentRole = (Role) it.next();
								currentRole.ancestors.add(cr);
							}
							lm.allCompoundRoles.add(cr);
						}
					
						else {
							CompoundRole allRoles = new CompoundRole(Messages.getString("PetriNet.Resources.AllCompoundRolesName"));
							allRoles.children.addAll(lm.allRoles);
							Iterator<?> it = lm.allRoles.iterator();
							while (it.hasNext()) {
								Role currentRole = (Role) it.next();
								currentRole.ancestors.add(allRoles);
							}
							lm.allCompoundRoles.add(allRoles);
						}
					
						Iterator<?> it = lm.allCompoundRoles.iterator();
						while (it.hasNext()) {
							CompoundRole currentCompoundRole = (CompoundRole) it.next();
							RoleLayoutGrid grid = new RoleLayoutGrid(currentCompoundRole);
							grid.identifyTrueSubSets();
							grid.analyzeSubSets();
							if (grid.complexSubsets == false) {
								grid.removeTrueSubSets();
							}
							grid.identifyUsersWithSingleAncestor();
							grid.moveUsersWithSingleAncestors();
							grid.identifyUsersWithMultipleAncestors();
							grid.moveUsersWithMultipleAncestors();
							grid.adjustYAxis();
							Layout layout = grid.createLayout();
							layout.name = currentCompoundRole.name;
							RoleLayoutSet.add(layout);
						}
					}
					
					//Execute algorithm for the compound Groups
					if (lm.allGroups.size() > 0) {
						
						if (lm.allCompoundGroups.size() == 1) {
							boolean unrelatedGroup = false;
							Iterator<?> it = lm.allGroups.iterator();
							while (it.hasNext()) {
								Group currentGroup = (Group) it.next();
								Iterator<?> subit = currentGroup.ancestors.iterator();
								while (subit.hasNext()) {
									CompoundGroup currentCompound = (CompoundGroup) subit.next();
									if (currentCompound.name == "") {
										unrelatedGroup = true;
										break;
									}
								}
								if (unrelatedGroup == true ) {
									break;
								}
							}
							if (unrelatedGroup == true) {
								CompoundGroup allGroups = new CompoundGroup(Messages.getString("PetriNet.Resources.AllCompoundGroupsName"));
								allGroups.children.addAll(lm.allGroups);
								it = lm.allGroups.iterator();
								while (it.hasNext()) {
									Group currentGroup = (Group) it.next();
									currentGroup.ancestors.add(allGroups);
								}
								lm.allCompoundGroups.add(allGroups);
							}
						}
						
						else if (lm.allCompoundGroups.size() == 0) {
							CompoundGroup cg = new CompoundGroup(Messages.getString("PetriNet.Resources.DefaultCompoundGroupName"));
							cg.children.addAll(lm.allGroups);
							Iterator<?> it = lm.allGroups.iterator();
							while (it.hasNext()) {
								Group currentGroup = (Group) it.next();
								currentGroup.ancestors.add(cg);
							}
							lm.allCompoundGroups.add(cg);
						}

						else {
							CompoundGroup allGroups = new CompoundGroup(Messages.getString("PetriNet.Resources.AllCompoundGroupsName"));
							allGroups.children.addAll(lm.allGroups);
							Iterator<?> it = lm.allGroups.iterator();
							while (it.hasNext()) {
								Group currentGroup = (Group) it.next();
								currentGroup.ancestors.add(allGroups);
							}
							lm.allCompoundGroups.add(allGroups);
						}
					
						Iterator<?> it = lm.allCompoundGroups.iterator();
						while (it.hasNext()) {
							CompoundGroup currentCompoundGroup = (CompoundGroup) it.next();
							GroupLayoutGrid grid = new GroupLayoutGrid(currentCompoundGroup);
							grid.identifyTrueSubSets();
							grid.analyzeSubSets();
							if (grid.complexSubsets == false) {
								grid.removeTrueSubSets();
							}
							grid.identifyUsersWithSingleAncestor();
							grid.moveUsersWithSingleAncestors();
							grid.identifyUsersWithMultipleAncestors();
							grid.moveUsersWithMultipleAncestors();
							grid.adjustYAxis();
							Layout layout = grid.createLayout();
							layout.name = currentCompoundGroup.name;
							GroupLayoutSet.add(layout);
						}
					}
				}
			}