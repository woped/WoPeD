package de.hpi.bpt.graph.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.graph.abs.AbstractMultiGraphFragment;
import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Collection of graph algorithms
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class GraphAlgorithms<E extends IEdge<V>,V extends IVertex> {
	
	/**
	 * Get graph boundary vertices (vertices that are included only into one edge)
	 * @param g Graph
	 * @return Collection of boundary vertices of the graph
	 */
	public Collection<V> getBoundaryVertices(IGraph<E,V> g)
	{
		Collection<V> result = new ArrayList<V>();
		
		Iterator<V> i = g.getVertices().iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (g.getEdges(v).size()==1)
				result.add(v);
		}
		
		return result;
	}
	
	/**
	 * Check if graph is connected (there is a path between any pair of vertices)
	 * @param g Graph
	 * @return true if graph is connected, false otherwise
	 */
	public boolean isConnected(IGraph<E,V> g)
	{
		if (g.countVertices()==0) return true;
		
		V x = g.getVertices().iterator().next(); 
		Collection<V> L = new ArrayList<V>();
		Collection<V> K = new ArrayList<V>();
		L.add(x); K.add(x);
		
		while (K.size()>0) {
			V y = K.iterator().next();
			K.remove(y);

			Iterator<V> j = g.getAdjacent(y).iterator();
			while (j.hasNext()) {
				V z = j.next();
				if (!L.contains(z)) {
					L.add(z);
					K.add(z);
				}
			}
		}
		
		return (g.countVertices()==L.size()) ? true : false;
	}
	
	/**
	 * Check if graph is v-connected
	 * Connected if any combination of v vertices is removed
	 * 
	 * @param g Graph
	 * @param v Number of vertices
	 * @return <code>true</code> if graph is v-connected, <code>false</code> otherwise
	 */
	public boolean isConnected(IGraph<E,V> g, int v) {
		if (g == null) return false;
		Collection<V> vs = g.getVertices();
		
		if (v<=0) return this.isConnected(g);
		
		if (v > vs.size()) return false;
		
		CombinationGenerator<V> cgv = new CombinationGenerator<V>(vs,v);
		
		// do extensive search
		AbstractMultiGraphFragment<E,V> gf = new AbstractMultiGraphFragment<E,V>(g);
		while (cgv.hasMore()) {
			Collection<V> cvs = cgv.getNextCombination();
			
			gf.copyOriginalGraph();
			
			gf.removeVertices(cvs);
			
			if (!this.isConnected(gf)) return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param g Graph
	 * @param nv 
	 * @return
	 */
	public Collection<V> getSeparationSet(IGraph<E,V> g, int nv) {
		if (nv<0) return null;
		Collection<V> vs = g.getVertices();
		if (nv > vs.size()) return null;
		CombinationGenerator<V> cgv = new CombinationGenerator<V>(vs,nv);
		
		AbstractMultiGraphFragment<E,V> gf = new AbstractMultiGraphFragment<E,V>(g);
		while (cgv.hasMore()) {
			Collection<V> cvs = cgv.getNextCombination();

			gf.copyOriginalGraph();
			
			gf.removeVertices(cvs);
			
			if (!this.isConnected(gf)) return cvs;
		}
		
		return null;
	}
	
	/**
	 * Get separation set of vertices with more than 2 incident edges (gateway vertices) 
	 * @param g Graph
	 * @param nv Number of vertices
	 * @return Collection of vertices of size 'nv' that make graph 'g' disconnected, <code>null</code> if no such set exists
	 */
	public Collection<V> getSJSeparationSet(IGraph<E,V> g, int nv) {
		if (nv<0) return null;
		
		Collection<V> vs = g.getVertices();
		Collection<V> gs = new ArrayList<V>();
		Iterator<V> i = vs.iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (g.getEdges(v).size()>2) gs.add(v);
		}
		
		if (nv > gs.size()) return null;
		CombinationGenerator<V> cgv = new CombinationGenerator<V>(gs,nv);
		
		AbstractMultiGraphFragment<E,V> f = new AbstractMultiGraphFragment<E,V>(g);
		while (cgv.hasMore()) {
			Collection<V> cvs = cgv.getNextCombination();

			f.copyOriginalGraph();
			
			f.removeVertices(cvs);
			
			if (!this.isConnected(f)) return cvs;
			
			// 2 node specific
			if (nv == 2){
				Iterator<V> j = cvs.iterator();
				if (g.getEdges(j.next(), j.next()).size()>1)
					return cvs;
			}
		}
		
		return null;
	}
	
	public Collection<V> getSJSeparationSet(IGraph<E,V> g, Collection<V> vs, int nv) {
		if (nv<0) return null;
		
		Collection<V> gs = new ArrayList<V>();
		Iterator<V> i = vs.iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (g.getEdges(v).size()>2) gs.add(v);
		}
		
		Collection<Collection<V>> rs = new ArrayList<Collection<V>>();
		if (nv > gs.size()) return null;
		CombinationGenerator<V> cgv = new CombinationGenerator<V>(gs,nv);
		
		AbstractMultiGraphFragment<E,V> gf = new AbstractMultiGraphFragment<E,V>(g);
		while (cgv.hasMore()) {
			Collection<V> cvs = cgv.getNextCombination();

			gf.copyOriginalGraph();
			
			gf.removeVertices(cvs);
			
			if (!this.isConnected(gf)) rs.add(cvs);
		}
		
		// find pair that contains all other pairs
		Iterator<Collection<V>> ci = rs.iterator();
		Collection<V> result = new ArrayList<V>();
		while (ci.hasNext()) {
			Collection<V> pair = ci.next();
			
			AbstractMultiGraphFragment<E,V> gf1 = this.getConnectedFragment(g, pair);
			AbstractMultiGraphFragment<E,V> gf2 = gf1.getComplementary();
			
			if (gf1.getVertices().size() == vs.size()) return pair;
			if (gf2.getVertices().size() == vs.size()) return pair;
		}
		
		return result;
	}
	
	/**
	 * Get random connected fragment of the graph
	 * Do not consider specified vertices
	 * @param g Graph
	 * @param vs Collection of vertices to not consider
	 * @return Some connected fragment of a graph 
	 */
	public AbstractMultiGraphFragment<E,V> getConnectedFragment(IGraph<E,V> g, Collection<V> vs) {
		AbstractMultiGraphFragment<E,V> result = new AbstractMultiGraphFragment<E,V>(g);
		
		Collection<V> vertices = g.getVertices();
		vertices.removeAll(vs);
		
		if (vertices.size()==0) return result;
		
		V x = vertices.iterator().next(); 
		Collection<V> L = new ArrayList<V>();
		Collection<V> K = new ArrayList<V>();
		L.add(x); K.add(x);
		
		while (K.size()>0) {
			V y = K.iterator().next();
			K.remove(y);

			Iterator<V> j = g.getAdjacent(y).iterator();
			while (j.hasNext()) {
				V z = j.next();
				if (!L.contains(z)) {
					L.add(z);
					if (!vs.contains(z)) K.add(z);
				}
				
				if (result.getEdge(y, z)==null)
					result.addEdge(y, z);
				
			}
		}
		
		return result;
	}
}
