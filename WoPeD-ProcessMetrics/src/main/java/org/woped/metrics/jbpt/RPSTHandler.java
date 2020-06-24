package org.woped.metrics.jbpt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;

import de.hpi.bpt.graph.DirectedEdge;
import de.hpi.bpt.graph.DirectedGraph;
import de.hpi.bpt.graph.algo.rpst.RPST;
import de.hpi.bpt.graph.algo.rpst.RPSTNode;
import de.hpi.bpt.graph.algo.tctree.TCType;
import de.hpi.bpt.hypergraph.abs.Vertex;

public class RPSTHandler {

	private RPST<DirectedEdge,Vertex> rpst;
	
	/**
	 * An RPST handler is used for conversion and calculation of RPST models based on the library of Artem Polyvyanyy
	 * 
	 * @param mec
	 */
	public RPSTHandler(ModelElementContainer mec){
		
		Map<String, Map<String, Object>> idMap = mec.getIdMap();
		Map<String, ArcModel> arcMap = mec.getArcMap();
		
		Map<String, Vertex> idToVertex = new HashMap<String, Vertex>();
		
		DirectedGraph g = new DirectedGraph();
		
		for(String key:idMap.keySet()){
			Vertex s = new Vertex(key);
			g.addVertex(s);
			idToVertex.put(key, s);
		}
			
		
		for(String key:idMap.keySet()){
			for(String subkey:idMap.get(key).keySet()){
				ArcModel arc = arcMap.get(subkey);
				if(arc == null) continue;
				g.addEdge(idToVertex.get(arc.getSourceId()), idToVertex.get(arc.getTargetId()));
			}
		}
		
		rpst = new RPST<DirectedEdge,Vertex>(g);
		
	}
	
	public double getComonents(){
		return rpst.getVertices().size();
	}
	
	public double getTrivial(){
		return rpst.getVertices(TCType.T).size();
	}
	
	public double getPolygon(){
		return rpst.getVertices(TCType.P).size();
	}
	
	public double getBond(){
		return rpst.getVertices(TCType.B).size();
	}
	
	public double getRigid(){
		return rpst.getVertices(TCType.R).size();
	}
	
	public double getDepth(){
		if(rpst.getRoot() == null) return Double.NaN;
		return getDepth(rpst.getRoot(), 1);
	}
	
	/**
	 * Recursive method for depth calculation of RPST nets
	 * 
	 * @param tn		Current Node (Containing outgoing Edges / Vertices)
	 * @param depth		Current depth of the recursive method
	 * @return			Largest depth of the net
	 */
	private double getDepth(RPSTNode<DirectedEdge, Vertex> tn, int depth){
		double result = depth;
		Collection<RPSTNode<DirectedEdge, Vertex>> children = rpst.getChildren(tn);
		if(children != null)
			for (RPSTNode<DirectedEdge, Vertex> c: children)
				result = Math.max(result, getDepth(c, depth+1));
		return result;
	}
	
	public double getNodesInRigid(){
		if(rpst.getRoot() == null) return Double.NaN;
		return getNodesInRigid(rpst.getRoot(), rpst.getRoot().getType() == TCType.R);
	}
	
	private double getNodesInRigid(RPSTNode<DirectedEdge, Vertex> tn, boolean instack){
		Collection<RPSTNode<DirectedEdge, Vertex>> subs = rpst.getChildren(tn);
		if(subs == null) return 0;
		instack = instack || tn.getType() == TCType.R;
		double elements = instack ? subs.size() : 0;
		for (RPSTNode<DirectedEdge, Vertex> c: subs)
			elements += getNodesInRigid(c, instack);
		return elements;
	}
}
