package org.woped.qualanalysis.soundness.datamodel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.qualanalysis.soundness.algorithms.generic.INode;

/**
 * this abstract class represents a node in a low level petri net all nodes e.g transitions, places should be extend
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public abstract class AbstractNode implements INode<AbstractNode> {
    // declaration
    private final Set<AbstractNode> preNodes = new HashSet<AbstractNode>();
    private final Set<AbstractNode> postNodes = new HashSet<AbstractNode>();
    private final String id;
    private final String originId;
    private final String name;
    private final int    optype;

    /**
     * default constructor.
     * 
     * @param id identifier
     */
    protected AbstractNode(String id, String name, String originId, int optype) {
        this.id = id;
        this.name = name;
        this.originId = originId;
        this.optype = optype;
    }

    /**
     * @param postNode the postNode to add
     * @return true if the provided node was added
     */
    public boolean addPostNode(AbstractNode postNode) {
        if (postNode.addPreNode(this)) {
            return postNodes.add(postNode);
        } else {
            return false;
        }
    }

    /**
     * @param preNode the preNode to add
     * @return true if the the provided node was added
     */
    public boolean addPreNode(AbstractNode preNode) {
        return preNodes.add(preNode);
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractNode other = (AbstractNode) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else
            if (!id.equals(other.id)) {
                return false;
            }
        if (originId == null) {
            if (other.originId != null) {
                return false;
            }
        } else
            if (!originId.equals(other.originId)) {
                return false;
            }
        return true;
    }

    /**
     * identifier.
     * 
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * @return the type
     */
    public int getOpType() {
        return optype;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getOriginId() {
        if (originId == null) {
            return id;
        }
        return originId;
    }

    /**
     * @return the postNodes as an array
     */
    public Set<AbstractNode> getPostNodes() {
        return postNodes;
    }

    /**
     * @return the preNodes as an array
     */
    public Set<AbstractNode> getPreNodes() {
        return preNodes;
    }

    /**
     * @return the node-id as string
     */
    @Override
    public String toString() {
    	String nodeName = "";
    	
        if (this.optype == OperatorTransitionModel.XOR_JOIN_TYPE || 
            	this.optype == OperatorTransitionModel.XOR_SPLITJOIN_TYPE ||
            	this.optype == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE) {
        	Iterator<AbstractNode> it = getPreNodes().iterator();
    		nodeName += "(";
    		while (it.hasNext()) {
    			AbstractNode node = it.next();
    			if (node.getName().equals(name)) {
    				nodeName += OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE;
    			}
    			nodeName += node.getName();
    		} 		
    		nodeName += ")";
        }
        
        nodeName += name;
    	
        if (this.optype == OperatorTransitionModel.XOR_SPLIT_TYPE || 
        	this.optype == OperatorTransitionModel.XOR_SPLITJOIN_TYPE ||
        	this.optype == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE) {
        	Iterator<AbstractNode> it = getPostNodes().iterator();
    		nodeName += "(";
    		while (it.hasNext()) {
       			AbstractNode node = it.next();
    			if (node.getName().equals(name)) {
    				nodeName += OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE;
    			}
    			nodeName += node.getName();
    		} 		
    		nodeName += ")";
        }
    	return nodeName;
    }

}
