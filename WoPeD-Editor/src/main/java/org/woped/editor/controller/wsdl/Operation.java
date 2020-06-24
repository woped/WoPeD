package org.woped.editor.controller.wsdl;

public class Operation {

/*
 * Variables
 ************************************************/
	private String Operation_Name;
	private String Input_Message;
	private String Output_Message;

/*
 * Setter methods
 ************************************************/
/**
 * Sets the name of the "operation".
 *
 * @param 	operationName 	Name of the "operation".
 **********************************************************************************************************************/
	public void setOperationName(String operationName){
		Operation_Name = operationName;
	}

/**
 * Sets the value of the "input message".
 *
 * @param 	inputMessage 	Name of the "input message".
 **********************************************************************************************************************/
	public void setInputMessage(String inputMessage){
		Input_Message = inputMessage;
	}

/**
 * Sets the value of the "output message".
 *
 * @param 	outputMessage 	Name of the "output message".
 **********************************************************************************************************************/
	public void setOutputMessage(String outputMessage){
		Output_Message = outputMessage;
	}

/*
 * Getter methods
 ************************************************/
/**
 * Gets the name of the "operation".
 *
 * @return 	Operation_Name 	Name of the "operation".
 **********************************************************************************************************************/
	public String getOperationName(){
		return Operation_Name;
	}

/**
 * Gets the value of the "input message".
 *
 * @return 	Input_Message 	Name of the "input message".
 **********************************************************************************************************************/
	public String getInputMessage(){
		return Input_Message;
	}

/**
 * Gets the name of the "output message".
 *
 * @return 	Output_Message 	Name of the "output message".
 **********************************************************************************************************************/
	public String getOutputMessage(){
		return Output_Message;
	}

}