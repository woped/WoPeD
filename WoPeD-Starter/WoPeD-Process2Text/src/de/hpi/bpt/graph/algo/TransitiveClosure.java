package de.hpi.bpt.graph.algo;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.graph.abs.IDirectedEdge;
import de.hpi.bpt.graph.abs.IDirectedGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

public class TransitiveClosure<E extends IDirectedEdge<V>,V extends IVertex> {

	protected IDirectedGraph<E, V> g;
	protected List<V> verticesAsList;
	protected boolean[][] matrix;
	
	
	public TransitiveClosure(IDirectedGraph<E, V> g) {
		this.g = g;
		this.matrix = null;
		this.verticesAsList = new ArrayList<V>(this.g.getVertices());
	}

	protected void calculateMatrix() {
		matrix = new boolean[this.verticesAsList.size()][this.verticesAsList.size()];
		
		/*
		 * Init matrix with edges
		 */
		for (E e: this.g.getEdges()) {
			int source = this.verticesAsList.indexOf(e.getSource());
			int target = this.verticesAsList.indexOf(e.getTarget());
			matrix[source][target] = true;
		}
		
		/*
		 * Compute the transitive closure
		 */
		for (int i = 0; i < matrix.length; i++) 
			for (int j = 0; j < matrix.length; j++) 
				if (matrix[j][i])
					for (int k = 0; k < matrix.length; k++)
						matrix[j][k] = matrix[j][k] | matrix[i][k];
	}
	
	/**
	 * Check if there exists a directed path between two vertices
	 * @param v1 Vertex
	 * @param v2 Vertex
	 * @return <code>true</code> if there is a directed path from v1 to v2, <code>false</code> otherwise 
	 */
	public boolean hasPath(V v1, V v2) {
		if (matrix == null)
			calculateMatrix();
		int i = this.verticesAsList.indexOf(v1);
		int j = this.verticesAsList.indexOf(v2);
		return matrix[i][j];
	}
	
	/**
	 * Check if vertex is part of a loop
	 * @param v Vertex
	 * @return <code>true</code> if vertex is part of a loop, <code>false</code> otherwise
	 */
	public boolean isInLoop(V v) {
		if (matrix == null)
			calculateMatrix();
		int index = this.verticesAsList.indexOf(v);
		return matrix[index][index];
	}
	
	@Override
	public String toString() {
		if (matrix == null)
			calculateMatrix();
		
		String result = "";
		
		result += "==================================================\n";
		result += " Transitive Closure\n";
		result += "--------------------------------------------------\n";
		for (int i=0; i<verticesAsList.size(); i++)
			result += String.format("%d : %s\n", i, verticesAsList.get(i));
		result += "--------------------------------------------------\n";
		result += "    ";
		for (int i=0; i<verticesAsList.size(); i++) result += String.format("%-4d", i);
		result += "    \n";
		for (int i=0; i<verticesAsList.size(); i++) {
			result += String.format("%-4d", i);
			for (int j=0; j<verticesAsList.size(); j++) {
				result += String.format("%-4s",(matrix[i][j] ? "+" : "-"));
			}
			result += String.format("%-4d", i);
			result += "\n";
		}
		result += "    ";
		for (int i=0; i<verticesAsList.size(); i++) result += String.format("%-4d", i);
		result += "    \n";
		result += "==================================================";
		
		return result;
	}

}


