package de.hpi.bpt.graph.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Implementation of the multi graph fragment
 * Graph fragment is a collection of edges of the original graph
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class AbstractMultiGraphFragment<E extends IEdge<V>, V extends IVertex> extends AbstractMultiGraph<E, V> {
	
	protected Map<E,E> esMap = new HashMap<E,E>();
	protected IGraph<E, V> graph;
	
	/**
	 * Constructor 
	 * @param parent Parent graph of the fragment
	 */
	public AbstractMultiGraphFragment(IGraph<E,V> parent) {
		this.graph = parent;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public E addEdge(V v1, V v2) {
		if (this.graph==null) return null;

		// get edges of the original graph you can bind to
		Collection<E> edges = this.graph.getEdges(v1, v2);
		edges.removeAll(this.getOriginalEdges(v1, v2));
		if (edges.size()==0) return null;
		
		E e = super.addEdge(v1, v2);
		this.esMap.put(e, edges.iterator().next());
		
		return e;
	}
	
	public Map<E,E> getESMap() {
		return esMap;
	}
	
	/*private Collection<E> filterByIds(Collection<E> es, Collection<E> es2) {
		Collection<E> result = new ArrayList<E>();
		
		Iterator<E> i = es.iterator();
		while (i.hasNext()) {
			E e = i.next();
			
			boolean flag = false;
			Iterator<E> j = es2.iterator();
			while (j.hasNext()) {
				E nextE = j.next();
				if (nextE.getId().equals(e.getId())) {
					flag = true;
					break;
				}
			}
			
			if (!flag)
				result.add(e);
		}
		
		return result;
	}*/
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiHyperGraph#removeEdge(de.hpi.bpt.hypergraph.abs.IHyperEdge)
	 */
	@Override
	public E removeEdge(E e) {
		E result = super.removeEdge(e);
		this.esMap.remove(e);
		
		return result;
	}
	
	/**
	 * Add edge to the fragment even if it does not exist in the original graph
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return Edge added to the graph, <code>null</code> upon failure
	 */
	protected E doAddEdge(V v1, V v2) {
		if (this.graph == null) return null;
		
		E e = this.addEdge(v1, v2);
		if (e!=null) return e;
		
		return super.addEdge(v1,v2);
	}
	
	/**
	 * Add non fragment edge to the fragment (there is no link to original graph) 
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return Edge added to the graph, <code>null</code> upon failure
	 */
	protected E addNonFragmentEdge(V v1, V v2) {
		if (this.graph == null) return null;
		
		return super.addEdge(v1,v2);
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
	 * Get original graph edge represented by the edge in the fragment
	 * @param e Edge in the fragment
	 * @return Edge in the original graph
	 */
	public E getOriginal(E e) {
		// TODO make efficient (get methode has a bug??!!)
		Iterator<Entry<E,E>> it = this.esMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<E,E> pairs = it.next();
	        if (pairs.getKey().getId().equals(e.getId()))
	        	return pairs.getValue();
	    }
		
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
	public IGraph<E, V> getOriginalGraph() {
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
			this.addEdge(e.getV1(), e.getV2());
		}
	}
	
	/**
	 * Get fragment complementary
	 * @return Complementary of the fragment
	 */
	public  AbstractMultiGraphFragment<E,V> getComplementary() {
		AbstractMultiGraphFragment<E,V> result = new AbstractMultiGraphFragment<E,V>(this.graph);
		if (this.graph == null) return result;
		
		Collection<E> es = this.graph.getEdges();
		
		Iterator<E> i = this.getEdges().iterator();
		while (i.hasNext()) {
			es.remove(this.getOriginal(i.next()));
		}
		
		i = es.iterator();
		while (i.hasNext()) {
			E e = i.next();
			result.addEdge(e.getV1(), e.getV2());
		}
		
		return result;
	}
	
	/**
	 * Get original graph edges between given pair of vertices
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return Collection of graph edges between given pair of vertices
	 */
	protected Collection<E> getOriginalEdges(V v1, V v2) {
		Collection<E> result = new ArrayList<E>();
		
		Iterator<E> i = this.esMap.values().iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.connectsVertices(v1, v2))
				result.add(e);
		}
		
		return result;
	}

	public Collection<E> getOriginalEdges() {
		return esMap.values();
	}
	
	public Collection<E> getOriginalEdges(V v) {
		Collection<E> result = new ArrayList<E>();
		
		Iterator<E> i = this.esMap.values().iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (e.connectsVertex(v))
				result.add(e);
		}
		
		return result;
	}
}
