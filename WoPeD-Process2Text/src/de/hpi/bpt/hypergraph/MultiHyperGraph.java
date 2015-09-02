package de.hpi.bpt.hypergraph;

import java.util.Collection;

import de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * Multi hyper graph implementation
 * Multi hyper graph is a collection of hyper edges and vertices
 * Multiple edges are allowed, i.e., edges with same vertices
 * 
 * @author Artem Polyvyanyy
 */
public class MultiHyperGraph extends AbstractMultiHyperGraph<HyperEdge, Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(java.util.Collection)
	 */
	@Override
	public HyperEdge addEdge(Collection<Vertex> vs) {
		HyperEdge e = new HyperEdge(this);
		e.addVertices(vs);
		
		return e;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public HyperEdge addEdge(Vertex v) {
		HyperEdge e = new HyperEdge(this);
		e.addVertex(v);
		
		return e;
	}
}
