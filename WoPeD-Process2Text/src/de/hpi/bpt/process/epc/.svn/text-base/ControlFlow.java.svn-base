package de.hpi.bpt.process.epc;

import de.hpi.bpt.graph.abs.AbstractDirectedEdge;
import de.hpi.bpt.graph.abs.AbstractDirectedGraph;

/**
 * EPC control flow edge implementation
 * 
 * @author Artem Polyvyanyy
 */
public class ControlFlow extends AbstractDirectedEdge<FlowObject> implements IControlFlow<FlowObject> {
	private double p = 1.0;
	
	@SuppressWarnings("unchecked")
	protected ControlFlow(AbstractDirectedGraph g, FlowObject source,
			FlowObject target) {
		super(g, source, target);
	}

	@SuppressWarnings("unchecked")
	protected ControlFlow(AbstractDirectedGraph g, FlowObject source,
			FlowObject target, float probability) {
		super(g, source, target);
		this.setProbability(probability);
	}

	public double getProbability() {
		return this.p;
	}

	public void setProbability(double p) {
		this.p = p;
	}
}
