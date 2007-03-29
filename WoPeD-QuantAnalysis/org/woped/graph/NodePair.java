package org.woped.graph;

public class NodePair {
	Node a;
	Node b;
	
	public NodePair(Node x, Node y){
		a = x;
		b = y;
	}
	
	public Node getFirst(){
		return a;
	}
	
	public Node getSecond(){
		return b;
	}
}
