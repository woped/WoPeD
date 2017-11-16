package de.hpi.bpt.graph.algo.rpst;

import java.util.ArrayList;
import java.util.Collection;

import de.hpi.bpt.graph.abs.AbstractDirectedEdge;
import de.hpi.bpt.graph.abs.AbstractDirectedGraph;
import de.hpi.bpt.graph.abs.IDirectedEdge;
import de.hpi.bpt.hypergraph.abs.IVertex;

public class RPSTSkeleton<E extends IDirectedEdge<V>, V extends IVertex>
			extends AbstractDirectedGraph<E,V>
{
	private Collection<Collection<V>> vEdges = new ArrayList<Collection<V>>();
	
	@Override
	public E addEdge(V from, V to) {
		if (from == null || to == null) return null;
		
		Collection<V> ss = new ArrayList<V>(); ss.add(from);
		Collection<V> ts = new ArrayList<V>(); ts.add(to);
		
		if (!this.checkEdge(ss, ts)) return null;
		
		AbstractDirectedEdge<V> abstractDirectedEdge = new AbstractDirectedEdge<V>(this, from, to);
		return (E)abstractDirectedEdge;
	}
		
	public void addVirtualEdge(V v1, V v2) {
		Collection<V> edge = new ArrayList<V>();
		edge.add(v1);
		edge.add(v2);
		vEdges.add(edge);
	}
	
	@Override
	public E removeEdge(E e) {
		vEdges.remove(e);
		return super.removeEdge(e);
	}
	
	public Collection<Collection<V>> getVirtualEdges() {
		return this.vEdges;
	}
	
	public boolean isVirtual(E e) {
		return vEdges.contains(e);
	}
}
