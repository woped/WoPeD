package org.woped.qualanalysis.soundness.datamodel;

import java.util.Set;
import java.util.SortedSet;

import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;

/**
 * This interface represents a classic petri net.
 * <p>
 * It consists of a set of places, a set of transitions and a weight function.
 * <p>
 * The flow relation between the places and the transitions is contained in the nodes itself
 * in the predecessor and successor properties.
 * <p>
 * The weight function returns the weight of an direct connection between two nodes or {@code 0} if no such
 * connection exists.
 */
public interface ILowLevelPetriNet extends INodeNet<AbstractNode> {

    /**
     * @param node adds the provide node to this low level petri net
     * @return true if node was added.
     */
    boolean addNode(AbstractNode node);

    /**
     * adds all nodes of the provided set of nodes.
     *
     * @param nodes to be added.
     * @return true if all nodes were added
     */
    boolean addNodes(Set<AbstractNode> nodes);

    /**
     * if a node with the id of the provided node already exists the existing node will returned. if the provided node does not exist, the provided node will
     * returned
     *
     * @param node example.
     * @return node from map.
     */
    PlaceNode getPlaceNode(PlaceNode node);

    /**
     * Gets the set of places sorted by their id in short lex order.
     *
     * @return the set of places of the net.
     */
    SortedSet<PlaceNode> getPlaces();

    /**
     * if a node with the id of the provided node already exists the existing node will returned. if the provided node does not exist, the provided node will
     * returned
     *
     * @param node example.
     * @return node from map.
     */
    TransitionNode getTransitionNode(TransitionNode node);

    /**
     * @return the transitionNodes
     */
    Set<TransitionNode> getTransitions();
}
