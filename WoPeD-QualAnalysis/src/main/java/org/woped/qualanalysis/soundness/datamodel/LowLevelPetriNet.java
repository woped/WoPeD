package org.woped.qualanalysis.soundness.datamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class is the reference implementation of the {@code ILowLevelPetriNet} interface.
 */
public class LowLevelPetriNet implements ILowLevelPetriNet {

    private Map<String, PlaceNode> places = new HashMap<>();
    private Map<String, TransitionNode> transitions = new HashMap<>();

    @Override
    public boolean addNode(AbstractNode node) {
        boolean result = false;

        if ( node instanceof TransitionNode ) {
            // true if node does not exist in map
            result = !transitions.containsKey(node.getId());
            getTransitionNode((TransitionNode) node);
        } else if ( node instanceof PlaceNode ) {
            // true if node does not exist in map
            result = !places.containsKey(node.getId());
            getPlaceNode((PlaceNode) node);
        }

        return result;
    }

    @Override
    public boolean addNodes(Set<AbstractNode> nodes) {
        boolean result = true;

        for ( AbstractNode node : nodes ) {
            result = result & addNode(node);
        }
        return result;
    }

    @Override
    public PlaceNode getPlaceNode(PlaceNode node) {
        PlaceNode node2return;
        node2return = places.get(node.getId());

        if ( node2return == null ) {
            // add the provided node to map
            node2return = node;
            places.put(node.getId(), node);
        }

        return node2return;
    }

    @Override
    public SortedSet<PlaceNode> getPlaces() {
        SortedSet<PlaceNode> places = new TreeSet<>(new PlaceNodeComparator());
        for ( String key : this.places.keySet() ) {
            places.add(this.places.get(key));
        }
        return places;
    }

    @Override
    public TransitionNode getTransitionNode(TransitionNode node) {
        TransitionNode node2return;
        node2return = transitions.get(node.getId());

        if ( node2return == null ) {
            // add the provided node to map
            node2return = node;
            transitions.put(node.getId(), node);
        }

        return node2return;
    }

    @Override
    public Set<TransitionNode> getTransitions() {
        Set<TransitionNode> transitions = new HashSet<TransitionNode>();
        for ( String key : this.transitions.keySet() ) {
            transitions.add(this.transitions.get(key));
        }
        return transitions;
    }

    @Override
    public Set<AbstractNode> getAllContainedNodes() {
        Set<AbstractNode> set = new HashSet<AbstractNode>();
        set.addAll(places.values());
        set.addAll(transitions.values());
        return set;
    }
}
