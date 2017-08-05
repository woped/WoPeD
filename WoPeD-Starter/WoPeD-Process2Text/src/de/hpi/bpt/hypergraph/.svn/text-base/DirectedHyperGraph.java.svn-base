package de.hpi.bpt.hypergraph;

import java.util.ArrayList;
import java.util.Collection;

import de.hpi.bpt.hypergraph.abs.AbstractDirectedHyperGraph;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * Directed hyper graph implementation
 * Directed hyper graph is collection of directed hyper edges and disconnected vertices
 * Multi edges are not allowed
 * 
 * @author Artem Polyvyanyy
 */
public class DirectedHyperGraph extends AbstractDirectedHyperGraph<DirectedHyperEdge,Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractDirectedHyperGraph#addEdge(java.util.Collection, java.util.Collection)
	 */
	@Override
	public DirectedHyperEdge addEdge(Collection<Vertex> ss, Collection<Vertex> ts) {
		if (!this.checkEdge(ss, ts)) return null;
		
		DirectedHyperEdge e = new DirectedHyperEdge(this);
		e.addSourceAndTagetVertices(ss, ts);
		
		return e;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public DirectedHyperEdge addEdge(Vertex s, Vertex t) {
		Collection<Vertex> ss = new ArrayList<Vertex>(); ss.add(s);
		Collection<Vertex> ts = new ArrayList<Vertex>(); ts.add(t);
		if (!this.checkEdge(ss, ts)) return null;
		
		DirectedHyperEdge e = new DirectedHyperEdge(this);
		e.addSourceAndTagetVertices(ss, ts);
		
		return e;
	}
}
