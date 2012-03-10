package org.woped.editor.controller.wsdl;

import java.util.ArrayList;

public class Message {

/*
 * Variables
 ************************************************/
	private String Message_Name;
	private ArrayList<Part> parts = new ArrayList<Part>();

/*
 * Setter methods
 ************************************************/
/**
 * Sets the name of the "message".
 *
 * @param 	messageName 	Name of the "message".
 **********************************************************************************************************************/
	public void setMessageName(String messageName){
		Message_Name = messageName;
	}

/*
 * Getter methods
 ************************************************/
/**
 * Gets the name of the "message".
 *
 * @return 	messageName 	Name of the "message".
 **********************************************************************************************************************/
	public String getMessageName(){
		return Message_Name;
	}

/**
 * Returns an ArrayList with all "Parts".
 *
 * @return 	An ArrayList of type <Part> which contains all "Parts".
 ***********************************************************************************************/
	public ArrayList<Part> getParts(){
		return parts;
	}

/*
 * Methods for manipulating the ArrayLists
 ************************************************/
/**
 * Adds a "part" to the internal ArrayList "parts" which stores the "parts".
 *
 * @param 	part 	An object of type "Part" which shall be added to the internal data storage.
 **********************************************************************************************************************/
	public void addPart(Part part){
		parts.add(part);
	}

}
