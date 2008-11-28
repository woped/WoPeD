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
			wsdlFileRepresentation = wsdl.readDataFromWSDL("http://www.esther-landes.de/wsdlFiles/example.wsdl");
//			wsdlFileRepresentation = wsdl.readDataFromWSDL("http://www.webservicex.net/globalweather.asmx?WSDL");


			ArrayList<PartnerLinkType> partnerLinkTypes = wsdlFileRepresentation.getPartnerLinkTypes();
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

}