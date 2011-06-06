package org.woped.editor.controller;


			class RoleLocator implements Comparable<RoleLocator> {
				String name; 	// Name of the Role
				int column; 	// # of the column in the grid
				int ystart; 	// First element of the Role
				int yend; 		// Last element of the Role
				
				//Constructor
				public RoleLocator(String name, int column, int ystart, int yend) {
					this.name = name;
					this.column = column;
					this.ystart = ystart;
					this.yend = yend;
				}
				
				public int getColumn(){
					return this.column;
				}
				
				public int compareTo(RoleLocator o){
					
					if((this.ystart-this.yend)<(o.ystart-o.yend)){
						return -1;
					}else {
						return 1;
					}
				}
			}