package org.woped.editor.controller;


			class GroupLocator implements Comparable<GroupLocator>{
				String name; 	// Name of the Group
				int column; 	// # of the column in the grid
				int ystart; 	// First element of the Group
				int yend; 		// Last element of the Group
				
				//Constructor
				public GroupLocator(String name, int column, int ystart, int yend) {
					this.name = name;
					this.column = column;
					this.ystart = ystart;
					this.yend = yend;
				}
				
				public int getColumn(){
					return this.column;
				}
				
				public int compareTo(GroupLocator o){
					
					if((this.ystart-this.yend)<(o.ystart-o.yend)){
						return -1;
					}else {
						return 1;
					}
				}
			}