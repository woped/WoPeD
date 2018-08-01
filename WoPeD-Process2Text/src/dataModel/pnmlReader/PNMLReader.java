package dataModel.pnmlReader;

import dataModel.pnmlReader.PetriNet.Arc;
import dataModel.pnmlReader.PetriNet.PetriNet;
import dataModel.pnmlReader.PetriNet.Place;
import dataModel.pnmlReader.PetriNet.Transition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class PNMLReader {
    public PetriNet getPetriNetFromPNMLString(InputStream input) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(input);
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

            //If there is already an arc with the same ID --> new ID neccessary
            //Hashmap to check for existing arcs
            HashMap<String, Arc> arcs = petriNet.getArcs();
            if (arcs.keySet().contains(id)) {
                id = id + "_exists";
            }

            petriNet.addArc(new Arc(id, source, target));
        }
    }

    public static String role;

    private static void extractElements(Document doc, String type, PetriNet petriNet) {
        NodeList list = doc.getElementsByTagName(type);
        for (int i = 0; i < list.getLength(); i++) {
            Node fstNode = list.item(i);
            Element element = (Element) fstNode;
            String id = (element.getAttribute("id").toString());
            NodeList fstNodeElems = fstNode.getChildNodes();
            String operatortype = "none";
            role = "none";

            //-------- CHECK FOR OPERATOR TYPE---------------
            for (int o = 0; o < fstNodeElems.getLength(); o++) {
                Node n1 = fstNodeElems.item(o);
                if (n1.getNodeName().equals("toolspecific")) {
                    NodeList n1NodeElems = n1.getChildNodes();
                    for (int u = 0; u < n1NodeElems.getLength(); u++) {
                        Node n2 = n1NodeElems.item(u);
                        if (n2.getNodeName().equals("operator")) {
                            Element n2Elem = (Element) n2;
                            operatortype = n2Elem.getAttribute("type");
                        }
                    }
                }
            }
            //--------------------------------------------

            //-------- CHECK FOR RESOURCE-----------------
            for (int o = 0; o < fstNodeElems.getLength(); o++) {
                Node n1 = fstNodeElems.item(o);
                if (n1.getNodeName().equals("toolspecific")) {
                    NodeList n1NodeElems = n1.getChildNodes();
                    for (int u = 0; u < n1NodeElems.getLength(); u++) {
                        Node n2 = n1NodeElems.item(u);
                        if (n2.getNodeName().equals("transitionResource")) {
                            Element n2Elem = (Element) n2;
                            role = n2Elem.getAttribute("roleName");
                        }
                    }
                }
            }
            //-------------------------------------------
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
                                petriNet.addElements(new Transition(id, thdNode.getTextContent(), role, operatortype));
                            }
                        }
                    }
                }
            }
        }
    }

    public void test() {
        System.out.println(role);
    }
}