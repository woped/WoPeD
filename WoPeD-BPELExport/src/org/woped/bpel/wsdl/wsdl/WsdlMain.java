package org.woped.bpel.wsdl.wsdl;
import java.util.ArrayList;

import org.woped.bpel.wsdl.Wsdl;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Message;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Operation;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Part;
import org.woped.bpel.wsdl.wsdlFileRepresentation.PartnerLinkType;
import org.woped.bpel.wsdl.wsdlFileRepresentation.PortType;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Role;
import org.woped.bpel.wsdl.wsdlFileRepresentation.WsdlFileRepresentation;

/*
 *	Eine Beispiel-Main Datei, die die Repräsentation einer WSDL-Datei ausliest.
 *	--> Auf jeden Fall wieder zu löschen!
 *		Selbiges gilt für die Datei Test-Files.example.wsdl
 */

public class WsdlMain {

	public static void main(String[] args) {

		Wsdl wsdl = new Wsdl();
		WsdlFileRepresentation wsdlFileRepresentation;
		try {

//			wsdlFileRepresentation = wsdl.readDataFromWSDL("HTTP://www-db.informatik.tu-muenchen.de/UniVerwaltung.wsdl");
			wsdlFileRepresentation = wsdl.readDataFromWSDL("Test-Files/example.wsdl");
//			wsdlFileRepresentation = wsdl.readDataFromWSDL("http://www.webservicex.net/globalweather.asmx?WSDL");


			System.out.println("\nALLE PARTNER LINK TYPES:");
			ArrayList<PartnerLinkType> partnerLinkTypes = wsdlFileRepresentation.getPartnerLinkTypes();
			for(PartnerLinkType partnerLinkType : partnerLinkTypes){
				System.out.println(partnerLinkType.getName());	//oder Dropdowdnlist.addItem(partnerLinkType.getName());
			}

			System.out.println("\nALLE PORT TYPES:");
			ArrayList<PortType> portTypes = wsdlFileRepresentation.getPortTypes();
			for(PortType portType : portTypes){
				System.out.println(portType.getPortTypeName());
			}

			System.out.println("\nALLE MESSAGES:");
			ArrayList<Message> messages = wsdlFileRepresentation.getMessages();
			for(Message message : messages){
				System.out.println(message.getMessageName());
			}

			System.out.println("\n-------------------------------------------------------------------------------------------");

			if (partnerLinkTypes.size() != 0){										// Wichtig, falls keine partner link types vorhanden.
																					// Entsprechend sind auch alle Subelemente nicht über die partner link types erreichbar.
				System.out.println("\nIN DER PARTNER LINK TYPE DROPDOWNLIST WURDE DAS ERSTE ITEM AUSGEWÄHLT.");
				System.out.println("DAZU JETZT ALLE ROLES AUSGEBEN.");

				ArrayList<Role> roles = partnerLinkTypes.get(0).getRoles();
				for(Role role : roles){
					System.out.println(role.getRoleName());
				}


				System.out.println("\nZUR ZWEITEN ROLLE DEN NAMEN DES PORT TYPES AUSGEBEN.");
				System.out.println(partnerLinkTypes.get(0).getRoles().get(0).getPortTypeName());

				System.out.println("\nWEITERE INFOS ZU DIESEM PORT TYPE AUSGEBEN.");
				PortType portType = wsdlFileRepresentation.getPortType( partnerLinkTypes.get(0).getRoles().get(0).getPortTypeName() );
				System.out.println("Name: " + portType.getPortTypeName());
				ArrayList<Operation> operations = portType.getOperations();
				for(Operation operation : operations){
					System.out.println("Operation Name: " + operation.getOperationName());
					System.out.println("Input Message:  " + operation.getInputMessage());
					System.out.println("Output Message: " + operation.getOutputMessage());
				}

				System.out.println("\nWEITERE INFOS ZUR INPUT MESSAGE.");
				Message message = wsdlFileRepresentation.getMessage( wsdlFileRepresentation.getPortType( partnerLinkTypes.get(0).getRoles().get(0).getPortTypeName() ).getOperations().get(0).getInputMessage() );
				System.out.println("Message Name:  " + message.getMessageName());
				for(Part part : message.getParts()){
					System.out.println("Part Element:  " + part.getPartElement());
					System.out.println("Part Name:     " + part.getPartName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}