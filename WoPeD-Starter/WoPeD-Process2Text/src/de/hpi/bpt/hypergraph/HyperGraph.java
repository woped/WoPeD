package de.hpi.bpt.hypergraph;

import java.util.ArrayList;
import java.util.Collection;

import de.hpi.bpt.hypergraph.abs.AbstractHyperGraph;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * Hyper graph implementation
 * Hyper graph is a collection of hyper edges and vertices
 * Multiple edges are not allowed, i.e., edges with same vertices
 * 
 * @author Artem Polyvyanyy
 */
public class HyperGraph extends AbstractHyperGraph<HyperEdge, Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperGraph#addEdge(java.util.Collection)
	 */
	@Override
	public HyperEdge addEdge(Collection<Vertex> vs) {
		if (!this.checkEdge(vs)) return null;
		
		HyperEdge e = new HyperEdge(this);
		e.addVertices(vs);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public HyperEdge addEdge(Vertex v) {
		Collection<Vertex> vs = new ArrayList<Vertex>(); vs.add(v);
		if (!this.checkEdge(vs)) return null;
		
		HyperEdge e = new HyperEdge(this);
		e.addVertex(v);
		return e;
	}
}
