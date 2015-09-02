package de.hpi.bpt.graph;

import de.hpi.bpt.graph.abs.AbstractMultiDirectedGraphFragment;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * 
 * @author Artem Polyvyanyy
 *
 */
public class MultiDirectedGraphFragment extends AbstractMultiDirectedGraphFragment<DirectedEdge,Vertex> {

	/**
	 * Constructor
	 * @param parent Parent graph of the fragment
	 */
	public MultiDirectedGraphFragment(MultiDirectedGraph parent) {
		super(parent);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractMultiDirectedGraphFragment#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public DirectedEdge addEdge(Vertex s, Vertex t) {
		if (this.graph!=null && this.graph.areAdjacent(s, t)) {
			DirectedEdge e = new DirectedEdge(this,s,t);
			return e;
		}	
		
		return null;
	}
}
