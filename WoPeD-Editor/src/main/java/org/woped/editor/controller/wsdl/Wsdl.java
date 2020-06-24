package org.woped.editor.controller.wsdl;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;



public class Wsdl {

/*
 *	Global Variables
 *****************************************************************************************/
	private WsdlFileRepresentation wsdlFileRepresentation;

/*
 * Methods to read data from the WSDL file.
 *****************************************************************************************/
/**
 * Reads a WSDL file and stores data which is relevant for a BPEL export in an object of type
 * "WsdlFileRepresentation". This object can be read by using other methods.
 *
 * @param 	pathToWsdlFile 	The path to an existing WSDL file. The path can reference a local file
 * 							or a file in the internet (starting with "http" or "ftp".
 * @return 					The object oriented representation of a WSDL file (includes only the
 * 							tags which are relevant for a BPEL export).
 * @throws MalformedURLException	Imported URL pathToWsdlFile is not valid.
 * @throws IOException				Stream to the URL could not be established.
 * @throws XMLStreamException		Imported path to local file is not valid.
 * @throws FileNotFoundException	Error during reading the WSDL file.
 ***************************************************************************************************/
	public WsdlFileRepresentation readDataFromWSDL(String pathToWsdlFile) throws 	MalformedURLException,		// Imported URL pathToWsdlFile is not valid.
																					IOException,				// Stream to the URL could not be established.
																					FileNotFoundException,		// Imported path to local file is not valid.
																					XMLStreamException			// Error during reading the WSDL file.
	{
		InputStream in;
		WsdlFileRepresentation wsdl;

		// Check if the file is stored in the internet (path starts with "ftp" or "http"; "https" is included too as it starts with "http").
		// INFO:
		// 	  It's NOT possible to use: pathToWsdlFile = pathToWsdlFile.toLowerCase(); and then
		//								if ( (pathToWsdlFile.startsWith("http")) || (pathToWsdlFile.startsWith("ftp")) )
		// 	  because in this case the whole string is converted to lower case --> It might be possible that the stream cannot be opened.
		if( pathToWsdlFile.substring(0,6).toLowerCase().equals("https:") ||
			pathToWsdlFile.substring(0,5).toLowerCase().equals("http:")  ||
			pathToWsdlFile.substring(0,4).toLowerCase().equals("ftp:") ){
			// Initialize a stream to an INTERNET FILE.
			URL url = new URL( pathToWsdlFile );

			// Check if URL is correct
			if (!pathToWsdlFile.toLowerCase().endsWith("wsdl")){
				throw new MalformedURLException("File doesn't end with .wsdl");
			}
				in = url.openStream();
				wsdl = this.readDataFromWSDL(in);
			}
		else {
			// Initialize a stream to a LOCAL FILE.
			in = new FileInputStream(pathToWsdlFile);
			// The stream to the WSDL file was initialized. Now read the data from the WSDL file.
			wsdl = this.readDataFromWSDL(in);
		}
		return wsdl;
	}

/*
 * This private method is responsible for reading the WSDL file and storing the extracted data
 * in an object of type "WsdlFileRepresentation".
 *
 * @param 	in 	 An input stream to a WSDL file.
 * @return 		 The object oriented representation of a WSDL file (includes only the
 * 				 tags which are relevant for a BPEL export).
 ***********************************************************************************************/
	private WsdlFileRepresentation readDataFromWSDL(InputStream in) throws XMLStreamException{

/*
 * 		Local Variables
 *****************************************************************************************/
		PartnerLinkType partnerLinkType = new PartnerLinkType();
		Role role = new Role();
		PortType portType = new PortType();
		Operation operation = new Operation();
		Message message = new Message();
		Part part = new Part();

		boolean bool_partnerLinkType = false;
//		boolean bool_partnerLinkType_Role = false;
//		boolean bool_partnerLinkType_Role_PortType = false;
		boolean bool_portType = false;
//		boolean bool_portType_Operation = false;
//		boolean bool_portType_Operation_Input = false;
//		boolean bool_portType_Operation_Output = false;
		boolean bool_message = false;
//		boolean bool_message_part = false;


/*
 * 		Read data from the WSDL file.
 *****************************************************************************************/
		wsdlFileRepresentation = new WsdlFileRepresentation();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader parser = factory.createXMLEventReader(in);

		while (parser.hasNext()) {
			XMLEvent event = parser.nextEvent();
			switch (event.getEventType()) {
			case XMLStreamConstants.START_DOCUMENT:
				break;
			case XMLStreamConstants.END_DOCUMENT:
				parser.close();
				break;


			case XMLStreamConstants.START_ELEMENT:

				StartElement element = event.asStartElement();

				// Check if the current tag is a sub element of the tag "partnerLinkType".
				if (bool_partnerLinkType == true){
					// Check if the current sub element tag is the "role" tag.
					if (element.getName().getLocalPart().equals("role")){
						// bool_partnerLinkType_Role = true;
						role = new Role();
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().toString().equals("name")){
								role.setRoleName(attribute.getValue());
							}
						}
					}
					// Check if the current sub element is the "portType" tag.
					else if (element.getName().getLocalPart().equals("portType")){
//						bool_partnerLinkType_Role_PortType = true;
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().toString().equals("name")){
								role.setPortTypeName(this.getValueWithoutPrefix(attribute.getValue()));
//								operation.setInputMessage(attribute.getValue().substring(pos+1));
//								role.setPortTypeName(attribute.getValue());
							}
						}
					}
				}
				// Check if the current tag is a sub element of the tag "portType".
				else if (bool_portType == true){
					// Check if the current sub element tag is the "operation" tag.
					if (element.getName().getLocalPart().equals("operation")){
//						bool_portType_Operation = true;
						operation = new Operation();
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().toString().equals("name")){
								operation.setOperationName(attribute.getValue());
							}
						}
					}
					// Check if the current sub element tag is the "input" tag.
					else if (element.getName().getLocalPart().equals("input")){
//						bool_portType_Operation_Input = true;
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().toString().equals("message")){
								operation.setInputMessage(this.getValueWithoutPrefix(attribute.getValue()));
							}
						}
					}
					// Check if the current sub element tag is the "output" tag.
					else if (element.getName().getLocalPart().equals("output")){
//						bool_portType_Operation_Output = true;
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().toString().equals("message")){
								operation.setOutputMessage(this.getValueWithoutPrefix(attribute.getValue()));
							}
						}
					}

				}
				// Check if the current tag is a sub element of the tag "message".
				else if (bool_message == true){
					// Check if the current sub element tag is the "part" tag.
					if (element.getName().getLocalPart().equals("part")){
//						bool_message_part = true;
						part = new Part();
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().toString().equals("name")){
								part.setPartName(attribute.getValue());
							}
							else if (attribute.getName().toString().equals("element")){
								part.setPartElement(this.getValueWithoutPrefix(attribute.getValue()));
							}
						}
					}
				}
// 					Boolean variables were checked and the result is that "super element" tags weren't opened.
//					(All bolleans had the value "false".
// 					So now the next open tag is a "super element" tag.
// 					("super element" tags = types, message, portType, binding, port, service or partnerLinkType)
				else{
					// Check if the current tag is the "definitions" tag.
					if (element.getName().getLocalPart().equals("definitions")){
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().toString().equals("targetNamespace")){
								wsdlFileRepresentation.setNamespace(attribute.getValue());
							}
						}
					}
					// Check if the current tag is the "partnerLinkType" tag.
					if (element.getName().getLocalPart().equals("partnerLinkType")){
						bool_partnerLinkType = true;
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							partnerLinkType = new PartnerLinkType();
							if (attribute.getName().toString().equals("name")){
								partnerLinkType.setName(attribute.getValue());
							}
						}
					}
					// Check if the current tag is the "portType" tag.
					else if (element.getName().getLocalPart().equals("portType")){
						bool_portType = true;
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							portType = new PortType();
							if (attribute.getName().toString().equals("name")){
								portType.setPortTypeName(attribute.getValue());
							}
						}
					}
					// Check if the current tag is the "message" tag.
					else if (element.getName().getLocalPart().equals("message")){
						bool_message = true;
						for (Iterator<?> attributes = element.getAttributes(); attributes.hasNext();) {
							Attribute attribute = (Attribute) attributes.next();
							message = new Message();
							if (attribute.getName().toString().equals("name")){
								message.setMessageName(attribute.getValue());
							}
						}
					}
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				break;

// 				A tag has been closed. Depending on the name of the tag a certain boolean variable is set to value "false".
			case XMLStreamConstants.END_ELEMENT:
				if (bool_partnerLinkType == true){
					if (event.asEndElement().getName().getLocalPart().equals("partnerLinkType")){
						wsdlFileRepresentation.addPartnerLinkType(partnerLinkType);
						bool_partnerLinkType = false;
					}
					else if (event.asEndElement().getName().getLocalPart().equals("role")){
						partnerLinkType.addRole(role);
//						bool_partnerLinkType_Role = false;
					}
					else if (event.asEndElement().getName().getLocalPart().equals("portType")){
//						bool_partnerLinkType_Role_PortType = false;
					}
				}
				else if (bool_portType == true){
					if (event.asEndElement().getName().getLocalPart().equals("portType")){
						wsdlFileRepresentation.addPortType(portType);
						bool_portType = false;
					}
					else if (event.asEndElement().getName().getLocalPart().equals("operation")){
						portType.addOperation(operation);
//						bool_portType_Operation = false;
					}
					else if (event.asEndElement().getName().getLocalPart().equals("input")){
//						bool_portType_Operation_Input = false;
					}
					else if (event.asEndElement().getName().getLocalPart().equals("output")){
//						bool_portType_Operation_Output = false;
					}
				}
				else if (bool_message == true){
					if (event.asEndElement().getName().getLocalPart().equals("message")){
						wsdlFileRepresentation.addMessage(message);
						bool_message = false;
					}
					else if (event.asEndElement().getName().getLocalPart().equals("part")){
						message.addPart(part);
//						bool_message_part = false;
					}
				}
				break;

			case XMLStreamConstants.ATTRIBUTE:
				break;
			default:
				break;
			}
		}
		return wsdlFileRepresentation;
	}

	/*
	 * This private method modifies a String so that it is returned without prefix (e.g. "xsd:")
	 *
	 * @param 	value 	 A String which shall be modified.
	 * @return 		 	 A string without prefix.
	 ***********************************************************************************************/
	private String getValueWithoutPrefix(String value){
		char c = ':';
		int pos = value.indexOf(c);
		return value.substring(pos+1);
	}

}

