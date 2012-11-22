package org.woped.core.model.petrinet;

import java.util.ArrayList;

public class ParaphrasingModel {

	private ArrayList<Object[]> table= new ArrayList<Object[]>(0);
	
	public ParaphrasingModel(){

	}
	
	public ParaphrasingModel(String id){
	}
	
	public void addElement(String id, String text){
		String[] row = {id, text};
		this.table.add(row);
	}
	
	public void addRow(String[] row){
		this.table.add(row);
	}
	
	public String[] getElementByRow(int row){
		//return this.getElementByRow(row);
		return (String[])this.table.get(row);
	}
	
	public void setId(String id){
	}
	
	public int getTableSize(){
		return this.table.size();
	}
	
	public void deleteValues(){
		this.table.clear();
	}
	
	
}
