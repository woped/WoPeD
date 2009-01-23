package org.woped.core.analysis;

public class FlowArc
{
	private final int capacity = 1;
	private int flow = 0;

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

	private FlowNode source;
	private FlowNode target;
	
	FlowArc(FlowNode source, FlowNode target){
		this.source = source;
		this.target = target;
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