package de.hpi.bpt.graph.algo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hpi.bpt.graph.abs.IDirectedEdge;
import de.hpi.bpt.graph.abs.IDirectedGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Collection of directed graph algorithms
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IDirectedEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class DirectedGraphAlgorithms<E extends IDirectedEdge<V>,V extends IVertex> {
	
	/**
	 * Get boundary vertices in the directed graph - vertices without predecessors or successors 
	 * @param g Directed graph
	 * @return Collection of boundary vertices
	 */
	public Collection<V> getBoundaryVertices(IDirectedGraph<E, V> g)
	{
		Collection<V> result = new ArrayList<V>();
		
		Iterator<V> i = g.getVertices().iterator();
		while (i.hasNext()) {
			V v = i.next();
			int in = g.getIncomingEdges(v).size();
			int out = g.getOutgoingEdges(v).size();
			if (in==0 || out==0) result.add(v);
		}
		
		return result;
	}
	
	/**
	 * Get directed graph input vertices
	 * @param g Directed graph
	 * @return Collection of graph input vertices
	 */
	public Collection<V> getInputVertices(IDirectedGraph<E, V> g)
	{
		Collection<V> result = new ArrayList<V>();
		Iterator<V> i = this.getBoundaryVertices(g).iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (g.getPredecessors(v).size()==0)
				result.add(v);
		}
		
		return result;
	}
	
	/**
	 * Get directed graph output vertices
	 * @param g Directed graph
	 * @return Collection of graph output vertices
	 */
	public Collection<V> getOutputVertices(IDirectedGraph<E, V> g)
	{
		Collection<V> result = new ArrayList<V>();
		Iterator<V> i = this.getBoundaryVertices(g).iterator();
		while (i.hasNext()) {
			V v = i.next();
			if (g.getSuccessors(v).size()==0)
				result.add(v);
		}
		
		return result;
	}
	
	/**
	 * Check if directed graph has cycles
	 * @param g Directed graph
	 * @return <code>true</code> if graph has a cycle, <code>false</code> otherwise
	 */
	public boolean hasCycles(IDirectedGraph<E, V> g) {
		TransitiveClosure<E, V> tc = new TransitiveClosure<E,V>(g);
		for (V v : g.getVertices())
			if (tc.isInLoop(v))
				return true;
		
		return false;
	}
	
	/**
	 * Check if directed graph has a path between the given nodes
	 * 
	 * @param Directed graph
	 * @param source node
	 * @param target node
	 * @return true, if there is a path from the source node to the target node in the directed graph
	 */
	public boolean hasPath(IDirectedGraph<E, V> g, V from, V to) {
		TransitiveClosure<E, V> tc = new TransitiveClosure<E,V>(g);
		return tc.hasPath(from, to);
	}

	/**
	 * Simple implementation of an algorithm to derive dominators and postdominators of a 
	 * directed graph. It uses the iterative approach which is simple but not really efficient, 
	 * it requires polynomial time. Could be done in linear time though. 
	 * 
	 * 
	 * @param the directed graph
	 * @param postDominators boolean parameter, if set the postdominators instead of dominators are computed
	 * @return a map comprising for each vertex the set of its dominators (or postdominators, respectively)
	 */
	public Map<V,Set<V>> getDominators(IDirectedGraph<E, V> g, boolean postDominators) {
		List<V> vList = new ArrayList<V>(g.getVertices());
		
		Collection<V> initV = postDominators ? this.getOutputVertices(g) : this.getInputVertices(g);
		
		int size = vList.size(); 
		final BitSet[] dom = new BitSet[size];
		final BitSet ALL = new BitSet(size);
		
		for (int i = 0; i < size; i++) ALL.set(i);

		for (int i = 0; i < size; i++) {
			BitSet curDoms = new BitSet(size);
			dom[i] = curDoms;

			if (!initV.contains(vList.get(i))) curDoms.or(ALL);
			else curDoms.set(i);
		}
		
		boolean changed = true;
	
		/*
		 * While we change the dom relation for a node
		 */
		while (changed) {
			changed = false;
			for (int i = 0; i < size; i++) {
				if (initV.contains(vList.get(i))) continue;
				 
				final BitSet old = dom[i];
				final BitSet curDoms = new BitSet(size);
				curDoms.or(old);
				
				Collection<V> predecessors = postDominators ? g.getSuccessors(vList.get(i)) : g.getPredecessors(vList.get(i));
				for (V v : predecessors)
					curDoms.and(dom[vList.indexOf(v)]);
				
				curDoms.set(i);
				
				if (!curDoms.equals(old)) {
					changed = true;
					dom[i] = curDoms;
				}
			}
		}
		
		/*
		 * Create the data structure that we want to return
		 * 
		 * The quadratic time complexity of building up this structure does not hurt
		 * as the above algorithm requires more time anyways
		 */
		Map<V,Set<V>> dominators = new HashMap<V, Set<V>>();
		for (int i = 0; i < size; i++) {
			dominators.put(vList.get(i), new HashSet<V>());
			for (int j = 0; j < size; j++)
				if (dom[i].get(j))
					dominators.get(vList.get(i)).add(vList.get(j));
		}
		
		return dominators;
	}

	
}
