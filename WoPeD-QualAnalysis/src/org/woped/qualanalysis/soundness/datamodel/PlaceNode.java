package org.woped.qualanalysis.soundness.datamodel;

/**
 * this class represents a place in a low level petri net extends AbstractNode
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */ 
public class PlaceNode extends AbstractNode {
    // declaration
    private Integer tokenCount;
    private Integer virtualTokenCount;

    /**
     * default constructor.
     * 
     * @param tokenCount count of tokens
     * @see AbstractNode#AbstractNode(String)
     */
    public PlaceNode(Integer tokenCount, Integer virtualTokenCount, String id, String name, String originId) {
        super(id, name, originId);
        this.tokenCount = tokenCount;
        this.virtualTokenCount = virtualTokenCount;
    }

    /**
     * @see AbstractNode#addPostNode(AbstractNode)
     */
    @Override
    public boolean addPostNode(AbstractNode postNode) {
        if (postNode instanceof TransitionNode) {
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
        if (preNode instanceof TransitionNode) {
            return super.addPreNode(preNode);
        } else {
            throw new RuntimeException("Invalid type of the provided node.");
        }
    }

    /**
     * @return the tokenCount
     */
    public Integer getTokenCount() {
        return tokenCount;
    }

    /**
     * @param tokenCount the tokenCount to set
     */
    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    /**
     * @return the virtualTokenCount
     */
    public Integer getVirtualTokenCount() {
        return virtualTokenCount;
    }
}
