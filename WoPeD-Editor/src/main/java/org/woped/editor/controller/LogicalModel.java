package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.woped.editor.controller.PetriNetResourceEditor.GroupsTreeNode;
import org.woped.editor.controller.PetriNetResourceEditor.RolesTreeNode;
import org.woped.editor.controller.PetriNetResourceEditor.SuperGroupsTreeNode;
import org.woped.editor.controller.PetriNetResourceEditor.SuperRolesTreeNode;

/**
 * The logical model created in the background - this model is the basis for the work of the Layouting Algorithm
 * @author Jonathan Peuker, Frederick Rouviere, Johannes Grenzemann, Cosima Juenger, Daniel Becea
 * @version 1.0
 */

			//The Logical Model itself
			class LogicalModel {
				Set<Component> allAssignedComponents;
				Set<Component> allUnAssignedComponents;
				Set<Group> allGroups;
				Set<Role> allRoles;
				Set<CompoundRole> allCompoundRoles;
				Set<CompoundGroup> allCompoundGroups;
				
				public LogicalModel(DefaultListModel objectsAssignedListModel,DefaultListModel objectsUnassignedListModel,
						DefaultTreeModel RolesTreeModel,DefaultMutableTreeNode RolesTopNode,
						DefaultTreeModel GroupsTreeModel, DefaultMutableTreeNode GroupsTopNode,
						DefaultTreeModel superRolesTreeModel,DefaultTreeModel superGroupsTreeModel,
						DefaultMutableTreeNode superRolesTopNode,DefaultMutableTreeNode superGroupsTopNode) {
					
					//Instantiate the Sets for the Logical Model
					allAssignedComponents = new HashSet<Component>();
					allUnAssignedComponents = new HashSet<Component>();
					allGroups = new HashSet<Group>();
					allRoles = new HashSet<Role>();
					allCompoundRoles = new HashSet<CompoundRole>();
					allCompoundGroups = new HashSet<CompoundGroup>();
					
					//Get assigned Components
					 for (int i=0;i<objectsAssignedListModel.getSize();i++) {
						 Component currentComponent = new Component(objectsAssignedListModel.get(i).toString());
						 allAssignedComponents.add(currentComponent);
					 } 
					 
					 //Get unassigned Components
					 for (int i=0;i<objectsUnassignedListModel.getSize();i++) {
						 Component currentComponent = new Component(objectsUnassignedListModel.get(i).toString());
						 allUnAssignedComponents.add(currentComponent);
					 }
					 
					 //Get Roles
					 for(int j =0; j <  RolesTreeModel.getChildCount(RolesTopNode);j++){
			             String currentRoleName = RolesTreeModel.getChild(RolesTopNode, j).toString();
			             Role currentRole = new Role(currentRoleName);
			             allRoles.add(currentRole);
			             
			             RolesTreeNode currentNode = (RolesTreeNode) RolesTreeModel.getChild(RolesTopNode, j);
			            
			             // find all children of currentRole and assign them to the Role
			             for(int i =0; i <  RolesTreeModel.getChildCount(currentNode);i++){
			                   String currentComponentName = (String) RolesTreeModel.getChild(currentNode, i).toString();
			                   
			                   if (allAssignedComponents.size() != 0) {
			                	   Iterator<?> it = allAssignedComponents.iterator();
			                	   while (it.hasNext()) {
			                		   Component currentComponent = (Component) it.next();
			                		   if (currentComponent.name.equals( currentComponentName)) {
			                			   currentRole.addChild(currentComponent);
			                			   currentComponent.addAncestorRole(currentRole);
			                		   }
			                	   }
			                   }
			             }
			             
			             
			          }
					 
					//Get Groups
					 for(int j =0; j <  GroupsTreeModel.getChildCount(GroupsTopNode);j++){
			             String currentGroupName = GroupsTreeModel.getChild(GroupsTopNode, j).toString();
			             Group currentGroup = new Group(currentGroupName);
			             allGroups.add(currentGroup);
			             
			             GroupsTreeNode currentNode = (GroupsTreeNode) GroupsTreeModel.getChild(GroupsTopNode, j);
			            
			             // find all children of currentGroup and assign them to the Role
			             for(int i =0; i <  GroupsTreeModel.getChildCount(currentNode);i++){
			                   String currentComponentName = (String) GroupsTreeModel.getChild(currentNode, i).toString();
			                   
			                   if (allAssignedComponents.size() != 0) {
			                	   Iterator<?> it = allAssignedComponents.iterator();
			                	   while (it.hasNext()) {
			                		   Component currentComponent = (Component) it.next();
			                		   if (currentComponent.name.equals(currentComponentName)) {
			                			   currentGroup.addChild(currentComponent);
			                			   currentComponent.addAncestorGroup(currentGroup);
			                		   }
			                	   }
			                   }
			             }
			             
			             
			          }
					 
					 //Get Compound Roles
					 for(int j =0; j <  superRolesTreeModel.getChildCount(superRolesTopNode);j++){
			             String currentSuperRoleName = superRolesTreeModel.getChild(superRolesTopNode, j).toString();
			             CompoundRole currentSuperRole = new CompoundRole(currentSuperRoleName);
			             allCompoundRoles.add(currentSuperRole);
			             
			             SuperRolesTreeNode currentNode = (SuperRolesTreeNode) superRolesTreeModel.getChild(superRolesTopNode, j);
			            
			             // Get all Children of each Compound Role Branches
			             for(int i =0; i <  superRolesTreeModel.getChildCount(currentNode);i++){
			                   String currentRoleName = (String) superRolesTreeModel.getChild(currentNode, i).toString();
			                   
			                   for (int t=0;t<allRoles.size();t++) {
			                	   
			                	   Iterator<?> it = allRoles.iterator();
			                	   while (it.hasNext()) {
			                		   Role currentRole = (Role) it.next();
			                		   if (currentRoleName.equals(currentRole.name)) {
			                			   currentRole.addAncestor(currentSuperRole);
			                			   currentSuperRole.addRole(currentRole);
			                		   }
			                	   }
			                   }
			             }
			        }
					 
					//Get Compound Groups
					 for(int j =0; j <  superGroupsTreeModel.getChildCount(superGroupsTopNode);j++){
			             String currentSuperGroupName = superGroupsTreeModel.getChild(superGroupsTopNode, j).toString();
			             CompoundGroup currentSuperGroup = new CompoundGroup(currentSuperGroupName);
			             allCompoundGroups.add(currentSuperGroup);
			             
			             SuperGroupsTreeNode currentNode = (SuperGroupsTreeNode) superGroupsTreeModel.getChild(superGroupsTopNode, j);
			            
			             // Get all Children of each Compound Group Branches
			             for(int i =0; i <  superGroupsTreeModel.getChildCount(currentNode);i++){
			                   String currentGroupName = (String) superGroupsTreeModel.getChild(currentNode, i).toString();
			                      
			                	   Iterator<?> it = allGroups.iterator();
			                	   while (it.hasNext()) {
			                		   Group currentGroup = (Group) it.next();
			                		   if (currentGroupName.equals(currentGroup.name)) {
			                			   currentGroup.addAncestor(currentSuperGroup);
			                			   currentSuperGroup.addGroup(currentGroup);
			                		   }
			                	   }
			             }
			        }
					 
					 //Fill empty sets
					 Iterator<?> it = allAssignedComponents.iterator();
					 while (it.hasNext()) {
						 Component currentComponent = (Component) it.next();
						 if (currentComponent.roleAncestor.size() == 0) {
							 currentComponent.addAncestorRole(new Role(""));
						 }
					 }
					 
					 it = allGroups.iterator();
					 while (it.hasNext()) {
						 Group currentGroup = (Group) it.next();
						 if (currentGroup.children.size() == 0) {
							 currentGroup.addChild(new Component(""));
						 }
						 if (currentGroup.ancestors.size() == 0) {
							 currentGroup.addAncestor(new CompoundGroup(""));
						 }
					 }
					 
					 it = allRoles.iterator();
					 while (it.hasNext()) {
						 Role currentRole = (Role) it.next();
						 if (currentRole.children.size() == 0) {
							 currentRole.addChild(new Component(""));
						 }
						 if (currentRole.ancestors.size() == 0) {
							 currentRole.addAncestor(new CompoundRole(""));
						 }
					 }
				}
				
			}