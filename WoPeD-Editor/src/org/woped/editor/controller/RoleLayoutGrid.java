package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.woped.editor.controller.RoleLocator;
import org.woped.editor.controller.Layout;

//import org.woped.editor.controller.PetriNetResourceEditor.RoleLayoutGrid.subsetRelation;

			// Class which creates and reworks the Role grid
			class RoleLayoutGrid {
				CompoundRole currentCompound;
				Set<Role> columns;
				Set<Role> allColumns;
				Component[][] elements;
				int columnscount;
				int rowscount;
				Set<Role> lockedColumns;
				Set<Component> lockedElements;
				boolean complexSubsets;
				boolean subsetsRelatedtoNonSubsets;
				Set<Component> usersWithSingleAncestors;
				Set<Component> usersWithMultipleAncestors;
				class subsetRelation {
					Role subset;
					Role superset;
					public subsetRelation(Role subset, Role superset) {
						this.subset = subset;
						this.superset = superset;
					}
				}
				Set<subsetRelation> subSets;
				
				//Constructor
				public RoleLayoutGrid(CompoundRole cr) {
					currentCompound = cr;
					columns = new HashSet<Role>();
					subSets = new HashSet<subsetRelation>();
					allColumns = new HashSet<Role>();
					usersWithSingleAncestors = new HashSet<Component>();
					usersWithMultipleAncestors = new HashSet<Component>();
					Iterator<?> it = cr.children.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						columns.add(currentRole);
					}
					allColumns.addAll(columns);
					
					columnscount = columns.size();
					rowscount=0;
					it = columns.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						if (currentRole.children.size()>rowscount) rowscount = currentRole.children.size();
					}
					rowscount = rowscount*2;
					elements = new Component[columnscount][rowscount];
					int x = 0;
					int y = 0;
					it = columns.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						currentRole.column = x;
						Iterator<?> subit = currentRole.children.iterator();
						while (subit.hasNext()) {
							Component currentComponent = (Component) subit.next();
							elements[x][y]=currentComponent;
							y+=1;
						}
						x+=1;
						y=0;
					}
				}
				
				//Find true subsets
				public void identifyTrueSubSets() {
					Iterator<?> it1 = columns.iterator();
					while (it1.hasNext()) {
						Role currentRole1 = (Role) it1.next();
						Iterator<?> it2 = columns.iterator();
						while (it2.hasNext()) {
							Role currentRole2 = (Role) it2.next();
							if (currentRole1.children.size() >= currentRole2.children.size()) {
								if (currentRole1 != currentRole2) {
									if (currentRole1.children.containsAll(currentRole2.children)) {
										subsetRelation currentRelation = new subsetRelation(currentRole2, currentRole1);
										currentRelation.superset.isSuperSet = true;
										currentRelation.superset.truesubsets.add(currentRelation.subset);
										Iterator<?> it3 = currentRelation.subset.children.iterator();
										while (it3.hasNext()) {
											Component currentComponent = (Component) it3.next();
											currentComponent.partOfTrueSubset = true;
											currentComponent.neighboursInSubset.addAll(currentRelation.subset.children);
										}
										subSets.add(currentRelation);
									}
								}
							}
						}
					}
				}
				
				//Analyze how the true subsets are related to each other
				public void analyzeSubSets() {
					int trueSubSetsAreRelated = 0;
					Iterator<?> it1 = subSets.iterator();
					while (it1.hasNext()) {
						subsetRelation currentRelation1 = (subsetRelation) it1.next();
						Iterator<?> it2 = subSets.iterator();
						while (it2.hasNext()) {
							subsetRelation currentRelation2 = (subsetRelation) it2.next();
							if (currentRelation1.superset == currentRelation2.superset) {
								trueSubSetsAreRelated+=1;
							}
						}
					}
					if (trueSubSetsAreRelated>subSets.size()) complexSubsets = true;
					
					if (complexSubsets == true) {
						Set<Role> relatedRoles = new HashSet<Role>();
						Set<Component> subsetelements = new HashSet<Component>();
						Iterator<?> it = subSets.iterator();
						while (it.hasNext()) {
							subsetRelation currentRelation = (subsetRelation) it.next();
							relatedRoles.add(currentRelation.subset);
							relatedRoles.add(currentRelation.superset);
							Iterator<?> subsetiterator = currentRelation.subset.children.iterator();
							while (subsetiterator.hasNext()) {
								Component currentComponent = (Component) subsetiterator.next();
								subsetelements.add(currentComponent);
							}
						}
						boolean strangerelatives = false;
						it = subsetelements.iterator();
						while (it.hasNext()) {
							Component currentComponent = (Component) it.next();
							if (relatedRoles.containsAll(currentComponent.roleAncestor)) {
								strangerelatives = false;
							}
							else { strangerelatives = true; }
						}
						if (strangerelatives == true) {
							subsetsRelatedtoNonSubsets = true;
						}
					}
				}
				
				//Remove true subsets from the grid
				public void removeTrueSubSets() {
						Iterator<?> it = subSets.iterator();
						while (it.hasNext()) {
							subsetRelation currentRelation = (subsetRelation) it.next();
							columns.remove(currentRelation.subset);
						}
						columnscount = columns.size();
						rowscount = 0;
						it = columns.iterator();
						while (it.hasNext()) {
							Role currentRole = (Role) it.next();
							if (currentRole.children.size()>rowscount) rowscount = currentRole.children.size();
						}
						rowscount = rowscount*2;
						elements = new Component[columnscount][rowscount];
						int x = 0;
						int y = 0;
						it = columns.iterator();
						while (it.hasNext()) {
							Role currentRole = (Role) it.next();
							currentRole.column = x;
							if (currentRole.isSuperSet) {
								
								Set<Component> Componentsofsubsets = new HashSet<Component>();
								Iterator<?> subit = currentRole.truesubsets.iterator();
								while (subit.hasNext()) {
									Role currentSubSet = (Role) subit.next();
									Componentsofsubsets.addAll(currentSubSet.children);
								}
								
								Set<Component> Componentsnotinsubsets = new HashSet<Component>();
								Componentsnotinsubsets.addAll(currentRole.children);
								Componentsnotinsubsets.removeAll(Componentsofsubsets);
								
								subit = Componentsofsubsets.iterator();
								while (subit.hasNext()) {
									Component currentComponent = (Component) subit.next();
									elements[x][y]=currentComponent;
									//lockedElements.add(currentComponent);
									y+=1;
								}
								
								subit = Componentsnotinsubsets.iterator();
								while (subit.hasNext()) {
									Component currentComponent = (Component) subit.next();
									elements[x][y]=currentComponent;
									y+=1;
								}
							}
							else {
								Iterator<?> subit = currentRole.children.iterator();
								while (subit.hasNext()) {
									Component currentComponent = (Component) subit.next();
									elements[x][y]=currentComponent;
									y+=1;
								}
							}
							x+=1;
							y=0;
						}
						it = subSets.iterator();
						while (it.hasNext()) {
							subsetRelation currentRelation = (subsetRelation) it.next();
							currentRelation.subset.column = currentRelation.superset.column;
						}
				}
				
				//Method which finds out which users in the grid have only one ancestor
				public void identifyUsersWithSingleAncestor() {
					Iterator<?> it = columns.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						Iterator<?> subit = currentRole.children.iterator();
						while (subit.hasNext()) {
							Component currentComponent = (Component) subit.next();
							if (currentComponent.roleAncestor.size() == 1) {
								usersWithSingleAncestors.add(currentComponent);
							}
						}
					}
				}
				
				//Move Groups which consist only of users which have only one ancestor
				public void moveUsersWithSingleAncestors() {
					Set<Role> Rolestomoveleft = new HashSet<Role>();
					Set<Role> Rolestomoveright = new HashSet<Role>();
					Component[][] placeholderleft = new Component[columns.size()][];
					Component[][] placeholderright = new Component[columns.size()][];
					Component[][] placeholder = new Component[columns.size()][];
					int leftindex = 0;
					int rightindex = 0;
					
					Iterator<?> it = columns.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						if (usersWithSingleAncestors.containsAll(currentRole.children)) {
							Rolestomoveleft.add(currentRole);
							placeholderleft[leftindex] = elements[currentRole.column];
							currentRole.column = leftindex;
							leftindex+=1;
						}
						else {
							Rolestomoveright.add(currentRole);
							placeholderright[rightindex] = elements[currentRole.column];
							currentRole.column = rightindex;
							rightindex+=1;
						}
					}
					
					int currentindex = 0;
					for (int i=0; i<placeholderleft.length;i++) {
						if (placeholderleft[i] != null) {
							placeholder[currentindex] = placeholderleft[i];
							currentindex+=1;
						}
					}
					for (int i=0; i<placeholderright.length;i++) {
						if (placeholderright[i] != null) {
							placeholder[currentindex] = placeholderright[i];
							currentindex+=1;
						}
					}
					elements = placeholder;
					
					it = Rolestomoveright.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						currentRole.column = currentRole.column + Rolestomoveleft.size();
					}
					
					if (complexSubsets == false) {
						it = subSets.iterator();
						while (it.hasNext()) {
							subsetRelation currentRelation = (subsetRelation) it.next();
							currentRelation.subset.column = currentRelation.superset.column;
						}
					}
				}
				
				//Method which finds out which users in the grid have multiple ancestors
				public void identifyUsersWithMultipleAncestors() {
					Iterator<?> it = columns.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						Iterator<?> subit = currentRole.children.iterator();
						while (subit.hasNext()) {
							Component currentComponent = (Component) subit.next();
							if (currentComponent.roleAncestor.size() > 1) {
								usersWithMultipleAncestors.add(currentComponent);
							}
						}
					}
				}
				
				//Method which sorts all Groups which are not applied by moveUsersWithSingleAncestors()
				public void moveUsersWithMultipleAncestors() {
					
					//Get the relevant Roles
					Set<Role> RolestoMove = new HashSet<Role>();
					Iterator<?> it = columns.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						if (usersWithSingleAncestors.containsAll(currentRole.children) == false) {
							RolestoMove.add(currentRole);
						}
					}
					
					if (RolestoMove.size() > 1) {
					
						//Identify the part of the grid which has to be reworked
						int lowestcolumn = columns.size();
						it = RolestoMove.iterator();
						while (it.hasNext()) {
							Role currentRole = (Role) it.next();
							if (currentRole.column < lowestcolumn) lowestcolumn = currentRole.column;
						}
						//Transfer this part into a work environment
						int dimensionofworkenv = elements.length - lowestcolumn;
						
						Role[] input = new Role[dimensionofworkenv];
						Role[] workset = new Role[input.length];
						Role[] result = new Role[input.length];
						
						
						it = RolestoMove.iterator();
						while (it.hasNext()) {
							Role currentRole = (Role) it.next();
							input[currentRole.column-lowestcolumn] = currentRole;
						}
						
						for (int i=0; i<workset.length;i++) {
							workset[i] = input[i];
						}
						
						//Determine intersections of relevant Roles
						int[][] intersections = new int[dimensionofworkenv][dimensionofworkenv];
						for (int i=0;i<workset.length;i++) {
							for (int y=0;y<workset.length;y++) {
								if (workset[i]!=workset[y]) {
									Set<Component> currentIntersection = new HashSet<Component>();
									currentIntersection.addAll(workset[i].children);
									currentIntersection.retainAll(workset[y].children);
									int dimension = currentIntersection.size();
									intersections[i][y] = dimension;
								}
							}
						}
						
						// Sort the workset according to the highest intersections
						
						int iterator = 0;
						boolean running = true;
						int index = 0;
						while (running) {
							result[index] = workset[iterator];
							int maxintersection = 0;
							int maxintersectionpos = 0;
							for (int i=0;i<intersections[iterator].length;i++) {
								if (intersections[iterator][i] > maxintersection) {
									maxintersection = intersections[iterator][i];
									maxintersectionpos = i;
								}
							}
							if (maxintersection == 0) {
								running = false;
							}
							else {
								for (int i=0; i<intersections.length;i++) {
									intersections[i][iterator] = 0;
								}
							}
							iterator = maxintersectionpos;
							index+=1;
						}
						
						//Fill result set with possible left over Roles.
						
						boolean arraynotfull = false;
						for (int i=0;i<result.length;i++) {
							if (result[i] == null) {
								arraynotfull = true;
							}
						}
						while (arraynotfull) {
							for (int y=0;y<workset.length;y++) {
								boolean alreadyincluded = false;
								for (int z=0;z<result.length;z++) {
									if (workset[y] == result[z]) {alreadyincluded = true; break;}
								}
								if (alreadyincluded == false) {
									for (int z=0;z<result.length;z++) {
										if (result[z] == null) {
											result[z] = workset[y];
											arraynotfull = false;
											break;
										}
									}
								}
								for (int i=0;i<result.length;i++) {
									if (result[i] == null) {
										arraynotfull = true;
									}
								}
							}
						}
						
						//Return the finished result set to the layout grid
						
						Component[][] placeholder = new Component[elements.length][];
						
						for (int i=0;i<result.length;i++) {
							placeholder[i+lowestcolumn] = elements[result[i].column];
							result[i].column = i+lowestcolumn;
						}
						
						for (int y=0;y<lowestcolumn;y++) {
							placeholder[y]=elements[y];
						}
						
						elements = placeholder;
						
						if (complexSubsets == false) {
							it = subSets.iterator();
							while (it.hasNext()) {
								subsetRelation currentRelation = (subsetRelation) it.next();
								currentRelation.subset.column = currentRelation.superset.column;
							}
						}
					
					}
				}
				
				// Method which modifies the grid so the single elements are all placed in the same grid row
				public void adjustYAxis() {
					//Determine if there are true subsets in the grid
					
					//No true subsets present
					if (subSets.size() == 0) {
						
						//Get all elements in the grid
						Set<Component> WorkSet = new HashSet<Component>();
						Iterator<?> it = columns.iterator();
						while (it.hasNext()) {
							Role currentRole = (Role) it.next();
							Iterator<?> subit = currentRole.children.iterator();
							while (subit.hasNext()) {
								Component currentComponent = (Component) subit.next();
								WorkSet.add(currentComponent);
							}
						}
						
						//Get the sole elements of the working set
						it = WorkSet.iterator();
						while (it.hasNext()) {
							Component currentComponent = (Component) it.next();
							
							//Remove the current element from the grid and save the relevant columns
							Set<Object> relevantColumns = new HashSet<Object>();
							Iterator<?> subit = currentComponent.roleAncestor.iterator();
							while (subit.hasNext()) {
								Role currentRole = (Role) subit.next();
								if (currentCompound.children.contains(currentRole)) {
									relevantColumns.add(new Integer(currentRole.column));
								}
							}
							
							subit = relevantColumns.iterator();
							while (subit.hasNext()) {
								int currentColumn = (Integer) subit.next();
								for (int i=0;i<elements[currentColumn].length;i++) {
									if (currentComponent == elements[currentColumn][i]) {
										elements[currentColumn][i] = null;
									}
								}
							}
							
							//Look for first common empty row in the relevant Roles and add the element in this row
							subit = relevantColumns.iterator();
							if (subit.hasNext()) {
								int currentColumn = (Integer) subit.next();
								for (int i=0;i<elements[currentColumn].length;i++) {
									if (elements[currentColumn][i] == null) {
										Iterator<?> subsubit = relevantColumns.iterator();
										int matchmax = relevantColumns.size();
										int matchcount = 0;
										while (subsubit.hasNext()) {
											int compareColumn = (Integer) subsubit.next();
											if (elements[compareColumn][i] == null) {
												matchcount+=1;
											}
										}
										if (matchcount == matchmax) {
											subsubit = relevantColumns.iterator();
											while (subsubit.hasNext()) {
												int compareColumn = (Integer) subsubit.next();
												elements[compareColumn][i] = currentComponent;
											}
											break;
										}
									}
								}
							}
						}
					}

					//True subsets present
					else {

						//Get all elements in the grid and create two worksets - one for the subset elements, one for the others
						Set<Component> NonSubSetElements = new HashSet<Component>();
						Set<Component> SubSetElements = new HashSet<Component>();
						Iterator<?> it = columns.iterator();
						while (it.hasNext()) {
							Role currentRole = (Role) it.next();
							Iterator<?> subit = currentRole.children.iterator();
							while (subit.hasNext()) {
								Component currentComponent = (Component) subit.next();
								NonSubSetElements.add(currentComponent);
							}
						}
						
						it = subSets.iterator();
						while (it.hasNext()) {
							subsetRelation currentRelation = (subsetRelation) it.next();
							Iterator<?> subit = currentRelation.subset.children.iterator();
							while (subit.hasNext()) {
								Component currentComponent = (Component) subit.next();
								SubSetElements.add(currentComponent);
								NonSubSetElements.remove(currentComponent);
							}
						}
						
						//Get the number of the elements which are located in the subsets and store this number
						int elementsInSubsets = SubSetElements.size();
						
						//Get the sole Non SubSet Elements
						it = NonSubSetElements.iterator();
						while (it.hasNext()) {
							Component currentComponent = (Component) it.next();
							
							//Remove the current element from the grid and save the relevant columns
							Set<Object> relevantColumns = new HashSet<Object>();
							Iterator<?> subit = currentComponent.roleAncestor.iterator();
							while (subit.hasNext()) {
								Role currentRole = (Role) subit.next();
								if (currentCompound.children.contains(currentRole)) {
									relevantColumns.add(new Integer(currentRole.column));
								}
							}
							
							subit = relevantColumns.iterator();
							while (subit.hasNext()) {
								int currentColumn = (Integer) subit.next();
								for (int i=0;i<elements[currentColumn].length;i++) {
									if (currentComponent == elements[currentColumn][i]) {
										elements[currentColumn][i] = null;
									}
								}
							}
							
							//Look for first common empty row in the relevant Roles and add the element in this row
							subit = relevantColumns.iterator();
							if (subit.hasNext()) {
								int currentColumn = (Integer) subit.next();
								for (int i=elementsInSubsets;i<elements[currentColumn].length;i++) {
									if (elements[currentColumn][i] == null) {
										Iterator<?> subsubit = relevantColumns.iterator();
										int matchmax = relevantColumns.size();
										int matchcount = 0;
										while (subsubit.hasNext()) {
											int compareColumn = (Integer) subsubit.next();
											if (elements[compareColumn][i] == null) {
												matchcount+=1;
											}
										}
										if (matchcount == matchmax) {
											subsubit = relevantColumns.iterator();
											while (subsubit.hasNext()) {
												int compareColumn = (Integer) subsubit.next();
												elements[compareColumn][i] = currentComponent;
											}
											break;
										}
									}
								}
							}
						}
						
						//Get the sole SubSet Elements
						it = SubSetElements.iterator();
						while (it.hasNext()) {
							Component currentComponent = (Component) it.next();
							
							//Remove the current element from the grid and save the relevant columns
							Set<Object> relevantColumns = new HashSet<Object>();
							Iterator<?> subit = currentComponent.roleAncestor.iterator();
							while (subit.hasNext()) {
								Role currentRole = (Role) subit.next();
								if (currentCompound.children.contains(currentRole)) {
									relevantColumns.add(new Integer(currentRole.column));
								}
							}
							
							subit = relevantColumns.iterator();
							while (subit.hasNext()) {
								int currentColumn = (Integer) subit.next();
								for (int i=0;i<elements[currentColumn].length;i++) {
									if (currentComponent == elements[currentColumn][i]) {
										elements[currentColumn][i] = null;
									}
								}
							}
							
							//Look for first common empty row in the relevant Roles and add the element in this row
							subit = relevantColumns.iterator();
							if (subit.hasNext()) {
								int currentColumn = (Integer) subit.next();
								for (int i=0;i<elements[currentColumn].length;i++) {
									if (elements[currentColumn][i] == null) {
										Iterator<?> subsubit = relevantColumns.iterator();
										int matchmax = relevantColumns.size();
										int matchcount = 0;
										while (subsubit.hasNext()) {
											int compareColumn = (Integer) subsubit.next();
											if (elements[compareColumn][i] == null) {
												matchcount+=1;
											}
										}
										if (matchcount == matchmax) {
											subsubit = relevantColumns.iterator();
											while (subsubit.hasNext()) {
												int compareColumn = (Integer) subsubit.next();
												elements[compareColumn][i] = currentComponent;
											}
											break;
										}
									}
								}
							}
						}
					}
				}
				
				//Method which removes empty rows from the grid
				public void removeNullRows(Component[][] elements) {
					
					if (elements.length > 1) {
						int maxY = 0;
						
						//Determine the last filled row
						for (int x=0;x<elements.length;x++) {
							for (int y=elements[x].length-1;y>0;y--) {
								if (elements[x][y] != null) {
									if (y > maxY) {
										maxY = y;
									}
								}
							}
						}
						
						//Find empty rows and remove them
						for (int i=0;i<maxY;i++) {
							for (int y=0;y<maxY;y++) {
								if (elements[0][y] == null) {
									boolean emptyrow = true;
									for (int x=0;x<elements.length;x++) {
										if (elements[x][y] != null) {
											emptyrow = false;
										}
									}
									if (emptyrow == true) {
										for (int x=0;x<elements.length;x++) {
											for (int suby=y;suby<=maxY;suby++) {
												elements[x][suby] = elements[x][suby+1];
												elements[x][suby+1] = null;
											}
										}
									}
								}
							}
						}
					}
				}
				
				//Sample the grid into the interface which is used by the front end to paint the graph
				public Layout createLayout() {
					Layout layout = new Layout(elements);
					removeNullRows(layout.grid);
					Iterator<?> it = allColumns.iterator();
					while (it.hasNext()) {
						Role currentRole = (Role) it.next();
						String name = currentRole.name;
						int column = currentRole.column;
						int ystart = -1;
						int yend = -1;
						boolean started = false;
						for (int i=0;i<elements[currentRole.column].length;i++) {
							if (currentRole.children.contains(elements[currentRole.column][i])) {
								if (started == false) {
									ystart = i;
									started = true;
								}
								if (started == true) {
									yend = i;
								}
							}
						}
						if (yend == -1) {
							yend = ystart;
						}
						RoleLocator locationOfCurrentRole = new RoleLocator(name, column, ystart, yend);
						layout.addRole(locationOfCurrentRole);
					}
					return layout;
				}
			}