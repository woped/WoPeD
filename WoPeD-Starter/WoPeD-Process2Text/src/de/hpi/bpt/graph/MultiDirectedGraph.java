package de.hpi.bpt.graph;

import de.hpi.bpt.graph.abs.AbstractMultiDirectedGraph;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * Directed multi graph implementation
 *
 * @author Artem Polyvyanyy
 */
public class MultiDirectedGraph extends AbstractMultiDirectedGraph<DirectedEdge,Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public DirectedEdge addEdge(Vertex s, Vertex t) {
		DirectedEdge e = new DirectedEdge(this,s,t);
		return e;
	}
}
