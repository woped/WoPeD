package dataModel.bpmnReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BPMNReader {
	
	private int idCounter;
	private HashMap<String, Integer> keyMap;
	
	public void load() throws ParserConfigurationException, SAXException, IOException {
		
		idCounter = 0;
		keyMap = new HashMap<String, Integer>();
		
		File file = new File("/Users/henrikleopold/Desktop/Sample1.bpmn");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList epcLst = doc.getElementsByTagName("process");
		Node fstNode = epcLst.item(0);

		if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			Element process = (Element) fstNode;
			
			NodeList elems = process.getChildNodes();
			
			for (int j = 0; j < elems.getLength(); j++) {
				Node node = elems.item(j);
				
				// Check node type
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					
					Element elem = (Element) node;
					
					if (elem.getNodeName() == "task") {
						System.out.println(getId(elem.getAttribute("id")));
					}
				}	
			}	
		}
	}
	
	private int getId(String rid) {
		int id;  
		if (keyMap.containsKey(rid)) {
			id = keyMap.get(rid);
		} else {
			id = idCounter;
			idCounter++;
			keyMap.put(rid, id);
		}
		return id;
	}

}
