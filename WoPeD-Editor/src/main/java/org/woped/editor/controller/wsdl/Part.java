package org.woped.editor.controller.wsdl;

public class Part {

/*
 * Variables
 ************************************************/
	private String Part_Name;
	private String Part_Element;

/*
 * Setter methods
 ************************************************/
/**
 * Sets the name of the "part".
 *
 * @param 	partName 	Name of the "part".
 **********************************************************************************************************************/
	public void setPartName(String partName){
		Part_Name = partName;
	}

/**
 * Sets the element of the "part".
 *
 * @param 	partElement 	Element of the "part".
 **********************************************************************************************************************/
	public void setPartElement(String partElement){
		Part_Element = partElement;
	}

/*
 * Getter methods
 ************************************************/
/**
 * Gets the name of the "part".
 *
 * @return 	Part_Name 	Name of the "part".
 **********************************************************************************************************************/
	public String getPartName(){
		return Part_Name;
	}

/**
 * Gets the element of the "part".
 *
 * @return 	Part_Element 	Element of the "part".
 **********************************************************************************************************************/
	public String getPartElement(){
		return Part_Element;
	}
}
