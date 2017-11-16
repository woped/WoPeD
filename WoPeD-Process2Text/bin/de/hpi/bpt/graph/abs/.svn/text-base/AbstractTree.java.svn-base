package de.hpi.bpt.graph.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.graph.algo.GraphAlgorithms;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Abstract tree implementation
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractTree<E extends IDirectedEdge<V>,V extends IVertex> extends AbstractDirectedGraph<E,V> 
		implements ITree<E,V> {
	
	private GraphAlgorithms<E, V> graphAlgorithms = new GraphAlgorithms<E, V>();
	
	private V root = null;

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V v1, V v2) {
		if (this.contains(v1) && this.contains(v2)) return null;
		
		if (this.countEdges()==0) {
			E e = super.addEdge(v1, v2);
			if (e!=null) this.root = v1;
			return e;
		}
		
		if (this.contains(v1) && !this.contains(v2))
			return super.addEdge(v1, v2);
		else if (!this.contains(v1) && this.contains(v2))
			return super.addEdge(v2, v1);
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractMultiGraph#removeVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public V removeVertex(V v) {
		
		Collection<V> vs = this.getAllChildren(v);
		Iterator<V> i = vs.iterator();
		while (i.hasNext())
			super.removeVertex(i.next());
		
		v = super.removeVertex(v);
		if (v!=null && v.equals(this.root))
			this.root = null;
		
		return v;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractMultiGraph#removeVertices(java.util.Collection)
	 */
	@Override
	public Collection<V> removeVertices(Collection<V> vs) {
		Collection<V> result = new ArrayList<V>(vs);
		result.retainAll(this.getVertices());
		
		Iterator<V> i = result.iterator();
		while (i.hasNext())
			result.add(this.removeVertex(i.next()));
			
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#removeEdge(de.hpi.bpt.hypergraph.abs.IHyperEdge)
	 */
	/*@Override
	public E removeEdge(E e) {
		V v1 = this.removeVertex(e.getSource());
		
		return (v1!=null) ? e : null;
	}*/

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#removeEdges(java.util.Collection)
	 */
	/*@Override
	public Collection<E> removeEdges(Collection<E> es) {
		Collection<E> result = new ArrayList<E>(es);
		result.retainAll(this.getEdges());
		
		Iterator<E> i = es.iterator();
		while (i.hasNext())
			result.add(this.removeEdge(i.next()));
			
		return result;
	}*/

	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#addChild(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public E addChild(V parent, V child) {
		if (this.contains(parent) && ! this.contains(child)) {
			return super.addEdge(parent, child);
		}
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#getAllChildren(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getAllChildren(V v) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#getAllParents(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getAllParents(V v) {
		Collection<V> result = new ArrayList<V>();
		if (v==null || v.equals(this.root)) return result;
		
		V p = this.getParent(v);
		do {
			result.add(p);
			p = this.getParent(p);
		} while (p!=null && !p.equals(this.root));
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#getChildren(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getChildren(V v) {
		return this.getSuccessors(v);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#getInternalNodes()
	 */
	public Collection<V> getInternalNodes() {
		Collection<V> result = this.getVertices();
		result.removeAll(this.getLeaves());
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#getLeaves()
	 */
	public Collection<V> getLeaves() {
		return graphAlgorithms.getBoundaryVertices(this);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#getParent(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public V getParent(V v) {
		return this.getFirstPredecessor(v);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#getRoot()
	 */
	public V getRoot() {
		return this.root;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.ITree#reRoot(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public V reRoot(V v) {
		// TODO Auto-generated method stub
		return null;
	}
}
