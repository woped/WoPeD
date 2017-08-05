package de.hpi.bpt.hypergraph.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Multi directed hyper graph implementation
 * Multi directed hyper graph is collection of directed hyper edges and disconnected vertices
 * Multi edges are allowed
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for hyper edge (extends IDirectedHyperEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractMultiDirectedHyperGraph <E extends IDirectedHyperEdge<V>,V extends IVertex> 
				extends AbstractMultiHyperGraph<E,V>
				implements IDirectedHyperGraph<E,V> {
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public E addEdge(V s, V t) {
		if (s == null || t == null) return null;
		
		E e = (E) new AbstractDirectedHyperEdge<V>(this);
		Collection<V> ss = new ArrayList<V>(); ss.add(s);
		Collection<V> ts = new ArrayList<V>(); ts.add(t);
		e.addSourceAndTagetVertices(ss, ts);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#addEdge(java.util.Collection, java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public E addEdge(Collection<V> ss, Collection<V> ts) {
		if (ss == null || ts == null) return null;
		
		E e = (E) new AbstractDirectedHyperEdge<V>(this);
		e.addSourceAndTagetVertices(ss, ts);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getEdgesWithSource(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<E> getEdgesWithSource(V v) {
		Collection<E> result = new ArrayList<E>();
		
		Collection<E> es = this.getEdges(v);
		Iterator<E> i = es.iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.hasSource(v))
				result.add(e);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getEdgesWithSourceAndTarget(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<E> getEdgesWithSourceAndTarget(V s, V t) {
		Collection<E> result = new ArrayList<E>();
		
		Collection<E> es = this.getEdges(s);
		Iterator<E> i = es.iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.hasSource(s) && e.hasTarget(t))
				result.add(e);
		}
		
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getEdgesWithSourcesAndTargets(java.util.Collection, java.util.Collection)
	 */
	public Collection<E> getEdgesWithSourcesAndTargets(Collection<V> ss, Collection<V> ts) {
		Collection<E> result = new ArrayList<E>();
		if (ss == null && ts == null) return result;
		if (ss.size() == 0 && ts.size() == 0) return result;
		
		if (ss != null && ss.size() > 0)
		{
			V v = ss.iterator().next();
			Collection<E> es = this.getEdgesWithSource(v);
			if (es==null) return result;
			Iterator<E> i = es.iterator();
			while (i.hasNext()) {
				E e = i.next();
				if (e.hasSources(ss) && e.hasTargets(ts))
					result.add(e);
			}
		}
		else if (ts != null && ts.size() > 0) {
			V v = ts.iterator().next();
			Collection<E> es = this.getEdgesWithTarget(v);
			if (es==null) return result;
			Iterator<E> i = es.iterator();
			while (i.hasNext()) {
				E e = i.next();
				if (e.hasSources(ss) && e.hasTargets(ts))
					result.add(e);
			}
		}
		
		return result;	
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getEdgesWithSources(java.util.Collection)
	 */
	public Collection<E> getEdgesWithSources(Collection<V> vs) {
		Collection<E> result = new ArrayList<E>();
		if (vs==null || vs.size()==0) return result;
		
		Iterator<E> i = this.getEdges(vs.iterator().next()).iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.hasSources(vs))
				result.add(e);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getEdgesWithTarget(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<E> getEdgesWithTarget(V v) {
		Collection<E> result = new ArrayList<E>();
		
		Iterator<E> i = this.getEdges(v).iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.hasTarget(v))
				result.add(e);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getEdgesWithTargets(java.util.Collection)
	 */
	public Collection<E> getEdgesWithTargets(Collection<V> vs) {
		if (vs==null || vs.size()==0) return null;
		Collection<E> result = new ArrayList<E>();
		
		Iterator<E> i = this.getEdges(vs.iterator().next()).iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.hasTargets(vs))
				result.add(e);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getIncomingEdges(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<E> getIncomingEdges(V v) {
		return this.getEdgesWithTarget(v);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getFirstIncomingEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public E getFirstIncomingEdge(V v) {
		Collection<E> es = this.getIncomingEdges(v);
		if (es.size() == 0) return null;
		return es.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getOutgoingEdges(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<E> getOutgoingEdges(V v) {
		return this.getEdgesWithSource(v);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getFirstOutgoingEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public E getFirstOutgoingEdge(V v) {
		Collection<E> es = this.getOutgoingEdges(v);
		if (es.size() == 0) return null;
		return es.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getPredecessors(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getPredecessors(V v) {
		Set<V> result = new HashSet<V>();
		
		Collection<E> es = this.getIncomingEdges(v);
		Iterator<E> i = es.iterator();
		while (i.hasNext())
			result.addAll(i.next().getSourceVertices());
		
		return new ArrayList<V>(result);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getFirstPredecessor(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public V getFirstPredecessor(V v) {
		Collection<V> vs = this.getPredecessors(v);
		if (vs.size() == 0) return null;
		return vs.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getSuccessors(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getSuccessors(V v) {
		Set<V> result = new HashSet<V>();
		
		Collection<E> es = this.getOutgoingEdges(v);
		Iterator<E> i = es.iterator();
		while (i.hasNext())
			result.addAll(i.next().getTargetVertices());
		
		return new ArrayList<V>(result);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperGraph#getFirstSuccessor(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public V getFirstSuccessor(V v) {
		Collection<V> vs = this.getSuccessors(v);
		if (vs.size() == 0) return null;
		return vs.iterator().next();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(java.util.Collection)
	 */
	@Override
	public E addEdge(Collection<V> vs) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V v) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#isMultiGraph()
	 */
	@Override
	public boolean isMultiGraph() {
		return true;
	}
	
	/**
	 * Check if edge with collection of vertices exists in the graph 
	 * @param vs Collection of vertices
	 * @return <code>true</code> if edge exists, <code>false</code> otherwise
	 */
	protected boolean checkEdge(Collection<V> ss, Collection<V> ts) {
		Collection<E> es = this.getEdgesWithSourcesAndTargets(ss, ts);
		if (es.size()>0) {
			Iterator<E> i = es.iterator();
			while (i.hasNext()) {
				E e = i.next();
				if (e.getSourceVertices().size()==ss.size() && e.getTargetVertices().size()==ts.size())
					return false;
			}
		}
		
		return true;
	}
}

