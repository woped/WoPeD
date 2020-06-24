package org.woped.editor.controller.wsdl;
import java.util.ArrayList;



public class WsdlFileRepresentation {


/*
 * Variables
 ************************************************/
	private ArrayList<PartnerLinkType> partnerLinkTypes = new ArrayList<PartnerLinkType>();
	private ArrayList<PortType> 	   portTypes 		= new ArrayList<PortType>();
	private ArrayList<Message> 		   messages 		= new ArrayList<Message>();
	private String					   namespace		= null;



/*
 * Setter methods
 ************************************************/
/**
 * Sets the namespace of the wsdl document.
 *
 * @param 	A String with the targetNamespace of the wsdl document.
 ***********************************************************************************************/
	public void setNamespace(String namespace){
		this.namespace = namespace;
	}

/*
 * Getter methods
 ************************************************/
/**
 * Returns an ArrayList with all "Partner Link Types".
 *
 * @return 	An ArrayList of type <PartnerLinkType> which contains all "Partner Link Types".
 ***********************************************************************************************/
	public ArrayList<PartnerLinkType> getPartnerLinkTypes(){
		return partnerLinkTypes;
	}

/**
 * Returns an ArrayList with all "Port Types".
 *
 * @return 	An ArrayList of type <PortType> which contains all "Port Types".
 ***********************************************************************************************/
	public ArrayList<PortType> getPortTypes(){
		return portTypes;
	}

/**
 * Returns an object of type PortType with the imported name.
 *
 * @param	portTypeName	Name of the Port Type which shall be returned.
 * @return 					An object of type PortType with the name of the imported variable.
 * @throws	NoPortTypeFoundException		There isn't a portType with the imported name.
 ***********************************************************************************************/
	public PortType getPortType(String portTypeName) throws NoPortTypeFoundException{
		PortType portType = new PortType();
		for(PortType x : portTypes){
			if (x.getPortTypeName().equals(portTypeName)){
				portType = x;
			}
		}
		try{
			if ( portType.getPortTypeName().equals("") ) {
			}
		}
		catch (NullPointerException e) {
			throw new NoPortTypeFoundException();
		}
		return portType;
	}


	public String getPortTypeNameByRoleName(String roleName) {
		String portTypeName = "";
		for(PartnerLinkType partnerLinkType : partnerLinkTypes){
			ArrayList<Role> roles = partnerLinkType.getRoles();
			for(Role role : roles){
				if (role.getRoleName().equals(roleName)){
					portTypeName = role.getPortTypeName();
				}
			}
		}
		return portTypeName;
	}

/**
 * Returns an ArrayList with all "Messages".
 *
 * @return 	An ArrayList of type <Message> which contains all "Messages".
 ***********************************************************************************************/
	public ArrayList<Message> getMessages(){
		return messages;
	}

/**
 * Returns an object of type Message with the imported name.
 *
 * @param	messageName	Name of the message which shall be returned.
 * @return 				An object of type Message with the name of the imported variable.
 * @throws	NoMessageFoundException		There isn't a message with the imported name.
 ***********************************************************************************************/
	public Message getMessage(String messageName) throws NoMessageFoundException{
		Message message = new Message();
		for(Message x : messages){
			if (x.getMessageName().equals(messageName)){
				message = x;
			}
		}
		try{
			if ( message.getMessageName().equals("") ) {
			}
		}
		catch (NullPointerException e) {
			throw new NoMessageFoundException();
		}
		return message;
	}


/**
 * Returns an object of type String with the targetNamespace of the wsdl document.
 *
 * @return 	A String with the targetNamespace of the wsdl document.
 ***********************************************************************************************/
	public String getNamespace(){
		return namespace;
	}


/*
 * Methods for manipulating the ArrayLists
 ************************************************/
/**
 * Adds a "partner link type" to the internal ArrayList "partnerLinkTypes" which stores the "partner link types".
 *
 * @param 	partnerLinkType 	An object of type "PartnerLinkType" which shall be added to the internal data storage.
 **********************************************************************************************************************/
	public void addPartnerLinkType(PartnerLinkType partnerLinkType){
		partnerLinkTypes.add(partnerLinkType);
	}

/**
 * Adds a "port type" to the internal ArrayList "portTypes" which stores the "port types".
 *
 * @param 	portType 	An object of type "PortType" which shall be added to the internal data storage.
 **********************************************************************************************************************/
	public void addPortType(PortType portType){
		portTypes.add(portType);
	}

/**
 * Adds a "message" to the internal ArrayList "messages" which stores the "messages".
 *
 * @param 	message 	An object of type "Message" which shall be added to the internal data storage.
 **********************************************************************************************************************/
	public void addMessage(Message message){
		messages.add(message);
	}

}




