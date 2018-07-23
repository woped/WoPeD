package de.hpi.bpt.hypergraph.abs;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Directed hyper graph implementation
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for hyper edge (extends IDirectedHyperEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractDirectedHyperGraph<E extends IDirectedHyperEdge<V>,V extends IVertex>
		extends AbstractMultiDirectedHyperGraph<E,V> {
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V s, V t) {
		Collection<V> ss = new ArrayList<V>(); ss.add(s);
		Collection<V> ts = new ArrayList<V>(); ts.add(t);
		
		if (this.checkEdge(ss, ts))
			return super.addEdge(s, t);
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(java.util.Collection, java.util.Collection)
	 */
	@Override
	public E addEdge(Collection<V> ss, Collection<V> ts) {
		if (this.checkEdge(ss, ts))
			return super.addEdge(ss, ts);
		
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
