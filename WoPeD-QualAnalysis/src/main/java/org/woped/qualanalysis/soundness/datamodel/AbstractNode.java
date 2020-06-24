package org.woped.qualanalysis.soundness.datamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.generic.INode;

/**
 * This abstract class provides the basic functionality of a node in a low level petri net.
 * <p>
 * It is only possible to add elements of this class to a {@link ILowLevelPetriNet}.
 * That's why all nodes (places, transitions) have to extend this class.
 */
public abstract class AbstractNode implements INode<AbstractNode> {

    private final Set<AbstractNode> predecessorNodes = new HashSet<>();
    private final Set<AbstractNode> successorNodes = new HashSet<>();
    private final Map<AbstractNode, Integer> incomingWeights = new HashMap<>();
    private final Map<AbstractNode, Integer> outgoingWeights = new HashMap<>();

    private final String id;
    private final String originId;
    private final String name;

    /**
     * Creates a new node which represents an {@link org.woped.core.model.petrinet.AbstractPetriNetElementModel}.
     *
     * @param id       the id of the node
     * @param name     the name of the new node
     * @param originId the id of the petrinet element, which is source for this node
     */
    protected AbstractNode(String id, String name, String originId) {
        this.id = id;
        this.name = name;
        this.originId = originId;
    }

    /**
     * Adds a new successor to this node with the provided weight.
     *
     * @param successor the successor to add
     * @param weight    the weight of the new connection
     * @return true if the provided node has been added, otherwise false
     */
    protected boolean addSuccessorNode(AbstractNode successor, int weight) {

        if ( weight <= 0 ) throw new IllegalArgumentException("Weight has to be larger than zero");

        if ( !successor.addPredecessorNode(this, weight) ) {
            return false;
        }

        outgoingWeights.put(successor, weight);

        return successorNodes.contains(successor) || successorNodes.add(successor);
    }

    /**
     * Adds a new predecessor to this node with the provided weight.
     *
     * @param preNode the predecessor to add
     * @param weight  the weight of the new connection
     * @return true if the provided node has been added, otherwise false
     * @throws IllegalArgumentException if weight is less or equal zero
     */
    public boolean addPredecessorNode(AbstractNode preNode, int weight) throws IllegalArgumentException {

        if ( weight <= 0 ) throw new IllegalArgumentException("Weight has to be larger than zero");

        incomingWeights.put(preNode, weight);

        return predecessorNodes.contains(preNode) || predecessorNodes.add(preNode);
    }

    /**
     * Gets the id of this node
     *
     * @return the id of this node
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of this node.
     *
     * @return the name of this node
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the {@link org.woped.core.model.petrinet.AbstractPetriNetElementModel} which is source for this node.
     *
     * @return the id of the petrinet element which is source for this node.
     */
    public String getOriginId() {
        if ( originId == null ) {
            return id;
        }
        return originId;
    }

    /**
     * Gets the successors of this node.
     *
     * @return the the successors of this node.
     */
    public Set<AbstractNode> getSuccessorNodes() {
        return successorNodes;
    }

    /**
     * Gets the predecessors of this node.
     *
     * @return the predecessors of this node.
     */
    public Set<AbstractNode> getPredecessorNodes() {
        return predecessorNodes;
    }

    /**
     * Gets the weight of the directed connection from the given node.
     *
     * @param source the source node of the connection
     * @return the weight of the connection or zero, if no such connection exists
     */
    public int getWeightFrom(AbstractNode source) {
        Integer weight = incomingWeights.get(source);
        return weight == null ? 0 : weight;
    }

    /**
     * Gets the weight of the directed connection to the given node.
     *
     * @param target the target node of the connection
     * @return the weight of the connection or zero, if no such connection exists
     */
    public int getWeightTo(AbstractNode target) {
        Integer weight = outgoingWeights.get(target);
        return weight == null ? 0 : weight;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        AbstractNode other = (AbstractNode) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals(other.id) ) {
            return false;
        }
        if ( originId == null ) {
            if ( other.originId != null ) {
                return false;
            }
        } else if ( !originId.equals(other.originId) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((originId == null) ? 0 : originId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
