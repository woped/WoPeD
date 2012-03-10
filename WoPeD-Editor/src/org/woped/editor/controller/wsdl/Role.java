package org.woped.editor.controller.wsdl;

public class Role {

/*
 * Variables
 ************************************************/
	private String Role_Name;
	private String PortType_Name;

/*
 * Setter methods
 ************************************************/
	public void setRoleName(String roleName){
		Role_Name = roleName;
	}

	public void setPortTypeName(String portTypeName){
		PortType_Name = portTypeName;
	}

/*
 * Getter methods
 ************************************************/
	public String getRoleName(){
		return Role_Name;
	}

	public String getPortTypeName(){
		return PortType_Name;
	}

}
