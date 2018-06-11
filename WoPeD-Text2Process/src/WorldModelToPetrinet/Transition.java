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

public class Transition extends PetriNetElement {

    String textofTrans, roleName, organizationalUnitName ="default", resourceName;
    static int id = 1,  i = 1;
    String transID, idGateway;
    int textPositionX = 0;
    int textPositionY = 0;
    int transPositionX = 0;
    int transPositionY = 0;
    int triggerPositionX = 0;
    int triggerPositionY = 0;
    int resourcePositionX = 0;
    int resourcePositionY = 0;
    int actorPositionX = 0;
    int actorPositionY = 0;
    int dimensionX = 40;
    int dimensionY = 40;
    int triggerType = 200;
    int triggerDimensionX = 24;
    int triggerDimensionY = 22;
    int resourceDimensionX = 60;
    int resourceDimensionY = 22;
    int operatorType;


    int orientationCode=1;
    boolean hasResource = false, isGateway = false;

    public Transition(String text, boolean hasResource, boolean isGateway, String originID) {
        super(originID);
        this.textofTrans = text;
        this.hasResource = hasResource;
        this.isGateway = isGateway;

        // Set Id of transition
        this.transID = "t" + id;
        id++;

        // if Gateway, set ID of Gateway
/*        if (isGateway == true) {

            idGateway = transID + "_op_" + i;
            i++;

            id = i;

        }*/

    }

    public void setOrientationCode(int orientationCode) {
        this.orientationCode = orientationCode;
    }

    public static void resetStaticContext(){
        //TODO replace by ID handler -> Thread Safeness
        id=1;
    }

    public void setPartOfGateway(int subID,String transID){
        idGateway=transID+"_op_"+subID;
        this.transID=transID;
    }

    // getter and setter for role
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // getter and setter for organizational unit
    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public void setOrganizationalUnitName(String organizationalUnitName) {
        this.organizationalUnitName = organizationalUnitName;
    }

    // getter and setter for resource name
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    // getter for ID of transition
    public String getTransID() {
        return transID;
    }

    // getter and setter for type of trigger e.g. trigger type 200 for roles
    public int getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    // Creates the XML of the Transition
    public String toString() {

        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        String transitionXMLStringValue = "";
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();

            Element transitionTag;

            if (isGateway == false) {

                transitionTag = doc.createElement("transition");
                transitionTag.setAttribute("id", transID);
                doc.appendChild(transitionTag);

            } else {

                transitionTag = doc.createElement("transition");
                transitionTag.setAttribute("id", idGateway);
                doc.appendChild(transitionTag);

            }

            Element name = doc.createElement("name");
            transitionTag.appendChild(name);

            Element text = doc.createElement("text");
            text.appendChild(doc.createTextNode(textofTrans));
            name.appendChild(text);

            Element graphicsOfText = doc.createElement("graphics");
            name.appendChild(graphicsOfText);

            Element offsetOfText = doc.createElement("offset");
            offsetOfText.setAttribute("x", "" + textPositionX);
            offsetOfText.setAttribute("y", "" + textPositionY);
            graphicsOfText.appendChild(offsetOfText);

            Element graphicsOfTrans = doc.createElement("graphics");
            transitionTag.appendChild(graphicsOfTrans);

            Element position = doc.createElement("position");
            position.setAttribute("x", "" + transPositionX);
            position.setAttribute("y", "" + transPositionY);
            graphicsOfTrans.appendChild(position);

            Element dimension = doc.createElement("dimension");
            dimension.setAttribute("x", "" + dimensionX);
            dimension.setAttribute("y", "" + dimensionY);
            graphicsOfTrans.appendChild(dimension);

            Element toolSpecific = doc.createElement("toolspecific");
            toolSpecific.setAttribute("tool", "WoPeD");
            toolSpecific.setAttribute("version", "1.0");
            transitionTag.appendChild(toolSpecific);

            if (isGateway == true) {

                Element operator = doc.createElement("operator");
                operator.setAttribute("id", transID);
                operator.setAttribute("type", "" + getOperatorType());
                toolSpecific.appendChild(operator);
            }

            if (hasResource == true) {

                Element trigger = doc.createElement("trigger");
                trigger.setAttribute("id", "");
                trigger.setAttribute("type", "" + triggerType);
                toolSpecific.appendChild(trigger);

                Element graphicsOfTrigger = doc.createElement("graphics");
                trigger.appendChild(graphicsOfTrigger);

                Element positionOfTrigger = doc.createElement("position");
                positionOfTrigger.setAttribute("x", "" + triggerPositionX);
                positionOfTrigger.setAttribute("y", "" + triggerPositionY);
                graphicsOfTrigger.appendChild(positionOfTrigger);

                Element dimensionOfTrigger = doc.createElement("dimension");
                dimensionOfTrigger.setAttribute("x", "" + triggerDimensionX);
                dimensionOfTrigger.setAttribute("y", "" + triggerDimensionY);
                graphicsOfTrigger.appendChild(dimensionOfTrigger);

                Element transResource = doc.createElement("transitionResource");
                transResource.setAttribute("organizationalUnitName", organizationalUnitName);
                transResource.setAttribute("roleName", roleName);
                toolSpecific.appendChild(transResource);

                Element graphicsOfResource = doc.createElement("graphics");
                transResource.appendChild(graphicsOfResource);

                Element positionOfResource = doc.createElement("position");
                positionOfResource.setAttribute("x", "" + resourcePositionX);
                positionOfResource.setAttribute("y", "" + resourcePositionY);
                graphicsOfResource.appendChild(positionOfResource);

                Element dimensionOfResource = doc.createElement("dimension");
                dimensionOfResource.setAttribute("x", "" + resourceDimensionX);
                dimensionOfResource.setAttribute("y", "" + resourceDimensionY);
                graphicsOfResource.appendChild(dimensionOfResource);

            }

            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode("0"));
            toolSpecific.appendChild(time);

            Element timeUnit = doc.createElement("timeUnit");
            timeUnit.appendChild(doc.createTextNode("1"));
            toolSpecific.appendChild(timeUnit);

            Element orientation = doc.createElement("orientation");
            orientation.appendChild(doc.createTextNode(""+orientationCode));
            toolSpecific.appendChild(orientation);

            // Transform Document to XML String
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            // Get the String value of final xml document
            transitionXMLStringValue = writer.getBuffer().toString();

            //TODO Bad Design -> improve
            transitionXMLStringValue = transitionXMLStringValue.substring(transitionXMLStringValue.indexOf('\n')+1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transitionXMLStringValue;

    }

    public int getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(int operatorType) {
        this.operatorType = operatorType;
    }

    public String getIdGateway() {
        return idGateway;
    }

}

