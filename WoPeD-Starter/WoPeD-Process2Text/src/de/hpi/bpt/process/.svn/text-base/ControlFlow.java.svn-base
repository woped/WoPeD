package de.hpi.bpt.process;

import de.hpi.bpt.graph.abs.AbstractDirectedEdge;
import de.hpi.bpt.graph.abs.AbstractMultiDirectedGraph;


public class ControlFlow extends AbstractDirectedEdge<Node> {

	private String label = "";
	
	@SuppressWarnings("unchecked")
	protected ControlFlow(AbstractMultiDirectedGraph g, Node source, Node target) {
		super(g, source, target);
	}

	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
}
