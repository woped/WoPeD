package de.hpi.bpt.graph;

import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.graph.abs.AbstractDirectedGraph;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * Directed graph implementation
 * 
 * @author Artem Polyvyanyy
 */
public class DirectedGraph extends AbstractDirectedGraph<DirectedEdge,Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractDirectedGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public DirectedEdge addEdge(Vertex s, Vertex t) {
		if (s == null || t == null) return null;
		Collection<DirectedEdge> es = this.getEdgesWithSourceAndTarget(s, t);
		if (es.size()>0) {
			Iterator<DirectedEdge> i = es.iterator();
			while (i.hasNext()) {
				DirectedEdge e = i.next();
				if (e.getVertices().size()==2)
					return null;
			}
		}
		
		DirectedEdge e = new DirectedEdge(this, s, t);
		return e;
	}
}
