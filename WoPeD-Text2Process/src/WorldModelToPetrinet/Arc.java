package WorldModelToPetrinet;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Arc extends PetriNetElement{

    String arcID;
    static int id = 1;
    double offsetX = 500.0, offsetY = -12.0;
    String source, target, text = "1";

    public Arc(String source, String target, String originID){
        super(originID);
        arcID = "a" + id;
        id++;

        this.source = source;
        this.target = target;

    }

    public static void resetStaticContext(){
        //TODO replace by ID handler -> Thread Safeness
        id=1;
    }

    public String toString(){

        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        String arcXMLStringValue = "";
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();

            Element arcTag = doc.createElement("arc");
            arcTag.setAttribute("id", arcID);
            arcTag.setAttribute("source", source);
            arcTag.setAttribute("target", target);
            doc.appendChild(arcTag);

            Element inscription = doc.createElement("inscription");
            arcTag.appendChild(inscription);

            Element textofArc = doc.createElement("text");
            textofArc.appendChild(doc.createTextNode(text));
            inscription.appendChild(textofArc);

            Element graphics = doc.createElement("graphics");
            inscription.appendChild(graphics);

            Element offset = doc.createElement("offset");
            offset.setAttribute("x", ""+offsetX);
            offset.setAttribute("y", ""+offsetY);
            graphics.appendChild(offset);

            Element toolSpecific = doc.createElement("toolspecific");
            toolSpecific.setAttribute("tool", "WoPeD");
            toolSpecific.setAttribute("version", "1.0");
            arcTag.appendChild(toolSpecific);

            Element probability = doc.createElement("probability");
            probability.appendChild(doc.createTextNode("1.0"));
            toolSpecific.appendChild(probability);

            Element displayProbabilityOn = doc.createElement("displayProbabilityOn");
            displayProbabilityOn.appendChild(doc.createTextNode("false"));
            toolSpecific.appendChild(displayProbabilityOn);

            Element displayProbabilityPosition = doc.createElement("displayProbabilityPosition");
            displayProbabilityPosition.setAttribute("x", "500.0");
            displayProbabilityPosition.setAttribute("y", "12.0");
            toolSpecific.appendChild(displayProbabilityPosition);



            // Transform Document to XML String
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            // Get the String value of final xml document
            arcXMLStringValue = writer.getBuffer().toString();

            //TODO Bad Design -> improve
            arcXMLStringValue = arcXMLStringValue.substring(arcXMLStringValue.indexOf('\n')+1);


        } catch (Exception e) {
            e.printStackTrace();
        }



        return arcXMLStringValue;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

}
