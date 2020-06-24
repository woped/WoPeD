package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Set;

			//Representation of the Group
			class Group {
				String name = null;
				Set<Component> children = null;
				Set<CompoundGroup> ancestors = null;
				boolean isSuperGroup;
				boolean columnislocked;
				Set<Group> truesubsets;
				int column;
				
				public Group(String name) {
					this.name=name;
					ancestors = new HashSet<CompoundGroup>();
					children = new HashSet<Component>();
					truesubsets = new HashSet<Group>();
					isSuperGroup = false;
					columnislocked = false;
				}
				
				public void addAncestor(CompoundGroup ancestor) {
					ancestors.add(ancestor);
				}
				
				public void addChild(Component child) {
					children.add(child);
				}
				
				public void removeAllAncestors() {
					ancestors.clear();
				}
				
				public void removeAllChildren() {
					children.clear();
				}
			}