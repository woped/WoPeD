package de.hpi.bpt.graph.abs;

import java.util.ArrayList;
import java.util.Collection;

import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Directed graph implementation
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IDirectedEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractDirectedGraph<E extends IDirectedEdge<V>, V extends IVertex>
		extends AbstractMultiDirectedGraph<E,V> {

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V s, V t) {
		Collection<V> ss = new ArrayList<V>(); ss.add(s);
		Collection<V> ts = new ArrayList<V>(); ts.add(t);
		if (!this.checkEdge(ss,ts)) return null;
		
		return (E)(new AbstractDirectedEdge<V>(this,s,t));
	}
	
	@Override
	public boolean isMultiGraph() {
		return false;
	}
}
