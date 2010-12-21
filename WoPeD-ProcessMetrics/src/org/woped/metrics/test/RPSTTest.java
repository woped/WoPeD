package org.woped.metrics.test;

import de.hpi.bpt.graph.DirectedEdge;
import de.hpi.bpt.graph.DirectedGraph;
import de.hpi.bpt.graph.algo.rpst.RPST;
import de.hpi.bpt.graph.algo.rpst.RPSTNode;
import de.hpi.bpt.hypergraph.abs.Vertex;

public class RPSTTest {

	public void testBPM08Fig11() {
		System.out.println("========================================================");
		System.out.println("BPM08 Fig.11");
		System.out.println("========================================================");
		
		DirectedGraph g = new DirectedGraph();
		
		Vertex s = new Vertex("s");
		Vertex t = new Vertex("t");
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Vertex v3 = new Vertex("v3");
		Vertex v4 = new Vertex("v4");
		Vertex v5 = new Vertex("v5");
		Vertex v6 = new Vertex("v6");
		Vertex v7 = new Vertex("v7");
		
		g.addVertex(s);
		g.addVertex(t);
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addVertex(v5);
		g.addVertex(v6);
		g.addVertex(v7);
		
		g.addEdge(s,v1);
		g.addEdge(s,v2);
		g.addEdge(v1,v3);
		g.addEdge(v1,v5);
		g.addEdge(v2,v5);
		g.addEdge(v3,v2);
		g.addEdge(v3,v4);
		g.addEdge(v4,v1);
		g.addEdge(v4,v2);
		g.addEdge(v5,v6);
		g.addEdge(v5,v7);
		g.addEdge(v6,v5);
		g.addEdge(v6,v7);
		g.addEdge(v7,v5);
		g.addEdge(v7,t);
		
		RPST<DirectedEdge,Vertex> rpst = new RPST<DirectedEdge,Vertex>(g);
		
		System.out.println(rpst);
		
		for (RPSTNode<DirectedEdge,Vertex> node : rpst.getVertices()) {
			System.out.println(node.getName() + " : " + node.getFragment());
		}
	}
	
	
	
	public static void main(String[] args){
		RPSTTest test = new RPSTTest();
		test.testBPM08Fig11();
	}

}
