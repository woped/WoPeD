package org.woped.starter.t2p;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PNMLGenerator {

	public PNMLGenerator() {

	}

	public Element root;
	public Document doc;
	public int zaehlerTrans = 1;

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

	public void setPlace(int id) {

		Element place = setElement(doc, root, "place", null, new String[] { "id" }, new String[] { "p" + id });

		Element name = setElement(doc, place, "name", null, null, null);

		setElement(doc, name, "text", "p" + id, null, null); // action.toString()

		Element graphics = setElement(doc, name, "graphics", null, null, null);

		setElement(doc, graphics, "offset", null, new String[] { "x", "y" }, new String[] { "0", "0" });

		Element graphics1 = setElement(doc, place, "graphics", null, null, null);

		setElement(doc, graphics1, "position", null, new String[] { "x", "y" }, new String[] { "0", "0" });

		setElement(doc, graphics1, "dimension", null, new String[] { "x", "y" }, new String[] { "40", "40" });

	}

	public void setArc() {

		int zaehlerArc = 1;
		for (int zaehler = 1; zaehler <= zaehlerTrans; zaehler++) {

			createArcPT(zaehlerArc, zaehler, zaehler);
			zaehlerArc++;
			if (zaehler < zaehlerTrans) {
				createArcTP(zaehlerArc, zaehler + 1, zaehler);
				zaehlerArc++;
			}

		}

	}

	private void createArcTP(int zaehler, int zaehlerP, int zaehlerT) {
		Element arc = setElement(doc, root, "arc", null, new String[] { "id", "source", "target" },
				new String[] { "a" + zaehler, "t" + zaehlerT, "p" + zaehlerP });

		// flow.getSingleObject().getActorFrom().hashCode()
		// flow.getSingleObject().getObject().hashCode()}

		// Action action = flow.getSingleObject();
		// if (action != null) {
		// Actor actorFrom = action.getActorFrom();
		// if (actorFrom != null) {
		// System.out.println("Source found: #" + actorFrom.hashCode());
		// } else {
		// System.out.println("flow is fucked up: " + flow.toString());
		// }
		// } else {
		// System.out.println("flow is fucked up: " + flow.toString());
		// }

		// flow.f_single.f_actorFrom

		Element inscription = setElement(doc, arc, "inscription", null, null, null);

		setElement(doc, inscription, "text", "1", null, null);

		setElement(doc, arc, "graphics", null, null, null);

		Element toolspecific = setElement(doc, arc, "toolspecific", null, new String[] { "tool", "version" },
				new String[] { "WoPeD", "1.0" });

		setElement(doc, toolspecific, "probability", "1.0", null, null);

		setElement(doc, toolspecific, "displayProbabilityOn", "false", null, null);

		setElement(doc, toolspecific, "displayProbabilityPosition", null, new String[] { "x", "y" },
				new String[] { "500.0", "0.0" });
	}

	private void createArcPT(int zaehler, int zaehlerP, int zaehlerT) {
		Element arc = setElement(doc, root, "arc", null, new String[] { "id", "source", "target" },
				new String[] { "a" + zaehler, "p" + zaehlerP, "t" + zaehlerT });

		// flow.getSingleObject().getActorFrom().hashCode()
		// flow.getSingleObject().getObject().hashCode()}

		// Action action = flow.getSingleObject();
		// if (action != null) {
		// Actor actorFrom = action.getActorFrom();
		// if (actorFrom != null) {
		// System.out.println("Source found: #" + actorFrom.hashCode());
		// } else {
		// System.out.println("flow is fucked up: " + flow.toString());
		// }
		// } else {
		// System.out.println("flow is fucked up: " + flow.toString());
		// }

		// flow.f_single.f_actorFrom

		Element inscription = setElement(doc, arc, "inscription", null, null, null);

		setElement(doc, inscription, "text", "1", null, null);

		setElement(doc, arc, "graphics", null, null, null);

		Element toolspecific = setElement(doc, arc, "toolspecific", null, new String[] { "tool", "version" },
				new String[] { "WoPeD", "1.0" });

		setElement(doc, toolspecific, "probability", "1.0", null, null);

		setElement(doc, toolspecific, "displayProbabilityOn", "false", null, null);

		setElement(doc, toolspecific, "displayProbabilityPosition", null, new String[] { "x", "y" },
				new String[] { "500.0", "0.0" });
	}

	public void createDummyPlace() {

		setPlace(new Integer(1));

	}

	public void setTransition(List<String> actions) {

		for (String action : actions) {

			System.out.println("Hashcode Transition:" + action.hashCode());

			if (!action.toString().equals("Action - Dummy Node")) {

				Element transition = setElement(doc, root, "transition", null, new String[] { "id" },
						new String[] { "t" + zaehlerTrans });

				Element name = setElement(doc, transition, "name", null, null, null);

				setElement(doc, name, "text", action, null, null); // action.toString()

				Element graphics = setElement(doc, name, "graphics", null, null, null);

				setElement(doc, graphics, "offset", null, new String[] { "x", "y" }, new String[] { "0", "0" });

				Element graphics1 = setElement(doc, transition, "graphics", null, null, null);

				setElement(doc, graphics1, "position", null, new String[] { "x", "y" }, new String[] { "0", "0" });

				setElement(doc, graphics1, "dimension", null, new String[] { "x", "y" }, new String[] { "40", "40" });

				Element toolspecific = setElement(doc, transition, "toolspecific", null,
						new String[] { "tool", "version" }, new String[] { "WoPeD", "1.0" });

				setElement(doc, toolspecific, "time", "0", null, null);

				setElement(doc, toolspecific, "timeUnit", "1", null, null);

				setElement(doc, toolspecific, "orientation", "1", null, null);

				zaehlerTrans++;

				setPlace(zaehlerTrans);
			}

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

	public InputStream after() {
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source source = new DOMSource(doc);
		Result outputTarget = new StreamResult(outputStream);
		try {
			transformer.transform(source, outputTarget);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayInputStream(outputStream.toByteArray());

		// StreamResult console = new StreamResult(new
		// File("output.pnml").getPath());
		// try {
		// transformer.transform(source, console);
		// } catch (TransformerException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println("\nXML DOM Created Successfully..");
	}

}
