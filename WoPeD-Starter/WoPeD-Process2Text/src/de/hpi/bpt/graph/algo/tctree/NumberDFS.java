package de.hpi.bpt.graph.algo.tctree;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * This class corresponds to the second execution of DFS during the
 * triconnectivity algorithm of Hopcroft and Tarjan. It determines 
 * the final numbering of vertices and the list of high-points used 
 * in the last stage of the triconnectivity algorithm. To correctly 
 * do this, an instance of {@link LowAndDescDFS} must have already 
 * been run on the given graph and the given adjacency structure must 
 * have been ordered according to low-point values previously.
 * <br><br>
 * For further details on the algorithm have a look at:<br>
 * http://kops.ub.uni-konstanz.de/volltexte/2009/8739/<br>
 * chapter 3.3
 * 
 * @author Martin Mader
 * @author Christian Wiggert
 * @Precondition	an instance of {@link LowAndDescDFS} has already 
 * 					been run on the given graph.
 *
 */
public class NumberDFS<E extends IEdge<V>, V extends IVertex> extends ParentAndPathDFS<E, V> {
	
	/**
	 * NodeMap storing for each vertex its list of high-points
	 */
	protected NodeMap<V> highptMap;
	/**
	 * NodeMap storing the new number of each vertex 
	 * (according to inverse post-order numbering)
	 */
	protected NodeMap<V> numVMap;
	/**
	 * NodeMap storing foe each vertex its number of leaving 
	 * tree edges
	 */
	protected NodeMap<V> numTreeEdgesMap;
	/**
	 * NodeMap storing for each vertex its low-point 1 number 
	 * according to the inverse post-order numbering
	 */
	protected NodeMap<V> lowpt1NumMap;
	/**
	 * NodeMap storing for each vertex its low-point 2 number 
	 * according to the inverse post-order numbering
	 */
	protected NodeMap<V> lowpt2NumMap;
	private int m = -1;
	
	/**
	 * creates an Instance of DFS which operates on the given graph 
	 * and adjacency structure.
	 * 
	 * @param graph 	the graph on which to perform DFS
	 * @param adjMap	the adjacency structure to be used.
	 * 					in each entry must be contained an 
	 * 					{@link y.base.EdgeList} 
	 * 					of adjacent neighbors. DFS traverses edges 
	 * 					the according order.				
	 */
	public NumberDFS(IGraph<E, V> graph, MetaInfoContainer container, NodeMap<V> adjMap) {
		super(graph, container, adjMap);
		
		highptMap = this.createNodeMap(g);
		numVMap = this.createNodeMap(g);
		numTreeEdgesMap = this.createNodeMap(g);
		lowpt1NumMap = this.createNodeMap(g);
		lowpt2NumMap = this.createNodeMap(g);
		
		for (V node:g.getVertices()){
			highptMap.put(node, new NodeList<V>());
			numVMap.put(node, -1);
			numTreeEdgesMap.put(node, -1);
		}
		
		m = g.countVertices();
		
		
		// add data provider
		meta.setMetaInfo(MetaInfo.DFS_HIGHPT_LISTS, highptMap);
		meta.setMetaInfo(MetaInfo.DFS_NUM_V, numVMap);
		meta.setMetaInfo(MetaInfo.DFS_LOWPT1_NUM, lowpt1NumMap);
		meta.setMetaInfo(MetaInfo.DFS_LOWPT2_NUM, lowpt2NumMap);
		meta.setMetaInfo(MetaInfo.DFS_NUM_TREE_EDGES, numTreeEdgesMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void preVisit(V v, int dfsNumber) {
		super.preVisit(v, dfsNumber);
		numVMap.put(v, m - ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM_DESC)).getInt(v) + 1);
		numTreeEdgesMap.put(v, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void preTraverse(E e, V w, boolean treeEdge) {
		super.preTraverse(e, w, treeEdge);
		// if back edge add high-point to w
		if (!treeEdge){
			((NodeList<V>) highptMap.get(w)).add(e.getOtherVertex(w));
		}
	}

	@Override
	protected void postTraverse(E e, V w) {
		super.postTraverse(e, w);
		V v = e.getOtherVertex(w);
		m--;
		numTreeEdgesMap.put(v, (Integer) numTreeEdgesMap.get(v) + 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void postVisit(V v, int dfsNumber, int complNumber) {
		super.postVisit(v, dfsNumber, complNumber);
		// adjust low point numbers to new numbering
		lowpt1NumMap.put(v, (Integer) numVMap.get(((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_VERTEX)).get(v)));
		lowpt2NumMap.put(v, (Integer) numVMap.get(((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT2_VERTEX)).get(v)));	
	}

	public NodeMap<V> getHighptMap() {
		return highptMap;
	}

}

