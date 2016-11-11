package org.woped.qualanalysis.soundness.datamodel;

/**
 * This class contains the information which is pushed to the observers,
 * when a node is added to a class that implements the {@link INotifyNodeAdded} interface.
 */
class NodeAddedArgs {

    private final AbstractNode sender;
    private final AbstractNode node;
    private final ConnectionType direction;
    private final int weight;

    /**
     * Creates a new instance of NodeAddedArgs.
     *
     * @param sender    the node which has added the new node
     * @param addedNode the node that has been added
     * @param direction the direction of the connection from the observed node
     * @param weight    the weight of the connection
     */
    NodeAddedArgs(AbstractNode sender, AbstractNode addedNode, ConnectionType direction, int weight) {
        this.sender = sender;
        this.node = addedNode;
        this.direction = direction;
        this.weight = weight;
    }

    /**
     * Gets the node that has triggered the update.
     *
     * @return the node that has triggered the update
     */
    public AbstractNode getSender() {
        return sender;
    }

    /**
     * Gets the node which has been added.
     *
     * @return the added node
     */
    public AbstractNode getAddedNode() {
        return node;
    }

    /**
     * Gets the direction of the connection from the point of view of the sender.
     *
     * @return the direction of the connection.
     */
    public ConnectionType getDirection() {
        return direction;
    }

    /**
     * Gets the weight of the new connection between sender and the new node.
     *
     * @return the weight of the connection
     */
    public int getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        int basePrime = 71;
        int biasPrime = 73;

        int result = basePrime;
        result *= biasPrime + this.getAddedNode().hashCode();
        result *= biasPrime + this.getDirection().hashCode();
        result *= biasPrime + this.getWeight();

        return result;
    }

    @Override
    public boolean equals(Object other) {
        if ( other == this ) return true;

        if ( other == null ) return false;

        if ( !(other instanceof NodeAddedArgs) ) return false;

        NodeAddedArgs otherArgs = (NodeAddedArgs) other;

        if ( !otherArgs.getAddedNode().equals(this.getAddedNode()) ) return false;

        //noinspection SimplifiableIfStatement
        if ( !otherArgs.getDirection().equals(this.getDirection()) ) return false;

        return otherArgs.getWeight() == this.getWeight();

    }
}
