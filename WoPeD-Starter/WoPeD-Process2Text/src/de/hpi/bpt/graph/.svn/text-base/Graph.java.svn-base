package de.hpi.bpt.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.graph.abs.AbstractGraph;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * Graph implementation
 * 
 * @author Artem Polyvyanyy
 */
public class Graph extends AbstractGraph<Edge,Vertex>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public Edge addEdge(Vertex v1, Vertex v2) {
		Collection<Vertex> vs = new ArrayList<Vertex>();
		vs.add(v1); vs.add(v2);
		Collection<Edge> es = this.getEdges(vs);
		if (es.size()>0) {
			Iterator<Edge> i = es.iterator();
			while (i.hasNext()) {
				Edge e = i.next();
				if (e.getVertices().size()==2)
					return null;
			}
		}
		
		Edge e = new Edge(this,v1,v2);
		return e;
	}
}
