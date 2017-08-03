package nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class Cluster extends ProcessNode {
	
	/** Property for the contained Nodes of this Cluster */
    public final static String PROP_CONTAINED_NODES = "#nodes";
    /** Marks if this cluster is currently collapsed */
    public final static String PROP_COLLAPSED = "collapsed";
    /** A list of contained ProcessNodes */
    private List<ProcessNode> containedNodes = new LinkedList<ProcessNode>();

    public Cluster() {
        super();
        setProperty(PROP_COLLAPSED, "FALSE");
        //setPropertyEditor(PROP_COLLAPSED, new BooleanPropertyEditor());
        updateProperties();
    }

    /**
     * Returns if this cluster is collapseable. Might be overwritten by
     * subclasses.
     * @return
     */
    public boolean isCollapseable() {
        return true;
    }

    public boolean isCollapsed() {
        return getProperty(PROP_COLLAPSED).equalsIgnoreCase("TRUE");
    }

    @Override
    public void setProperty(String key, String value) {
        // Check if key=PROP_CONTAINED_NODES
        List<String> removeList = new LinkedList<String>();
        if (key.equals(PROP_CONTAINED_NODES)) {
            // Take additional care of removing nodes
            String oldValue = getProperty(key);
            if (oldValue != null) {
                // Figure out nodes that are in oldValue but not in value
                StringTokenizer st1 = new StringTokenizer(oldValue, ";");
                while (st1.hasMoreElements()) {
                    StringTokenizer st2 = new StringTokenizer(value, ";");
                    String currentValue1 = st1.nextToken();
                    boolean found = false;
                    while (st2.hasMoreElements()) {
                        if (currentValue1.equals(st2.nextElement())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        removeList.add(currentValue1);
                    }
                }
                // Remove deprecated nodes from containment
                for (String id : removeList) {
                    ProcessNode remNode = null;
                    for (ProcessNode n : getProcessNodes()) {
                        if (n.getProperty(ProcessObject.PROP_ID).equals(id)) {
                            remNode = n;
                        }
                    }
                    if (remNode != null) {
                        containedNodes.remove(remNode);
                    }
                }
            }
        }
        super.setProperty(key, value);
    }

    /**
     * Updates the property representation of the contained nodes.
     */
    private synchronized void updateProperties() {
        String result = "";
        for (ProcessNode node : containedNodes) {
            assert node != null;
            assert node.getProperty(PROP_ID) != null;
            result += node.getProperty(PROP_ID) + ";";
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        setProperty(PROP_CONTAINED_NODES, result);
    }

    /**
     * Adds a ProcessNode to this Cluster.
     * @param n
     */
    public void addProcessNode(ProcessNode n) {
        if (n == null) {
            return;
        }
        if (!containedNodes.contains(n)) {
            containedNodes.add(n);
            updateProperties();
        }
    }

    /**
     * Removes a ProcessNode from this Cluster.
     * @param n
     */
    public void removeProcessNode(ProcessNode n) {
        if (containedNodes.contains(n)) {
            containedNodes.remove(n);
            updateProperties();
        }
    }

    /**
     * Returns the set of ProcessNodes contained in this Cluster.
     * @return final Set<ProcessNode>
     */
    public List<ProcessNode> getProcessNodes() {
        final List<ProcessNode> NODES = containedNodes;
        return NODES;
    }

    public List<ProcessNode> getProcessNodesRecursivly() {
        List<ProcessNode> nodes = new ArrayList<ProcessNode>();

        for (ProcessNode n : this.containedNodes) {
            nodes.add(n);

            if (n.isCluster()) {
                nodes.addAll(((Cluster) n).getProcessNodesRecursivly());
            }
        }

        return nodes;
    }


    /**
     * Sets the Nodeset as the ProcessNodes of the Cluster
     * @param NODES
     */
    public void setProcessNodes(Collection<ProcessNode> nodes) {
        //use add to ensure constraints and encapsulation
        clearContainment();
        for (ProcessNode givenNode : nodes) {
            addProcessNode(givenNode);
        }
    }

    /**
     * Returns whether a certain ProcessNode is contained inside this Cluster.
     * @param n
     * @return
     */
    public boolean isContained(ProcessNode n) {
        for (ProcessNode node : getProcessNodes()) {
            if (node == n) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the containment set. Usually required after creating a
     * to initialize containments.
     * @param nodes
     */
    public void updateContainments(List<ProcessNode> nodes) {
        // Clear list
        containedNodes.clear();
        for (ProcessNode n : nodes) {
            // Skip self
            if (n == this) {
                continue;
            }
           
        }
    }

    public void clearContainment() {
        containedNodes.clear();
        updateProperties();
    }

    

    @Override
    public boolean isCluster() {
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " (" + getText() + ")";
    }

    @Override
    public Cluster clone() {
        Cluster copy = (Cluster) super.clone();
        copy.containedNodes = new LinkedList<ProcessNode>(containedNodes);
        return copy;
    }
    
}
