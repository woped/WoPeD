package org.woped.quantanalysis;

public class NodeProperties {
	
	private String nodeId				= "";
	private double input				= 0.0;
	private double multiple				= 1.0;
	private double runs					= 0.0;
	private int numInputArcs			= 0;
	
	public int getNumInputArcs() {
		return numInputArcs;
	}

	public void setNumInputArcs(int numInputArcs) {
		this.numInputArcs = numInputArcs;
	}

	public NodeProperties(String id)
	{
		nodeId = id;
	}

	public double getInput() {
		return input;
	}

	public void setInput(double input) {
		this.input = input;
	}

	public double getMultiple() {
		return multiple;
	}

	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}

	public double getRuns() {
		return runs;
	}

	public void setRuns(double runs) {
		this.runs = runs;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public String toString(){
		String text = "";
		text += "ID: " + nodeId;
		text += " :: (I-M-R) " + input + " - " + multiple + " - " + runs;
		text += "; Input: " + numInputArcs;
		
		return text;
	}
}
