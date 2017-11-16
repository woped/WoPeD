package de.hpi.bpt.graph.algo;

import de.hpi.bpt.graph.abs.IDirectedEdge;
import de.hpi.bpt.graph.abs.IDirectedGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

public class ReflexiveTransitiveClosure<E extends IDirectedEdge<V>,V extends IVertex> extends TransitiveClosure<E, V> {

	public ReflexiveTransitiveClosure(IDirectedGraph<E, V> g) {
		super(g);
	}
	
	@Override
	protected void calculateMatrix() {
		super.calculateMatrix();
		
		for (int i=0; i<this.verticesAsList.size(); i++) {
			this.matrix[i][i] = true;
		}
	}
}


