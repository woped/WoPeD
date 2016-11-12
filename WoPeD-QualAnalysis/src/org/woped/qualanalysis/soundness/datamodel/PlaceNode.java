package org.woped.qualanalysis.soundness.datamodel;

/**
 * This class represents a place in a low level petri net.
 */
public class PlaceNode extends AbstractNode {
    private Integer tokenCount;
    private Integer virtualTokenCount;

    /**
     * default constructor.
     * 
     * @param tokenCount count of tokens
     * @see AbstractNode#AbstractNode(String, String, String)
     */
    public PlaceNode(Integer tokenCount, Integer virtualTokenCount, String id, String name, String originId) {
        super(id, name, originId);
        this.tokenCount = tokenCount;
        this.virtualTokenCount = virtualTokenCount;
    }

    /**
     * Adds a new successor to this node with the default weight 1.
     *
     * @param successor the successor to add
     * @return true if the provided node has been added, otherwise false
     */
    public boolean addSuccessorNode(AbstractNode successor) {
        return this.addSuccessorNode(successor, 1);
    }

    @Override
    public boolean addSuccessorNode(AbstractNode successor, int weight) {
        if ( successor instanceof TransitionNode ) {
            return super.addSuccessorNode(successor, weight);
        } else {
            throw new RuntimeException("Invalid type of the provided node.");
        }
    }

    /**
     * Adds a new predecessor to this node with the default weight 1.
     *
     * @param predecessor the predecessor to add
     * @return true if the provided node has been added, otherwise false
     * @throws IllegalArgumentException if weight is less or equal zero
     */
    public boolean addPredecessorNode(AbstractNode predecessor) {
        return addPredecessorNode(predecessor, 1);
    }

    @Override
    public boolean addPredecessorNode(AbstractNode predecessor, int weight) {
        if ( predecessor instanceof TransitionNode ) {
            return super.addPredecessorNode(predecessor, weight);
        } else {
            throw new RuntimeException("Invalid type of the provided node.");
        }
    }

    /**
     * Gets the amount of tokens contained in this place.
     *
     * @return the count of tokens
     */
    public Integer getTokenCount() {
        return tokenCount;
    }

    /**
     * Sets the amount of tokens contained in this place.
     *
     * @param tokenCount the tokenCount to set
     */
    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    /**
     * Gets the virtual token count from this place.
     *
     * @return the virtual token count
     */
    public Integer getVirtualTokenCount() {
        return virtualTokenCount;
    }
}
