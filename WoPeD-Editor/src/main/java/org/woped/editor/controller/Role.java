package org.woped.editor.controller;

import java.util.HashSet;
import java.util.Set;


			class Role {
				String name = null;
				Set<Component> children = null;
				Set<CompoundRole> ancestors = null;
				boolean isSuperSet;
				boolean columnislocked;
				Set<Role> truesubsets;
				int column;
				
				
				public Role(String name) {
					this.name=name;
					ancestors = new HashSet<CompoundRole>();
					children = new HashSet<Component>();
					truesubsets = new HashSet<Role>();
					isSuperSet = false;
					columnislocked = false;
				}
				public void addAncestor(CompoundRole ancestor) {
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