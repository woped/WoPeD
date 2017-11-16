package de.hpi.bpt.graph.algo.tctree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.graph.abs.AbstractMultiGraphFragment;
import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * Implementation of SPQR-tree fragment skeleton
 * SPQR-tree skeleton gives a basic structure to the triconnected component of the graph
 * along with relations of this component with other skeletons
 *  
 * @author Artem Polyvyanyy
 * 
 * @param <E> template for edge (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class TCTreeSkeleton<E extends IEdge<V>, V extends IVertex> extends AbstractMultiGraphFragment<E,V> {
	
	public static String VIRTUAL_EDGE_LABEL = "virtual";
	
	/**
	 * Constructor
	 * @param g Parent graph of the skeleton
	 */
	public TCTreeSkeleton(IGraph<E, V> g) {
		super(g);
	}

	/**
	 * Add virtual edge to the skeleton
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return Edge added to the skeleton
	 */
	public E addVirtualEdge(V v1, V v2) {
		E e = super.addNonFragmentEdge(v1, v2);
		
		// mark edge virtual
		e.setDescription(TCTreeSkeleton.VIRTUAL_EDGE_LABEL);
		
		return e;
	}
	
	/**
	 * Get all virtual edges of the skeleton
	 * @return Collection of virtual edges of the skeleton
	 */
	public Collection<E> getVirtualEdges() {
		Collection<E> result = new ArrayList<E>();
		
		Iterator<E> i = this.getEdges().iterator();
		while (i.hasNext()) {
			E e = i.next();
			if (this.isVirtual(e))
				result.add(e);
		}
		
		return result;
	}
	
	/**
	 * Check if edge is virtual
	 * @param e Edge
	 * @return <code>true</code> if edge is virtual, <code>false</code> otherwise
	 */
	public boolean isVirtual(E e) {
		return e.getDescription().equals(TCTreeSkeleton.VIRTUAL_EDGE_LABEL);
	}


	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractMultiGraphFragment#copyOriginalGraph()
	 */
	@Override
	public void copyOriginalGraph() {
		if (this.graph == null) return;
		
		this.removeEdges(this.getEdges());
		this.removeVertices(this.getDisconnectedVertices());
		
		this.addVertices(this.graph.getDisconnectedVertices());
		Iterator<E> i = this.graph.getEdges().iterator();
		while (i.hasNext()) {
			E e = i.next();
			E newE = this.addEdge(e.getV1(), e.getV2());
			newE.setDescription(e.getDescription());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractMultiGraphFragment#getComplementary()
	 */
	@Override
	public TCTreeSkeleton<E, V> getComplementary() {
		TCTreeSkeleton<E,V> result = new TCTreeSkeleton<E,V>(this.graph);
		if (this.graph == null) return result;
		
		Collection<E> es = this.graph.getEdges();
		
		Iterator<E> i = this.getEdges().iterator();
		while (i.hasNext()) {
			es.remove(this.getOriginal(i.next()));
		}
		
		i = es.iterator();
		while (i.hasNext()) {
			E e = i.next();
			E newE = result.addEdge(e.getV1(), e.getV2());
			newE.setDescription(e.getDescription());
		}
		
		return result;
	}
}
