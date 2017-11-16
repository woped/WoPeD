package de.hpi.bpt.graph.algo.bctree;

import java.util.Collection;
import java.util.Vector;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.hypergraph.abs.IVertex;

public class BCTreeNode<E extends IEdge<V>, V extends IVertex> {
	private BCType nodeType;
	
	private BCTreeNode<E,V> parentNode;
	private Vector<BCTreeNode<E,V>> childNodes;
	
	private BCTComponent<E,V> graph;
	private V point;
	
	
	public BCTreeNode(BCTComponent<E,V> g) {
		this.parentNode = null;
		this.childNodes = new Vector<BCTreeNode<E,V>>();

		this.graph = g;
		this.point = null;
		
		this.nodeType = BCType.B;
	}
	
	public BCTreeNode(V p) {
		parentNode = null;
		childNodes = new Vector<BCTreeNode<E,V>>();

		this.graph = null;
		this.point = p;
		
		this.nodeType = BCType.C;
	}
	
	public BCType getNodeType() {
		return nodeType;
	}
	
	public BCTreeNode<E,V> getParentNode() {
		return this.parentNode;
	}
	
	public BCTComponent<E,V> getGraph() {
		return this.graph;
	}
	
	public V getPoint() {
		return this.point;
	}
	
	public Collection<BCTreeNode<E,V>> getChildren() {
		return this.childNodes;
	}
	
	public void addChild(BCTreeNode<E,V> node) {
		this.childNodes.add(node);
	}
	
	public void removeChild(BCTreeNode<E,V> node) {
		this.childNodes.remove(node);
	}
	
	public void setParent(BCTreeNode<E,V> node) {
		this.parentNode = node;
	}
}
