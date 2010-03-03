package org.woped.qualanalysis.structure.components;

/**
 * @authors Bernhard von Hasseln, Matthias Mruzek and Markus Noeltner <br>
 * 
 * This class connects flow nodes in a low level net. In getMaxFlow() in the
 * class LowLevelNet the flow on each arc is noted in its attribute "flow".
 * <br>
 */

public class FlowArc
{
	//! Maximum capacity of an arc
	private final int capacity = 1;
	
	//! In getMaxFlow() (class LowLevelNet) the current flow is noted
	//! in this attribute.
	private int flow = 0;
	
	//! Source of the arc
	private FlowNode source;
	
	//! Target of the arc
	private FlowNode target;

	//! Constructor
	FlowArc(FlowNode source, FlowNode target){
		this.source = source;
		this.target = target;
	}
	
	public int getFlow() {
		return flow;
	}

	public void setFlow(int flow) {
		this.flow = flow;
	}

	public void addFlow(int flow) {
		this.flow += flow;
	}
	
	public void subFlow(int flow) {
		this.flow -= flow;
	}
	
	FlowNode getSource(){
		return this.source;
	}
	
	FlowNode getTarget(){
		return this.target;
	}

	public int getCapacity() {
		return capacity;
	}

}