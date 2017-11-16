package de.hpi.bpt.hypergraph.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Multi (same edges are allowed) hyper graph implementation
 * Hyper graph is collection of hyper edges and disconnected vertices
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for hyper edge (extends IHyperEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractMultiHyperGraph <E extends IHyperEdge<V>,V extends IVertex>
		extends AbstractGraphNotifier<E,V>
		implements IHyperGraph<E,V>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public E addEdge(V v) {
		E e = (E) new AbstractHyperEdge<V>(this);
		e.addVertex(v);
		
		return e;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#addEdge(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public E addEdge(Collection<V> vs) {
		E e = (E) new AbstractHyperEdge<V>(this);
		e.addVertices(vs);
		
		return e;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#removeEdge(de.hpi.bpt.hypergraph.abs.IHyperEdge)
	 */
	public E removeEdge(E e) {
		if (e == null) return null;
		if (this.contains(e)) {
			e.destroy();
			return e;
		}
		else return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#removeEdges(java.util.Collection)
	 */
	public Collection<E> removeEdges(Collection<E> es) {
		if (es == null) return null;
		Collection<E> result = new ArrayList<E>();
		Iterator<E> i = es.iterator();
		
		while (i.hasNext()) {
			E e = i.next();
			if (this.removeEdge(e) != null)
				result.add(e);
		}
		
		return (result.size() > 0) ? result : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#addVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public V addVertex(V v) {
		if (v == null) return null;
		if (this.contains(v)) return null;
		this.vertices.put(v, new HashSet<E>());
		
		return v;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#addVertices(java.util.Collection)
	 */
	public Collection<V> addVertices(Collection<V> vs) {
		if (vs == null) return null;
		Collection<V> result = new ArrayList<V>();
		
		Iterator<V> i = vs.iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (this.addVertex(v) != null)
				result.add(v);
		}
		
		return (result.size() > 0) ? result : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#removeVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public V removeVertex(V v) {
		if (v == null) return null;
		
		if (this.contains(v))
		{
			Collection<E> es = this.getEdges(v);
			Iterator<E> i = es.iterator();
			while (i.hasNext())
				i.next().removeVertex(v);
			
			this.vertices.remove(v);
			return v;
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#removeVertices(java.util.Collection)
	 */
	public Collection<V> removeVertices(Collection<V> vs) {
		if (vs == null || vs.size() == 0) return null;
	
		Collection<E> es = this.getEdges(vs.iterator().next());
		Iterator<E> i = es.iterator();
		while (i.hasNext())
			i.next().removeVertices(vs);
			
		return new ArrayList<V>(vs);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#getVertices()
	 */
	public Collection<V> getVertices() {
		Collection<V> result = this.vertices.keySet();
		return (result==null) ? (new ArrayList<V>()) : (new ArrayList<V>(result));
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#getEdges()
	 */
	public Collection<E> getEdges() {
		Collection<E> result = this.edges.keySet();
		return (result==null) ? (new ArrayList<E>()) : (new ArrayList<E>(result));
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#getEdges(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<E> getEdges(V v) {
		Collection<E> result = this.vertices.get(v);
		return (result==null) ? (new ArrayList<E>()) : (new ArrayList<E>(result));
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#getEdges(java.util.Collection)
	 */
	public Collection<E> getEdges(Collection<V> vs) {
		Collection<E> result = new ArrayList<E>();
		if (vs == null) return result;
		if (vs.size() == 0) return result;
		
		V v = vs.iterator().next();
		Collection<E> es = this.getEdges(v);
		Iterator<E> i = es.iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.connectsVertices(vs))
				result.add(e);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = "";
		
		Iterator<V> j = this.getDisconnectedVertices().iterator();
		while (j.hasNext()) {
			res += j.next();
		}
		
		Iterator<E> i = this.getEdges().iterator();
		while (i.hasNext())
			res += i.next();
		
		return res.trim();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#contains(de.hpi.bpt.hypergraph.abs.IHyperEdge)
	 */
	public boolean contains(E e) {
		return this.getEdges().contains(e);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#contains(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public boolean contains(V v) {
		return this.getVertices().contains(v);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#areAdjacent(java.util.Collection)
	 */
	public boolean areAdjacent(Collection<V> vs) {
		Iterator<E> i = this.getEdges().iterator();
		while (i.hasNext())
			if (i.next().connectsVertices(vs)) return true;
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#getConnectedVertices()
	 */
	public Collection<V> getConnectedVertices() {
		Set<V> result = new HashSet<V>();
		
		Iterator<E> i = this.getEdges().iterator();
		while (i.hasNext())
			result.addAll(i.next().getVertices());
		
		return new ArrayList<V>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#getDisconnectedVertices()
	 */
	public Collection<V> getDisconnectedVertices() {
		Collection<V> result = new ArrayList<V>(this.getVertices());
		result.removeAll(this.getConnectedVertices());
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#countEdges()
	 */
	public int countEdges() {
		return this.edges.size();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#countVertices()
	 */
	public int countVertices() {
		return this.vertices.size();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#getAdjacent(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getAdjacent(V v) {
		Collection<V> result = new ArrayList<V>();
		Iterator<E> i = this.getEdges(v).iterator();
		while (i.hasNext()) {
			result.addAll(i.next().getOtherVertices(v));
		}
		
		return new HashSet<V>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperGraph#isMultiGraph()
	 */
	public boolean isMultiGraph() {
		return true;
	}
	
	/**
	 * Check if set of vertices contains another set
	 * @param vs1 Collection of vertices
	 * @param vs2 Collection of vertices
	 * @return <code>true</code> if vs2 is contained in vs1, <code>false</code> otherwise
	 */
	protected boolean contains(Collection<V> vs1, Collection<V> vs2) {
		if (vs2==null || vs2.size()==0) return true;
		if (vs1==null || vs1.size()==0) return false;
		
		Collection<V> v3 = new ArrayList<V>(vs1);
		Iterator<V> i = vs2.iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (!v3.remove(v)) return false;
		}
		
		return true;
	}
	
	/**
	 * Check if edge with collection of vertices exists in the graph 
	 * @param vs Collection of vertices
	 * @return <code>true</code> if edge exists, <code>false</code> otherwise
	 */
	protected boolean checkEdge(Collection<V> vs) {
		Collection<E> es = this.getEdges(vs);
		if (es.size()>0) {
			Iterator<E> i = es.iterator();
			while (i.hasNext()) {
				E e = i.next();
				if (e.getVertices().size()==vs.size())
					return false;
			}
		}
		
		return true;
	}
}
