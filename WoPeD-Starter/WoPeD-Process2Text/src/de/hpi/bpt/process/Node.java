package de.hpi.bpt.process;

import de.hpi.bpt.hypergraph.abs.Vertex;


public abstract class Node extends Vertex {

	public Node() {
		super();
	}

	public Node(String name, String desc) {
		super(name, desc);
	}

	public Node(String name) {
		super(name);
	}
}
