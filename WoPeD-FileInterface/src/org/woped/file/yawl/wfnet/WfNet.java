/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl.wfnet;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Christian Klassen
 */
public class WfNet {

    /**
     * A hash map with the id value of the WfNetNode as key
     */
    private HashMap<String, WfNetNode> nodes = new HashMap<String, WfNetNode>();
    /**
     * A hash map with an external search criterion. In case there is no
     * external criterion the id of the node will be used as key.
     */
    private HashMap<String, WfNetNode> nodesExt = new HashMap<String, WfNetNode>();

    /////////////////////////////////////////////////////////////
    // Methods
    /////////////////////////////////////////////////////////////
    
    
    /**
     * Create a new Transition object, using the supplied string
     * as 'internal' as well as 'external' id.
     * @param id
     * @return the generated Transition object
     */
    public Place createPlace() {
        Place p = new Place(getNextUnusedId("p"), this);
        return p;
    }
    
    
    public Place createPlace(String id) {
        Place p = new Place(id, this);
        return p;
    }
    
    
    /**
     * Create a new Place object with a newly generated id value,
     * using it for the 'internal' id of the Place as well as
     * the 'external' (= second search criterion)
     * @return the generated Place object
     */
    public Place createAndAddPlace() {
        return createAndAddPlace(getNextUnusedId("p"));
    }

    /**
     * Create a new Place object, using the supplied string
     * as 'internal' as well as 'external' id.
     * @param id
     * @return the generated Place object
     */
    public Place createAndAddPlace(String id) {
        Place p = new Place(id, this);
        addNode(p, id);
        return p;
    }
    
    /**
     * Create a new Place object, generate a new
     * 'internal' id and use the supplied String as
     * the 'external' id
     * @param external_id
     * @return the generated Place object
     */
    public Place createAndAddPlaceExt(String external_id) {
        Place p = new Place(getNextUnusedId("p"), this);
        addNode(p, external_id);
        return p;
    }

    /**
     * Create a new Transition object with a newly generated id value,
     * using it for the 'internal' id of the Transition as well as
     * the 'external' (= second search criterion)
     * @return the generated Transition object
     */
    public Transition createAndAddTransition() {
        return createAndAddTransition(getNextUnusedId("t"));
    }

    /**
     * Create a new Transition object and generate a new id
     * as 'internal' as well as 'external' id.
     * @return the generated Transition object
     */
    public Transition createTransition() {
        Transition t = new Transition(getNextUnusedId("t"), this);
        return t;
    }
    
    /**
     * Create a new Transition object, using the supplied string
     * as 'internal' as well as 'external' id.
     * @param id
     * @return the generated Transition object
     */
    public Transition createTransition(String id) {
        Transition t = new Transition(id, this);
        return t;
    }
    
    /**
     * Create a new Transition object, using the supplied string
     * as 'internal' as well as 'external' id. Add the new
     * Transition to the HashMaps
     * @param id
     * @return the generated Transition object
     */
    public Transition createAndAddTransition(String id) {
        Transition t = new Transition(id, this);
        addNode(t, id);
        return t;
    }
    
    /**
     * Create a new Transition object, generate a new
     * 'internal' id and use the supplied String as
     * the 'external' id
     * @param external_id
     * @return the generated Transition object
     */
    public Transition createAndAddTransitionExt(String external_id) {
        Transition t = new Transition(getNextUnusedId("t"), this);
        addNode(t, external_id);
        return t;
    }

    private void addNode(WfNetNode node, String idExt) {
        if (this.nodes.containsKey(node.getId()) ||
                this.nodesExt.containsKey(idExt)) {
            // TODO: Implement Error handling
            // throw new WfNetException();
        }
        
        this.nodes.put(node.getId(), node);
        this.nodesExt.put(idExt, node);
    }
    
    public void addNode(WfNetNode node) {
        addNode(node, node.id);
    }
    
    public WfNetNode findNode(String id) {
        return this.nodes.get(id);
    }
    
    public WfNetNode findNodeExt(String idExt) {
        return this.nodesExt.get(idExt);
    }
    
    public Collection<WfNetNode> getNodes() {
        return this.nodes.values();
    }
    
    /**
     * Replace the existing id of the node with the new id.
     * If the node is already contained in the this.nodes HashMap 
     * it will be deleted and re-inserted into it.
     * @param node
     * @param newId 
     */
    public void setNodeId(WfNetNode node, String newId) {
        
        if (this.nodes.containsKey(node.id)) {
            this.nodes.remove(node.id);
            this.nodes.put(newId, node);
        }
        
        node.id = newId;
        
    }
    
    /**
     * Completely remove the node from the workflow net.
     * @param node The node to be removed.
     */
    public void removeNode(WfNetNode node) {
        if (!this.nodes.containsKey(node.id)) return;
        
        this.nodes.remove(node.id);
        
        // remove the node from the external id HashMap
        String extKey = null;
        for (String key : this.nodesExt.keySet()) {
            if (this.nodesExt.get(key).equals(node)) {
                extKey = key;
            }
        }
        if (extKey != null) this.nodesExt.remove(extKey);
        
        // remove links to other nodes
        while(node.getInputNodes().size() > 0) {
            node.removeInputNode(node.getInputNodes().get(0));
        }
        while(node.getOutputNodes().size() > 0) {
            node.removeOutputNode(node.getOutputNodes().get(0));
        }

    }
    
    private HashMap<String, Integer> idCounterMap = new HashMap<String, Integer>();

    /**
     * Helper method that returns an id value (using the supplied prefix) that
     * does not yet exist in the this.nodes HashMap
     *
     * @param prefix
     * @return a String that consists of the prefix + an integer value
     */
    public String getNextUnusedId(String prefix) {
        if (!idCounterMap.containsKey(prefix)) {
            idCounterMap.put(prefix, 0);
        }

        int n = idCounterMap.get(prefix) + 1;

        String id = prefix + n;
        while (similarIdExists(id)) {
            n++;
            id = prefix + n;
        }
        idCounterMap.put(prefix, n);

        return id;
    }
    
    /**
     * Ensure, that the supplied id value isn't already used
     * by any existing place or transition and is also not
     * the beginning of any existing id value.
     * @param id the id value to test
     * @return 
     */
    public boolean similarIdExists(String id) {
        for (WfNetNode node : nodes.values()) {
            if(node.getId().startsWith(id)) {
                return true;
            }
        }
        return false;
    }
    
    private int arcId = 0;
    /**
     * Create a new id value for an arc. Ensure that
     * the id isn't used by any transitions or places.
     * @return 
     */
    public String getNewArcId() {
        arcId++;
        String id = "a" + arcId;
        while (similarIdExists(id)) {
            arcId++;
            id = "a" + arcId;
        }
        
        return id;
    }
}
