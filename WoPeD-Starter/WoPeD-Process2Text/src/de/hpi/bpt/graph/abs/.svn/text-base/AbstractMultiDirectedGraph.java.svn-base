package de.hpi.bpt.graph.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Multi (same edges are allowed) directed graph implementation
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IDirectedEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractMultiDirectedGraph<E extends IDirectedEdge<V>, V extends IVertex> 
		extends AbstractMultiDirectedHyperGraph<E,V> 
		implements IDirectedGraph<E,V>, IGraph<E,V> {

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.IGraph#areAdjacent(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public boolean areAdjacent(V v1, V v2) {
		E e = this.getEdge(v1, v2);
		
		return (e!=null) ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(java.util.Collection, java.util.Collection)
	 */
	@Override
	public E addEdge(Collection<V> ss, Collection<V> ts) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#isMultiGraph()
	 */
	@Override
	public boolean isMultiGraph() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#removeVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public V removeVertex(V v) {
		if (v == null) return null;
		
		if (this.contains(v))
		{
			Collection<E> es = this.getEdges(v);
			Iterator<E> i = es.iterator();
			while (i.hasNext())
				this.removeEdge(i.next());
			
			this.vertices.remove(v);
			return v;
		}
		
		return null;
	}


	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#removeVertices(java.util.Collection)
	 */
	@Override
	public Collection<V> removeVertices(Collection<V> vs) {
		Collection<V> result = new ArrayList<V>();
		
		Iterator<V> i = vs.iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (this.removeVertex(v) != null)
				result.add(v);
		}
		return (result.size()>0) ? result : null;
	}

	public E getEdge(V v1, V v2) {
		Collection<E> es = this.vertices.get(v1);
		if (es == null) return null;
		
		Iterator<E> i = es.iterator();
		while(i.hasNext()) {
			E e = i.next();
			if (e.connectsVertex(v1) && e.connectsVertex(v2))
				return e;
		}
		
		return null;
	}
	
	public E getDirectedEdge(V v1, V v2) {
		Collection<E> es = this.vertices.get(v1);
		if (es == null) return null;
		
		Iterator<E> i = es.iterator();
		while(i.hasNext()) {
			E e = i.next();
			if (e.getSource().equals(v1) && e.getTarget().equals(v2))
				return e;
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.IGraph#getEdges(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<E> getEdges(V v1, V v2) {
		if (v1 == null || v2 == null) return new ArrayList<E>();
		Collection<V> vs = new ArrayList<V>();
		vs.add(v1); vs.add(v2);
		
		return this.getEdges(vs);
	}
}
