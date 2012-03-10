package org.woped.editor.controller.wsdl;

import java.util.ArrayList;

public class PortType {

/*
 * Variables
 ************************************************/
	private String PortType_Name;
	private ArrayList<Operation> operations = new ArrayList<Operation>();


/*
 * Setter methods
 ************************************************/
	public void setPortTypeName(String portTypeName){
		PortType_Name = portTypeName;
	}

/*
 * Getter methods
 ************************************************/
	public String getPortTypeName(){
		return PortType_Name;
	}
	public ArrayList<Operation> getOperations(){
		return operations;
	}

/*
 * Methods for manipulating the ArrayLists
 ************************************************/
	public void addOperation(Operation operation){
		operations.add(operation);
	}

}
