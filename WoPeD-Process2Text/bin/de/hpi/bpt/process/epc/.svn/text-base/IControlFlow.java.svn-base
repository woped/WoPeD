package de.hpi.bpt.process.epc;

import de.hpi.bpt.graph.abs.IDirectedEdge;


/**
 * The connection in a process model which is the part of the control flow.
 * 
 * @author Artem Polyvyanyy
 */
public interface IControlFlow<V extends IFlowObject> extends IDirectedEdge<V> {

	/**
	 * Get the probability of the transition from the flow object which is the source of this connection to the target flow object. 
	 * 
	 * @return transition probability value.
	 */
	double getProbability();

	/**
	 * Set the probability of the transition from the flow object which is the source of this connection to the target flow object.
	 * 
	 * @param p the transition probability value.
	 */
	void setProbability(double p);
}