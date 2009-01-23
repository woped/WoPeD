package org.woped.core.analysis;

import java.util.Iterator;
import java.util.LinkedList;

import org.woped.core.model.AbstractElementModel;

public class FlowNode
{
    private AbstractElementModel petriNetNode;

	//! Remember whether this is the first or the second node of our split petrinet node
    private boolean first;

	//! Remember all predecessors of this node
    private LinkedList<FlowArc> m_incomingArcs;
    //! Remember all successors of this node
    private LinkedList<FlowArc> m_outgoingArcs;
    
    public LinkedList<FlowArc> getM_outgoingArcs() {
		return m_outgoingArcs;
	}

	private int z = 0;
	
	private boolean visited = false;
	
    
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

	private FlowNode predecessor = null;
    
    public FlowNode getPredecessor() {
		return predecessor;
	}
	public void setPredecessor(FlowNode predecessor) {
		this.predecessor = predecessor;
	}

	private boolean isForeward = false; 					//forward = true, backward = false
    
    public boolean isForeward() {
		return isForeward;
	}
	public void setForeward(boolean isForeward) {
		this.isForeward = isForeward;
	}
	public AbstractElementModel getPetriNetNode() {
		return petriNetNode;
	}
    public boolean isFirst() {
		return first;
	}
    
    public FlowNode(AbstractElementModel i, boolean first)
    {
       this.m_incomingArcs = new LinkedList<FlowArc>();
       this.m_outgoingArcs = new LinkedList<FlowArc>();
       this.petriNetNode = i;
       this.first = first;
       this.z = Integer.MAX_VALUE;
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


