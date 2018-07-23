package de.hpi.bpt.graph.abs;

import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E>
 * @param <V>
 */
public class AbstractMultiDirectedGraphFragment<E extends IDirectedEdge<V>, V extends IVertex> extends AbstractMultiDirectedGraph<E, V> {

	protected AbstractMultiDirectedGraph<E, V> graph;
	
	/**
	 * Constructor
	 * @param parent Parent graph of the fragment
	 */
	public AbstractMultiDirectedGraphFragment(AbstractMultiDirectedGraph<E,V> parent) {
		this.graph = parent;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V s, V t) {
		if (this.graph!=null && this.graph.areAdjacent(s, t))
			return super.addEdge(s, t);
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public V addVertex(V v) {
		if (this.graph!=null && this.graph.contains(v))
			return super.addVertex(v);
		
		return null;
	}
	
	/**
	 * Get original graph edge (these are different objects)
	 * @param e Edge in the fragment
	 * @return Edge in the original graph, <code>null</code> if edge was already removed in the original graph
	 */
	public E getOriginal(E e) {
		if (this.graph!=null)
			return this.graph.getEdge(e.getSource(), e.getTarget());
		
		return null;
	}
	
	/**
	 * Get original graph vertex (these are the same objects)
	 * @param v Vertex in the fragment
	 * @return Vertex in the original graph, <code>null</code> if vertex was already removed in the original graph
	 */
	public V getOriginal(V v) {
		if (this.graph!=null && this.graph.contains(v))
			return v;
		
		return null;
	}
	
	/**
	 * Get original graph
	 * @return Original graph of the fragment
	 */
	public AbstractMultiDirectedGraph<E, V> getOriginalGraph() {
		return this.graph;
	}
	
	/**
	 * Copy fragment from the original graph
	 */
	public void copyOriginalGraph() {
		if (this.graph == null) return;
		
		this.removeEdges(this.getEdges());
		this.removeVertices(this.getDisconnectedVertices());
		
		this.addVertices(this.graph.getDisconnectedVertices());
		Iterator<E> i = this.graph.getEdges().iterator();
		while (i.hasNext()) {
			E e = i.next();
			this.addEdge(e.getSource(), e.getTarget());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#checkEdge(java.util.Collection, java.util.Collection)
	 */
	@Override
	protected boolean checkEdge(Collection<V> ss, Collection<V> ts) {
		V v1 = (ss!=null && ss.size()>0) ? ss.iterator().next() : null;
		V v2 = (ts!=null && ts.size()>0) ? ts.iterator().next() : null;
		
		if (this.graph.areAdjacent(v1,v2))
			return false;
		else
			return true;
	}
}
