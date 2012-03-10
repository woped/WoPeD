package org.woped.qualanalysis.soundness.datamodel;

/**
 * this class represents a transition in a low level petri net extends AbstractNode
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */ 
public class TransitionNode extends AbstractNode {

	/**
	 * 
	 * @param id the transition-id
	 * @param name the transition-name
	 * @param originId the transition-originId (needed to reference back to the editor)
	 */
    public TransitionNode(String id, String name, String originId, int optype) {
        super(id, name, originId, optype);
    }

    /**
     * @see AbstractNode#addPostNode(AbstractNode)
     */
    @Override
    public boolean addPostNode(AbstractNode postNode) {
        if (postNode instanceof PlaceNode) {
            return super.addPostNode(postNode);
        } else {
            throw new RuntimeException("Invalid type of the provided node.");
        }
    }

    /**
     * @see AbstractNode#addPreNode(AbstractNode)
     */
    @Override
    public boolean addPreNode(AbstractNode preNode) {
        if (preNode instanceof PlaceNode) {
            return super.addPreNode(preNode);
        } else {
            throw new RuntimeException("Invalid type of the provided node.");
        }
    }

}
