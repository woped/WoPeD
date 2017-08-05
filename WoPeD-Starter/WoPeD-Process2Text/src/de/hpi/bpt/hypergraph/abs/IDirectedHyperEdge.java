package de.hpi.bpt.hypergraph.abs;

import java.util.Collection;

/**
 * Interface describing directed hyper edge behavior
 * Directed hyper edge is composed of two directed sets of vertices
 * 
 * @author Artem Polyvyanyy
 *
 * @param <V> Vertex type employed in the edge
 */
public interface IDirectedHyperEdge<V extends IVertex> extends IHyperEdge<V> {
	
	/**
	 * Add source vertex to the edge
	 * @param v Source vertex to add
	 * @return Source vertex added to the edge, <code>null</code> upon failure
	 */
	public V addSourceVertex(V v);
	
	/**
	 * Add collection of source vertices to the edge
	 * @param vs Collection of source vertices to add
	 * @return Collection of source vertices added to the edge, <code>null</code> if no source vertex was added
	 */
	public Collection<V> addSourceVertices(Collection<V> vs);
	
	/**
	 * Add target vertex to the edge
	 * @param v Target vertex to add
	 * @return Target vertex added to the edge, <code>null</code> upon failure
	 */
	public V addTargetVertex(V v);
	
	/**
	 * Add collection of target vertices to the edge
	 * @param vs Collection of target vertices to add
	 * @return Collection of target vertices added to the edge, <code>null</code> if no target vertex was added
	 */
	public Collection<V> addTargetVertices(Collection<V> vs);
	
	/**
	 * Add source and target vertices to the edge
	 * @param ss Source vertices
	 * @param ts Target vertices
	 * @return Collection of vertices added to the edge, <code>null</code> upon failure
	 */
	public Collection<V> addSourceAndTagetVertices(Collection<V> ss, Collection<V> ts);
	
	/**
	 * Remove source vertex from the edge
	 * @param v Source vertex to remove
	 * @return Source vertex that was removed, <code>null</code> upon failure
	 */
	public V removeSourceVertex(V v);
	
	/**
	 * Remove collection of source vertices from the edge
	 * @param vs Collection of source vertices to remove
	 * @return Collection of source vertices removed from the edge, <code>null</code> if no source vertex was removed
	 */
	public Collection<V> removeSourceVertices(Collection<V> vs);
	
	/**
	 * Remove target vertex from the edge
	 * @param v Target vertex to remove
	 * @return Target vertex that was removed, <code>null</code> upon failure
	 */
	public V removeTargetVertex(V v);
	
	/**
	 * Remove collection of target vertices from the edge
	 * @param vs Collection of target vertices to remove
	 * @return Collection of target vertices removed from the edge, <code>null</code> if no target vertex was removed
	 */
	public Collection<V> removeTargetVertices(Collection<V> vs);
	
	/**
	 * Remove source and target vertices from the edge
	 * @param ss Source vertices
	 * @param ts Target vertices
	 * @return Collection of vertices removed from the edge, <code>null</code> upon failure
	 */
	public Collection<V> removeSourceAndTagetVertices(Collection<V> ss, Collection<V> ts);
	
	/**
	 * Get other source vertices
	 * @param v Vertex
	 * @return Return other source vertices as 'v'
	 */
	public Collection<V> getOtherSourceVertices(V v);
	
	/**
	 * Get other source vertices
	 * @param vs Vertices
	 * @return Other source vertices as 'vs'
	 */
	public Collection<V> getOtherSourceVertices(Collection<V> vs);
	
	/**
	 * Get other target vertices
	 * @param v Vertex
	 * @return Other target vertices as 'v'
	 */
	public Collection<V> getOtherTargetVertices(V v);
	
	/**
	 * Get other target vertices
	 * @param v Vertex
	 * @return Other target vertices as 'v'
	 */
	public Collection<V> getOtherTargetVertices(Collection<V> vs);
	
	/**
	 * Get source vertices
	 * @return Source vertices
	 */
	public Collection<V> getSourceVertices();
	
	/**
	 * Get target vertices
	 * @return Target vertices
	 */
	public Collection<V> getTargetVertices();
	
	/**
	 * Check if the edge has at least one source vertex
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean hasSource();
	
	/**
	 * Check if the edge has source vertex 'v'
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean hasSource(V v);
	
	/**
	 * Check if the edge has source vertices 'vs'
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean hasSources(Collection<V> vs);
	
	/**
	 * Check if the edge has at least one target vertex
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean hasTarget();
	
	/**
	 * Check if the edge has target vertex 'v'
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean hasTarget(V v);
	
	/**
	 * Check if the edge has target vertices 'vs'
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean hasTargets(Collection<V> vs);
}
