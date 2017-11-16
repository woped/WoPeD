package de.hpi.bpt.hypergraph.abs;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Hyper graph implementation
 * Hyper graph is collection of hyper edges and disconnected vertices
 * Multi edges are not allowed
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for hyper edge (extends IHyperEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractHyperGraph <E extends IHyperEdge<V>,V extends IVertex>
		extends AbstractMultiHyperGraph<E,V> {
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V v) {
		Collection<V> vs = new ArrayList<V>(); vs.add(v);
		if (this.checkEdge(vs))
			return super.addEdge(v);
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(java.util.Collection)
	 */
	@Override
	public E addEdge(Collection<V> vs) {
		if (this.checkEdge(vs))
			return super.addEdge(vs);
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#isMultiGraph()
	 */
	@Override
	public boolean isMultiGraph() {
		return false;
	}
}
