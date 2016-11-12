package org.woped.qualanalysis.soundness.datamodel;

import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.Iterator;

/**
 * this class represents a transition in a low level petri net extends AbstractNode
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public class TransitionNode extends AbstractNode {

    private final int opType;

    /**
     * @param id       the transition-id
     * @param name     the transition-name
     * @param originId the transition-originId (needed to reference back to the editor)
     */
    public TransitionNode(String id, String name, String originId, int opType) {
        super(id, name, originId);
        this.opType = opType;
    }

    /**
     * Adds a new successor to this node with the default weight 1.
     *
     * @param successor the successor to add
     * @return true if the provided node has been added, otherwise false
     */
    public boolean addSuccessorNode(AbstractNode successor) {
        return addSuccessorNode(successor, 1);
    }

    @Override
    public boolean addSuccessorNode(AbstractNode successor, int weight) {
        if ( successor instanceof PlaceNode ) {
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
        if ( predecessor instanceof PlaceNode ) {
            return super.addPredecessorNode(predecessor, weight);
        } else {
            throw new RuntimeException("Invalid type of the provided node.");
        }
    }

    @Override
    public String toString() {
        String nodeName = "";

        if ( this.opType == OperatorTransitionModel.XOR_JOIN_TYPE || this.opType == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE || this.opType == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE ) {
            Iterator<AbstractNode> it = getPredecessorNodes().iterator();
            nodeName += "(";
            while ( it.hasNext() ) {
                AbstractNode node = it.next();
                if ( node.getName().equals(this.getName()) ) {
                    nodeName += OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE;
                }
                nodeName += node.getName();
            }
            nodeName += ")";
        }

        nodeName += super.toString();

        if ( this.opType == OperatorTransitionModel.XOR_SPLIT_TYPE || this.opType == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE || this.opType == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE ) {
            Iterator<AbstractNode> it = getSuccessorNodes().iterator();
            nodeName += "(";
            while ( it.hasNext() ) {
                AbstractNode node = it.next();
                if ( node.getName().equals(this.getName()) ) {
                    nodeName += OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE;
                }
                nodeName += node.getName();
            }
            nodeName += ")";
        }
        return nodeName;
    }
}
