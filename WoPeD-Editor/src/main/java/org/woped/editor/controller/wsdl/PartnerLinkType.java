package org.woped.editor.controller.wsdl;

import java.util.ArrayList;

public class PartnerLinkType {

/*
 * Variables
 ************************************************/
	private String partnerLinkType_Name;
	private ArrayList<Role> roles = new ArrayList<Role>();

/*
 * Setter methods
 ************************************************/
	public void setName(String name){
		partnerLinkType_Name = name;
	}

/*
 * Getter methods
 ************************************************/
	public String getName(){
		return partnerLinkType_Name;
	}

/*
 * Methods for manipulating the ArrayList "roles"
 ************************************************/
	public void addRole(Role role){
		roles.add(role);
	}
	public ArrayList<Role> getRoles(){
		return roles;
	}

}
