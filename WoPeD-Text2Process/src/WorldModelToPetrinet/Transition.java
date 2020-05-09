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

    private String roleName, organizationalUnitName = "all", resourceName;
    private String idGateway;
    private int textPositionX = 0;
    private int textPositionY = 0;
    private int transPositionX = 0;
    private int transPositionY = 0;
    private int triggerPositionX = 0;
    private int triggerPositionY = 0;
    private int resourcePositionX = 0;
    private int resourcePositionY = 0;
    private int actorPositionX = 0;
    private int actorPositionY = 0;
    private int dimensionX = 40;
    private int dimensionY = 40;
    private int triggerType = 200;
    private int triggerDimensionX = 24;
    private int triggerDimensionY = 22;
    private int resourceDimensionX = 60;
    private int resourceDimensionY = 22;
    private int operatorType;
    private int orientationCode=1;
    private boolean hasResource = false, isGateway = false; //hasResource bezieht sich auf die Rolle, nicht auf die Ressource

    public Transition(String text, boolean hasResource, boolean isGateway, String originID, IDHandler idHandler) {
        super(originID, idHandler);
        this.text = text;
        this.hasResource = hasResource;
        this.isGateway = isGateway;

        // Set Id of transition
        this.ID = "t" + IDCounter;
    }

    public void setOrientationCode(int orientationCode) {
        this.orientationCode = orientationCode;
    }

    public void setPartOfGateway(int subID,String transID){
        idGateway=transID;
        this.ID=transID+"_op_"+subID;
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

            transitionTag = doc.createElement("transition");
            transitionTag.setAttribute("id", ID);
            doc.appendChild(transitionTag);


            Element name = doc.createElement("name");
            transitionTag.appendChild(name);

            Element text = doc.createElement("text");
            text.appendChild(doc.createTextNode(this.text));
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
                operator.setAttribute("id", idGateway);
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
                transResource.setAttribute("roleName", roleName);
                transResource.setAttribute("organizationalUnitName", organizationalUnitName);

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

