package de.hpi.bpt.hypergraph;

import java.util.ArrayList;
import java.util.Collection;

import de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * Multi directed graph implementation
 * 
 * @author Artem Polyvyanyy
 */
public class MultiDirectedHyperGraph extends AbstractMultiDirectedHyperGraph<DirectedHyperEdge, Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(java.util.Collection)
	 */
	@Override
	public DirectedHyperEdge addEdge(Collection<Vertex> ss, Collection<Vertex> ts) {
		DirectedHyperEdge e = new DirectedHyperEdge(this);
		e.addSourceAndTagetVertices(ss, ts);
		
		return e;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public DirectedHyperEdge addEdge(Vertex s, Vertex t) {
		DirectedHyperEdge e = new DirectedHyperEdge(this);
		Collection<Vertex> ss = new ArrayList<Vertex>(); ss.add(s);
		Collection<Vertex> ts = new ArrayList<Vertex>(); ts.add(t);
		e.addSourceAndTagetVertices(ss, ts);
		return e;
	}
}
