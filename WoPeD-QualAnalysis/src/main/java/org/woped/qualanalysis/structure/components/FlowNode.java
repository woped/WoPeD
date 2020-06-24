package org.woped.qualanalysis.structure.components;

import java.util.Iterator;
import java.util.LinkedList;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

/**
 * @authors Bernhard von Hasseln, Matthias Mruzek and Markus Noeltner <br>
 * 
 * This class represents an element in a LowLevelNet. It is derived from a petri
 * net node. There are always 2 flow nodes for 1 petri net node (first and second). 
 * FlowArcs are used to connect FlowNodes among themselves.
 * <br>
 */

public class FlowNode
{
    //! The original petri net node where this FlowNode is derived from.
	private AbstractPetriNetElementModel petriNetNode;

	//! Distinguishes whether this is the first or the second node of original 
	//! the split up petri net node
    private boolean first;

	//! All predecessors of this node
    private LinkedList<FlowArc> m_incomingArcs;
    
    //! All successors of this node
    private LinkedList<FlowArc> m_outgoingArcs;
    
    //! z value used in getMaxFlow(), LowLevelNet
	private int z = 0;
	
	//! Visited flag used in getMaxFlow(), LowLevelNet
	private boolean visited = false;
	
	//! Remembers the predecessor in a getMaxFlow() run (class LowLevelNet)
	private FlowNode predecessor = null;
	
	//! Remembers in which direction the node was visited 
	//! in a getMaxFlow() run (class LowLevelNet).
	//! forwards = true, backwards = false
	private boolean isForeward = false; 
    
	//! Constructor
    public FlowNode(AbstractPetriNetElementModel i, boolean first)
    {
       this.m_incomingArcs = new LinkedList<FlowArc>();
       this.m_outgoingArcs = new LinkedList<FlowArc>();
       this.petriNetNode = i;
       this.first = first;
       this.z = Integer.MAX_VALUE;
    }
	
	public LinkedList<FlowArc> getM_outgoingArcs() {
		return m_outgoingArcs;
	}

    public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}

    public FlowNode getPredecessor() {
		return predecessor;
	}
	public void setPredecessor(FlowNode predecessor) {
		this.predecessor = predecessor;
	}			
    
    public boolean isForeward() {
		return isForeward;
	}
	public void setForeward(boolean isForeward) {
		this.isForeward = isForeward;
	}
	public AbstractPetriNetElementModel getPetriNetNode() {
		return petriNetNode;
	}
    public boolean isFirst() {
		return first;
	}
    
    public void addIncomingArc(FlowArc n)
    {
    	m_incomingArcs.add(n);
    }
    
    public void addOutgoingArc(FlowArc n)
    {
    	m_outgoingArcs.add(n);
    }
    
    public Iterator<FlowArc> getOutgoingArcs() {
		return m_outgoingArcs.iterator();
	}
    
    public Iterator<FlowArc> getIncomingArcs() {
		return m_incomingArcs.iterator();
	}
    
    //! Returns the arc to the forwarded node
    public FlowArc getArcTo(FlowNode target) {
    	Iterator<FlowArc> i = getOutgoingArcs();
    	FlowArc tmpArc = null;
    	
    	while (i.hasNext()){
    		tmpArc = i.next();
    		if (tmpArc.getTarget().equals(target)){
    			return tmpArc;
    		}	
    	}
    	
    	return null;
	}
    
    //! Returns the arc which leads from the forwarded node to
    //! the current node.
    public FlowArc getArcFrom(FlowNode source) {
    	Iterator<FlowArc> i = getIncomingArcs();
    	FlowArc tmpArc = null;
    	
    	while (i.hasNext()){
    		tmpArc = i.next();
    		if (tmpArc.getSource().equals(source)){
    			return tmpArc;
    		}	
    	}
    	
    	return null;
	}
}


