package de.hpi.bpt.graph.abs;

import java.util.ArrayList;
import java.util.Collection;

import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Graph implementation
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractGraph<E extends IEdge<V>,V extends IVertex> extends AbstractMultiGraph<E,V>
{	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractMultiGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V v1, V v2) {
		Collection<V> vs = new ArrayList<V>();
		vs.add(v1); vs.add(v2);
		
		if (!this.checkEdge(vs)) return null;
		
		return super.addEdge(v1, v2);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractMultiGraph#isMultiGraph()
	 */
	@Override
	public boolean isMultiGraph() {
		return false;
	}
}
