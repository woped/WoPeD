/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl.wfnet;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author Chris
 */
public abstract class WfNetNode {

    protected String id = "";

    protected WfNet parent;

    protected String wopedOperatorId = "";
    protected String wopedOperatorCode = "";

    
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        if (this.parent != null) {
            this.parent.setNodeId(this, id);
        }
        this.id = id;
    }
    
    private ArrayList<WfNetNode> inputNodes = new ArrayList<WfNetNode>();
    public ListIterator<WfNetNode> inputNodes() {
        return inputNodes.listIterator();
    }

    private ArrayList<WfNetNode> outputNodes = new ArrayList<WfNetNode>();
    public ListIterator<WfNetNode> outputNodes() {
        return outputNodes.listIterator();
    }
    
    private String name = "";
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    private Rectangle shapePosition = new Rectangle();
    public Rectangle getShapePosition() {
        return shapePosition;
    }
    public void setShapePosition(Rectangle shapePosition) {
        this.shapePosition = shapePosition;
    }


    private Rectangle labelPosition = new Rectangle();
    public Rectangle getLabelPosition() {
        return labelPosition;
    }
    public void setLabelPosition(Rectangle labelPosition) {
        this.labelPosition = labelPosition;
    }
       
    
    
    WfNetNode(String id, WfNet parent) {
        this.id = id;
        this.parent = parent;
    }
    
    public void addInputNode(WfNetNode node) {
        this.inputNodes.add(node);
        node.outputNodes.add(this);
    }
    
    public void addOutputNode(WfNetNode node) {
        this.outputNodes.add(node);
        node.inputNodes.add(this);
    }
    
    public void removeInputNode(WfNetNode node) {
        this.inputNodes.remove(node);
        node.outputNodes.remove(this);
    }
    
    public void removeOutputNode(WfNetNode node) {
        this.outputNodes.remove(node);
        node.inputNodes.remove(this);
    }
    
    public ArrayList<WfNetNode> getInputNodes() {
        return this.inputNodes;
    }
    
    public int getInputNodeCount() {
        return this.inputNodes.size();
    }
    
    public WfNetNode getInputNode(int index) {
        return this.inputNodes.get(index);
    }    
    
    public ArrayList<WfNetNode> getOutputNodes() {
        return this.outputNodes;
    }
    
    public int getOutputNodeCount() {
        return this.outputNodes.size();
    }
    
    public WfNetNode getOutputNode(int index) {
        return this.outputNodes.get(index);
    }
    
    /**
     * Insert a new Node between this one and the specified existing
     * output node (i.e. create the following flow: 
     * this -> newOutputNode -> existingOutputNode)
     * @param existingOutputNode
     * @param newOutputNode 
     */
    public void insertBetween(WfNetNode existingOutputNode, 
            WfNetNode newOutputNode) {
        for (WfNetNode node : outputNodes) {
            if(!node.equals(existingOutputNode)) continue;
            
            this.removeOutputNode(existingOutputNode);
            this.addOutputNode(newOutputNode);
            newOutputNode.addOutputNode(existingOutputNode);
        }
    }
    
    public void copyAttributesTo(WfNetNode node,
            boolean copy_arcs) {
        node.name = this.name;
        node.shapePosition = this.shapePosition.clone();
        node.labelPosition = this.labelPosition.clone();
        
        if (copy_arcs) {
            for (WfNetNode input_node : this.inputNodes) {
                input_node.addOutputNode(node);
            }
            for (WfNetNode output_node : this.outputNodes) {
                output_node.addInputNode(node);
            }
        }
    }
    
    
    public String getWopedOperatorCode() {
        return wopedOperatorCode;
    }

    public void setWopedOperatorCode(String wopedOperatorCode) {
        this.wopedOperatorCode = wopedOperatorCode;
    }

    public String getWopedOperatorId() {
        return wopedOperatorId;
    }

    public void setWopedOperatorId(String wopedOperatorId) {
        this.wopedOperatorId = wopedOperatorId;
    }
    
    
    public void moveTo(int x, int y) {
        int dx = x - this.shapePosition.x;
        int dy = y - this.shapePosition.y;
        
        this.shapePosition.x = x;
        this.shapePosition.y = y;
        this.labelPosition.x += dx;
        this.labelPosition.y += dy;
    }
}
