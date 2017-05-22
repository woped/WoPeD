package nodes;

import java.util.HashMap;

import java.util.StringTokenizer;


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

import processing.ProcessUtils;

public abstract class ProcessEdge extends ProcessObject {

    /**
     * Serialization properties
     */
    public final static String TAG_EDGE = "edge";
    public final static String PROP_SOURCENODE = "#sourceNode";
    public final static String PROP_TARGETNODE = "#targetNode";
    /** The list of intermediate routing points in format x1,y1+x2,y2+x3,y3 */
    public final static String PROP_POINTS = "points";
    /** The color of the arc in jawa.awt.Color */
    public final static String PROP_COLOR_ARC = "color_arc";
    /** The label of the edge */
    public final static String PROP_LABEL = "label";
    /** The offset of the label from the source (in percent as double) */
    public final static String PROP_LABELOFFSET = "#label_offset";
    /** The source docking point (offset from source node) */
    public final static String PROP_SOURCE_DOCKPOINT = "#source_dockpoint";
    /** The target docking point (offset from target node) */
    public final static String PROP_TARGET_DOCKPOINT = "#target_dockpoint";
    private ProcessNode source;
    private ProcessNode target;

    public ProcessEdge() {
        this(null, null);
        initializeProperties();
    }

    public ProcessEdge(ProcessNode source, ProcessNode target) {
        super();
        setSource(source);
        setTarget(target);
        initializeProperties();
    }

    public static ProcessEdge newInstanceFromSerialization(Node XMLnode, HashMap<String, ProcessNode> nodesInModel) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            // Create flows according to type
            HashMap<String, String> props = ProcessUtils.readProperties(xpath, XMLnode);
            // Try to instantiate corresponding process edge
            Object o = Class.forName(props.get(ProcessObject.PROP_CLASS_TYPE)).newInstance();
            if (!(o instanceof ProcessEdge)) {
                throw new Exception("Illegal node found!");
            }
            ProcessEdge pn = (ProcessEdge) o;
            // Add properties to node
            for (String key : props.keySet()) {
                pn.setProperty(key, props.get(key));
            }
            // Set source and target
            pn.setSource(nodesInModel.get(props.get(ProcessEdge.PROP_SOURCENODE)));
            pn.setTarget(nodesInModel.get(props.get(ProcessEdge.PROP_TARGETNODE)));
            return pn;
    }

    private void initializeProperties() {
        setProperty(PROP_SOURCE_DOCKPOINT, "");
        setProperty(PROP_TARGET_DOCKPOINT, "");
        setProperty(PROP_POINTS, "");
        setProperty(PROP_LABEL, "");
        setProperty(PROP_LABELOFFSET, "0.5");
    }

    public ProcessNode getSource() {
        return source;
    }

    public void setSource(ProcessNode source) {
        if (source != null) {
            setProperty(PROP_SOURCENODE, source.getId());
        }
        this.source = source;
    }

    public ProcessNode getTarget() {
        return target;
    }

    public void setTarget(ProcessNode target) {
        if (target != null) {
            setProperty(PROP_TARGETNODE, target.getId());
        }
        this.target = target;
    }

    public void setLabel(String label) {
        setProperty(PROP_LABEL, label);
    }

    public String getLabel() {
        return getProperty(PROP_LABEL);
    }

	public double getLabelOffset() {
		return Double.parseDouble(getProperty(ProcessEdge.PROP_LABELOFFSET));
	}
	
	public void setLabelOffset(double offset) {
		if(offset > 1.0) {
			offset = 1.0;
		}else if(offset < 0.0) {
			offset = 0.0;
		}
		setProperty(ProcessEdge.PROP_LABELOFFSET, Double.toString(offset));
	}

    /**
     * Returns if this edge supports docking (might be overwritten by
     * subclasses).
     * @return
     */
    protected boolean isDockingSupported() {
        return false;
    }
    /**
     * removes all routing points of that edge
     */
    public void clearRoutingPoints() {
        setProperty(PROP_POINTS, "");
        updateCache();
    }

    /**
     * Removes a routing point at a specific location (0=first index)
     * @param p
     * @param pos
     */
    public void removeRoutingPoint(int pos) {
        String points = "";

        // Tokenize points
        StringTokenizer st = new StringTokenizer(getProperty(PROP_POINTS), "+");
        int ipos = 0;
        while (st.hasMoreTokens()) {
            if (ipos != pos-1) {
                points += st.nextToken() + "+";
            } else {
                st.nextToken();
            }
            ipos++;
        }
        if (points.length() > 0) {
            points = points.substring(0, points.length() - 1);
        }
        // Set changed property
        setProperty(PROP_POINTS, points);
        // Update cache
        updateCache();
    }
    protected synchronized void updateCache() {

        // Parse all points property
        StringTokenizer st = new StringTokenizer(getProperty(PROP_POINTS), "+");
        while (st.hasMoreTokens()) {
            String pos = st.nextToken();
            try {
                StringTokenizer st1 = new StringTokenizer(pos, ",");
//                int x = Integer.parseInt(st1.nextToken());
 //               int y = Integer.parseInt(st1.nextToken());
            } catch (Exception e) {
            }
        }
    }

    /**
     * resets the target docking point offset and returns to the usage 
     * of automatic determination
     */
    public void clearSourceDockPointOffset() {
        setProperty(PROP_SOURCE_DOCKPOINT, "");
    }

    /**
     * resets the target docking point offset and returns to the usage 
     * of automatic determination
     */
    public void clearTargetDockPointOffset() {
        setProperty(PROP_TARGET_DOCKPOINT, "");
    }

    /**
     * Determines whether the source arrow should be outlined or not. Might
     * be overwritten by subclasses.
     * @return
     */
    public boolean isOutlineSourceArrow() {
        return false;
    }

    /**
     * Determines whether the target arrow should be outlined or not. Might
     * be overwritten by subclasses.
     * @return
     */
    public boolean isOutlineTargetArrow() {
        return false;
    }

    /**
     * Copies the properties from this ProcessEdge, without ID and Type.
     * @see clonePropertiesFrom()
     * @param edge
     */
    public void copyPropertiesFrom(ProcessEdge edge) {
        for (String key : edge.getPropertyKeys()) {
            if (key.equals(PROP_ID) || key.equals(PROP_CLASS_TYPE)) {
                continue;
            }
            this.setProperty(key, edge.getProperty(key));
        }
    }

    /**
     * Clones all properties from this ProcessEdge, including ID and TYPE.
     * @see copyPropertiesFrom()
     * @param edge
     */
    public void clonePropertiesFrom(ProcessEdge edge) {
        for (String key : edge.getPropertyKeys()) {
            this.setProperty(key, edge.getProperty(key));
        }
    }

    @Override
    public ProcessEdge clone() {
        ProcessEdge newEdge = (ProcessEdge) super.clone();
        //copy properties
        newEdge.setSource(this.getSource());
        newEdge.setTarget(this.getTarget());
        return newEdge;
    }

    @Override
    protected String getXmlTag() {
        return TAG_EDGE;
    }

    @Override
    public String getName() {
        return getLabel();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " (" + getSource() + " -> " + getTarget() + ")";
    }
}