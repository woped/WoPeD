package de.hpi.bpt.graph.abs;

import java.util.Collection;

import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Abstract tree interface
 * 
 * @author Artem Polyvyanyy
 *
 * @param <E> template for edge (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public interface ITree <E extends IEdge<V>,V extends IVertex> {
	
	public V getRoot();
	
	public V reRoot(V v);
	
	public Collection<V> getChildren(V v);
	
	public Collection<V> getAllChildren(V v);
	
	public V getParent(V v);
	
	public Collection<V> getAllParents(V v);
	
	public E addChild(V parent, V child);
	
	public Collection<V> getLeaves();
	
	public Collection<V> getInternalNodes();

}
