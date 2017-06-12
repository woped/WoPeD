package org.woped.swt;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import worldModel.Action;
import worldModel.Actor;
import worldModel.Flow;

public class Ausgabe {

	public Element root;
	public Document doc;

	public void init() {

		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder icBuilder = null;
		try {
			icBuilder = icFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc = icBuilder.newDocument();
		Element mainRootElement = doc.createElement("pnml");
		doc.appendChild(mainRootElement);

		root = doc.createElement("net");
		root.setAttribute("id", "noID");
		root.setAttribute("type", "https://www2.informatik.hu-berlin.de/top/pntd/ptNetb");
		mainRootElement.appendChild(root);

	}

	public void setPlace(List<Actor> actions) {
		int zaehler = 1;
		for (Actor action : actions) {

	
			Element place = setElement(doc, root, "place", null, new String[] { "id" }, new String[] { "p" + zaehler });

			Element name = setElement(doc, place, "name", null, null, null);

			setElement(doc, name, "text", "p1", null, null); // action.toString()

			Element graphics = setElement(doc, name, "graphics", null, null, null);

			setElement(doc, graphics, "offset", null, new String[] { "x", "y" }, new String[] { "0", "0" });

			Element graphics1 = setElement(doc, place, "graphics", null, null, null);

			setElement(doc, graphics1, "position", null, new String[] { "x", "y" }, new String[] { "0", "0" });

			setElement(doc, graphics1, "dimension", null, new String[] { "x", "y" }, new String[] { "40", "40" });

			zaehler++;

		}

	}

	public void setArc(List<Flow> flows) {
		int zaehler = 1;

		for (Flow flow : flows) {



			Element arc = setElement(doc, root, "arc", null, new String[] { "id", "source", "target" },
					new String[] { "a" + zaehler, "p1", "t1" });
			
						

			Element inscription = setElement(doc, arc, "inscription", null, null, null);

			setElement(doc, inscription, "text", "1", null, null); // action.toString()

			setElement(doc, arc, "graphics", null, null, null);

			Element toolspecific = setElement(doc, arc, "toolspecific", null, new String[] { "tool", "version" },
					new String[] { "WoPeD", "1.0" });

			setElement(doc, toolspecific, "probability", "1.0", null, null);

			setElement(doc, toolspecific, "displayProbabilityOn", "false", null, null);

			setElement(doc, toolspecific, "displayProbabilityPosition", null, new String[] { "x", "y" },
					new String[] { "500.0", "0.0" });

			zaehler++;

		}

	}

	public void setTransition(List<Action> actions) {
		int zaehler = 1;


		for (Action action : actions) {

			Element transition = setElement(doc, root, "transition", null, new String[] { "id" },
					new String[] { "t" + zaehler });

			Element name = setElement(doc, transition, "name", null, null, null);

			setElement(doc, name, "text", "test", null, null); // action.toString()

			Element graphics = setElement(doc, name, "graphics", null, null, null);

			setElement(doc, graphics, "offset", null, new String[] { "x", "y" }, new String[] { "0", "0" });

			Element graphics1 = setElement(doc, transition, "graphics", null, null, null);

			setElement(doc, graphics1, "position", null, new String[] { "x", "y" }, new String[] { "0", "0" });

			setElement(doc, graphics1, "dimension", null, new String[] { "x", "y" }, new String[] { "40", "40" });

			Element toolspecific = setElement(doc, transition, "toolspecific", null, new String[] { "tool", "version" },
					new String[] { "WoPeD", "1.0" });

			setElement(doc, toolspecific, "time", "0", null, null);

			setElement(doc, toolspecific, "timeUnit", "1", null, null);

			setElement(doc, toolspecific, "orientation", "1", null, null);

			zaehler++;

		}

	}

	private static Element setElement(Document doc, Element parent, String name, String wert, String[] attributname,
			String[] attributwert) {
		Element element = doc.createElement(name);

		if (wert != null) {
			element.appendChild(doc.createTextNode(wert));
		}

		if (attributname != null && attributwert != null) {

			for (int i = 0; i < attributname.length; i++) {
				element.setAttribute(attributname[i], attributwert[i]);
			}

		}

		parent.appendChild(element);

		return element;
	}

	public void after() {
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult console = new StreamResult(new File("output.pnml").getPath());
		try {
			transformer.transform(source, console);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("\nXML DOM Created Successfully..");
	}

}
