package org.woped.qualanalysis.soundness.datamodel;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * this class represents a low level petri net
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */ 
public class LowLevelPetriNet {
    // declaration

    private Map<String, PlaceNode> places = new TreeMap<String, PlaceNode>(new PlaceSort());
    private Map<String, TransitionNode> transitions = new TreeMap<String, TransitionNode>();

    public LowLevelPetriNet() {

    }

    /**
     * 
     * @param node adds the provide node to this low level petri net
     * @return true if node was added.
     */
    public boolean addNode(AbstractNode node) {
        boolean result = false;

        if (node instanceof TransitionNode) {
            // true if node does not exist in map
            result = !transitions.containsKey(node.getId());
            getTransitionNode((TransitionNode) node);
        } else {
            if (node instanceof PlaceNode) {
                // true if node does not exist in map
                result = !places.containsKey(node.getId());
                getPlaceNode((PlaceNode) node);
            }
        }
        return result;

    }

    /**
     * adds all nodes of the provided set of nodes.
     * 
     * @param nodes to be added.
     * @return true
     */
    public boolean addNodes(Set<AbstractNode> nodes) {
        boolean result = true;

        for (AbstractNode node : nodes) {
            result = result & addNode(node);
        }
        return true;
    }

    /**
     * if a node with the id of the provided node already exists the existing node will returned. if the provided node does not exist, the provided node will
     * returned
     * 
     * @param node example.
     * @return node from map.
     */
    public PlaceNode getPlaceNode(PlaceNode node) {
        PlaceNode node2return;
        node2return = places.get(node.getId());

        if (node2return == null) {
            // add the provided node to map
            node2return = node;
            places.put(node.getId(), node);
        }

        return node2return;
    }

    /**
     * @return the placeNodes as an array
     */
    public PlaceNode[] getPlaces() {
        PlaceNode[] nodes = new PlaceNode[this.places.size()];
        Iterator<Entry<String, PlaceNode>> iter = places.entrySet().iterator();

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = iter.next().getValue();
        }
        return nodes;
    }

    /**
     * if a node with the id of the provided node already exists the existing node will returned. if the provided node does not exist, the provided node will
     * returned
     * 
     * @param node example.
     * @return node from map.
     */
    public TransitionNode getTransitionNode(TransitionNode node) {
        TransitionNode node2return;
        node2return = transitions.get(node.getId());

        if (node2return == null) {
            // add the provided node to map
            node2return = node;
            transitions.put(node.getId(), node);
        }

        return node2return;
    }

    /**
     * @return the transitionNodes as an array
     */
    public TransitionNode[] getTransitions() {
        TransitionNode[] nodes = new TransitionNode[this.transitions.size()];
        Iterator<Entry<String, TransitionNode>> iter = transitions.entrySet().iterator();

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = iter.next().getValue();
        }
        return nodes;

    }
}
