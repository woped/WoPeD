package org.woped.qualanalysis.soundness.datamodel;

import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.Iterator;

/**
 * this class represents a transition in a low level petri net extends AbstractNode
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */ 
public class TransitionNode extends AbstractNode {

    private final int opType;

    /**
     *
	 * @param id the transition-id
	 * @param name the transition-name
	 * @param originId the transition-originId (needed to reference back to the editor)
	 */
    public TransitionNode(String id, String name, String originId, int opType) {
        super(id, name, originId);
        this.opType = opType;
    }

    @Override
    public boolean addSuccessorNode(AbstractNode successor) {
        if ( successor instanceof PlaceNode ) {
            return super.addSuccessorNode(successor);
        } else {
            throw new RuntimeException("Invalid type of the provided node.");
        }
    }

    @Override
    public boolean addPredecessorNode(AbstractNode predecessor) {
        if ( predecessor instanceof PlaceNode ) {
            return super.addPredecessorNode(predecessor);
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
