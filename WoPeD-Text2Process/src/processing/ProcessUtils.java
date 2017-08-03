package processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import models.ProcessModel;
import nodes.Cluster;
import nodes.ProcessEdge;
import nodes.ProcessNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

/**
 * 
 * This class provides static methods for drawing process elements.
 */
public abstract class ProcessUtils {

    public final static String ATTR_NAME = "name";
    public final static String ATTR_VALUE = "value";
    public final static String TAG_PROPERTY = "property";
    public final static String TAG_PROPERTIES = "properties";

    public final static String TRANS_PROP_CREDENTIALS = "credentials";

    public enum Orientation {

        TOP, CENTER, RIGHT, LEFT
    }

    public enum Position {

        TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT
    }
  
    
    /**
     * helper method that writes a Properties Map to an xmlNode
     * @param xmlDoc
     * @param nodeToAddTo
     * @param properties
     */
    public static void writeProperties(Document xmlDoc, Element nodeToAddTo, HashMap<String, String> props) {
        // Insert all properties
        for (String key : props.keySet()) {
            Element property = xmlDoc.createElement(TAG_PROPERTY);
            property.setAttribute(ATTR_NAME, key);
            property.setAttribute(ATTR_VALUE, props.get(key));
            nodeToAddTo.appendChild(property);
        }
    }
    
    public static HashMap<String, String> readProperties(XPath xpath, Node node) {
        HashMap<String, String> props = new HashMap<String, String>();
        String query;
        Object res;
        // Get all properties
        query = "./" + TAG_PROPERTY;
        try {
            res = xpath.evaluate(query, node, XPathConstants.NODESET);
        } catch (Exception ex) {
            ex.printStackTrace();
            return props;
        }
        NodeList propertyNodes = (NodeList) res;

        for (int i1 = 0; i1 < propertyNodes.getLength(); i1++) {
            Element property = (Element) propertyNodes.item(i1);

            String key = property.getAttribute(ATTR_NAME);
            String value = property.getAttribute(ATTR_VALUE);

            // Hack to update old ProcessModels with editable sources and targets
            if (key.equals("sourceNode")) {
                key = ProcessEdge.PROP_SOURCENODE;
            }
            if (key.equals("targetNode")) {
                key = ProcessEdge.PROP_TARGETNODE;
            }

            props.put(key, value);
        }
        return props;
    }
    
    /**
     * Returns the default edge class for connecting to process nodes. 
     * @param source
     * @param target
     * @return
     */
    public abstract ProcessEdge createDefaultEdge(ProcessNode source, ProcessNode target);
    
    /**
     * Returns the list of recommendations for a following ProcessNode based
     * @param model The ProcessModel
     * @param node The ProcessNode used for recommandation
     */
    
    public List<Class<? extends ProcessNode>> getNextNodesRecommendation(ProcessModel model, ProcessNode node) {
        // Return empty list by default, might be overwritten by subclasses.
        return new LinkedList<Class<? extends ProcessNode>>();
    }
    
    /**
     * uses the movToFront to arrange the nodes in a non-overlapping manner.
     * This is necessary, as the nodes in the workflow.xml can in an arbitrary ordering.
     */
    public static void sortTopologically(ProcessModel model) {
        //work on a copy as moveToFront changes the list
        ArrayList<ProcessNode> _nodes = new ArrayList<ProcessNode>();
        _nodes.addAll(model.getNodes());

        //moving subProcesses to the front, starting with the largest
        HashSet<ProcessNode> _moved = new HashSet<ProcessNode>();
        ProcessNode _largest;
        do {
            _largest = null;
            //finding the (widest) subProcess, so it is behind the other elements
            for (ProcessNode sub : _nodes) {
                if ((sub instanceof Cluster) && !_moved.contains(sub)) {
                    if (_largest == null) {
                        _largest = sub;
                    }
                }
            }
            if (_largest != null) {
                _moved.add(_largest);
                model.moveToFront(_largest);
            }
        } while (_largest != null);
        //moving all other nodes to the front (due to that, pools are in the back)
        for (ProcessNode p : _nodes) {
            if (!(p instanceof Cluster)) {
                //changes list structure, but does not cause problems!
                model.moveToFront(p);
            }
        }
    }

}