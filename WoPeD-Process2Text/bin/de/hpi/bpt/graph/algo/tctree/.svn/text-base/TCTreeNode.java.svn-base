package de.hpi.bpt.graph.algo.tctree;

import java.util.ArrayList;
import java.util.Collection;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.hypergraph.abs.IVertex;
import de.hpi.bpt.hypergraph.abs.Vertex;

public class TCTreeNode<E extends IEdge<V>, V extends IVertex> extends Vertex {

	protected TCType type = TCType.UNDEFINED;
	
	protected TCTreeSkeleton<E,V> skeleton;
	
	protected Collection<V> boundary = new ArrayList<V>();
	
	/**
	 * Constructor
	 */
	public TCTreeNode() {
		super();
	}
	
	/**
	 * Constructor
	 * @param name Node name
	 */
	public TCTreeNode(String name) {
		super(name);
	}

	public TCType getType() {
		return type;
	}

	protected void setType(TCType type) {
		this.type = type;
	}
	
	public TCTreeSkeleton<E,V> getSkeleton() {
		return this.skeleton;
	}

	protected void setSkeleton(TCTreeSkeleton<E,V> skeleton) {
		this.skeleton = skeleton;
	}
	
	public Collection<V> getBoundaryNodes() {
		return new ArrayList<V>(this.boundary);
	}

	protected void setBoundaryNodes(Collection<V> boundary) {
		if (boundary == null || boundary.size()!=2) return;
		
		this.boundary = new ArrayList<V>(boundary);
	}
	
	@Override
	public String toString() {
		return this.getName() + " " + this.getBoundaryNodes() + " - " + this.getSkeleton() + " - " + this.getSkeleton().getVirtualEdges();
	}

}
