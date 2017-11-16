package dataModel.pnmlReader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataModel.pnmlReader.PetriNet.Arc;
import dataModel.pnmlReader.PetriNet.PetriNet;
import dataModel.pnmlReader.PetriNet.Place;
import dataModel.pnmlReader.PetriNet.Transition;


public class PNMLReader {
	
	public PetriNet getPetriNetFromPNML(File file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			PetriNet petriNet = new PetriNet();
			extractElements(doc, "place", petriNet);
			extractElements(doc, "transition", petriNet);
			extractFlow(doc, petriNet);
	
			return petriNet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public PetriNet getPetriNetFromPNML(String fileS) {
		
		try {
			File file = new File(fileS);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			PetriNet petriNet = new PetriNet();
			extractElements(doc, "place", petriNet);
			extractElements(doc, "transition", petriNet);
			extractFlow(doc, petriNet);
		
			return petriNet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void extractFlow(Document doc, PetriNet petriNet) {
		NodeList list = doc.getElementsByTagName("arc");
		 for (int i = 0; i < list.getLength(); i++) {
			Node fstNode = list.item(i);
			Element arc = (Element) fstNode;
			String id = (arc.getAttribute("id").toString());
			String source = arc.getAttribute("source");
			String target = arc.getAttribute("target");
			petriNet.addArc(new Arc(id, source, target));
		 }	
	}
	
	private static void extractElements(Document doc, String type, PetriNet petriNet) {
		NodeList list = doc.getElementsByTagName(type);
		 for (int i = 0; i < list.getLength(); i++) {
			Node fstNode = list.item(i);
			Element element = (Element) fstNode;
			String id = (element.getAttribute("id").toString());
			NodeList fstNodeElems = fstNode.getChildNodes();
			
			for (int j = 0; j < fstNodeElems.getLength(); j++) {
				Node sndNode = fstNodeElems.item(j);
				NodeList sndNodeElems = sndNode.getChildNodes();
				
				for (int k = 0; k < sndNodeElems.getLength(); k++) {
					Node thdNode = sndNodeElems.item(k);
					if (thdNode.getNodeType() == Node.ELEMENT_NODE) {
						if (thdNode.getNodeName().contains("text")) {
							if (type.equals("place")) {
								petriNet.addElements(new Place(id, thdNode.getTextContent()));
							} else {
								petriNet.addElements(new Transition(id, thdNode.getTextContent()));
							}
						}
					}	
				}
			}	
		 }	
	}

}
