package de.hpi.bpt.graph.algo.tctree;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * This class corresponds to the first execution of DFS during the
 * triconnectivity algorithm of Hopcroft and Tarjan. It computes the
 * palm tree of the given graph, and also information about low-points
 * and the number of descendants.
 * <br><br>
 * For further details on the algorithm have a look at:<br>
 * http://kops.ub.uni-konstanz.de/volltexte/2009/8739/<br>
 * chapter 3.2
 * 
 * @author Martin Mader
 * @author Christian Wiggert
 *
 */
public class LowAndDescDFS<E extends IEdge<V>, V extends IVertex> extends ParentAndPathDFS<E, V> {
	
	/**
	 * NodeMap storing the low-point 1 number of each vertex
	 */
	protected NodeMap<V> lowpt1NumMap;
	/**
	 * NodeMap storing the low-point 2 number of each vertex
	 */
	protected NodeMap<V> lowpt2NumMap;
	/**
	 * NodeMap storing vertex corresponding to the low-point 1 number of each vertex
	 */
	protected NodeMap<V> lowpt1VertexMap;
	/**
	 * NodeMap storing vertex corresponding to the low-point 2 number of each vertex
	 */
	protected NodeMap<V> lowpt2VertexMap;
	/**
	 * NodeMap storing for each vertex its number of descendants
	 */
	protected NodeMap<V> numDescMap;
	

	public LowAndDescDFS(IGraph<E, V> graph, MetaInfoContainer container, NodeMap<V> adjMap) {
		super(graph, container, adjMap);
		lowpt1NumMap = this.createNodeMap(g);
		lowpt2NumMap = this.createNodeMap(g);
		lowpt1VertexMap = this.createNodeMap(g);
		lowpt2VertexMap = this.createNodeMap(g);
		numDescMap = this.createNodeMap(g);
		
		
		for (V node:g.getVertices()){
			lowpt1NumMap.put(node, -1);
			lowpt2NumMap.put(node, -1);
			lowpt1VertexMap.put(node, INVALID_NODE);
			lowpt2VertexMap.put(node, INVALID_NODE);
			numDescMap.put(node, -1);
		}
		
		// add data provider
		meta.setMetaInfo(MetaInfo.DFS_LOWPT1_NUM, lowpt1NumMap);
		meta.setMetaInfo(MetaInfo.DFS_LOWPT2_NUM, lowpt2NumMap);
		meta.setMetaInfo(MetaInfo.DFS_LOWPT1_VERTEX, lowpt1VertexMap);
		meta.setMetaInfo(MetaInfo.DFS_LOWPT2_VERTEX, lowpt2VertexMap);
		meta.setMetaInfo(MetaInfo.DFS_NUM_DESC, numDescMap);
		
	}



	@Override
	protected void preVisit(V v, int dfsNumber) {
		super.preVisit(v, dfsNumber);
		// initialize low-point numbers with dfs number
		lowpt1NumMap.put(v, dfsNumber);
		lowpt2NumMap.put(v, dfsNumber);
		lowpt1VertexMap.put(v, v);
		lowpt2VertexMap.put(v, v);
		// initialize number of descendants
		numDescMap.put(v, 1);
	}



	@Override
	protected void preTraverse(E e, V w, boolean treeEdge) {
		super.preTraverse(e, w, treeEdge);
		
		V v = e.getOtherVertex(w);
		if (!treeEdge){
			// update low-point numbers for v
			if ((Integer) dfsNumMap.get(w) < (Integer) lowpt1NumMap.get(v)){
				// the found back edge reaches lower than current low1(v)
				// -> low2(v) gets low1(v)
				lowpt2NumMap.put(v, (Integer) lowpt1NumMap.get(v));
				lowpt2VertexMap.put(v, lowpt1VertexMap.get(v));
				// -> low1(v) gets target of found back edge
				lowpt1NumMap.put(v, (Integer) dfsNumMap.get(w));
				lowpt1VertexMap.put(v,w);
			}else if ((Integer) dfsNumMap.get(w) > (Integer) lowpt1NumMap.get(v)) {
				// low1(v) needs not to be changed
				// low2(v) possibly gets the target of the found back edge
				if ((Integer) dfsNumMap.get(w) < (Integer) lowpt2NumMap.get(v)){
					lowpt2NumMap.put(v, (Integer) dfsNumMap.get(w));
					lowpt2VertexMap.put(v,w);
				}
			}
			
		}
	}

	@Override
	protected void postTraverse(E e, V w) {
		super.postTraverse(e, w);
		
		V v = e.getOtherVertex(w);
		// update low-point numbers for v
		if ((Integer) lowpt1NumMap.get(w) < (Integer) lowpt1NumMap.get(v)) {
			// low1(v) gets low1(w)
			// low2(v) either gets low1(v) or low2(w)
			int min = Math.min((Integer) lowpt1NumMap.get(v), (Integer) lowpt2NumMap.get(w));
			lowpt2NumMap.put(v, min);
			if (min == (Integer) lowpt1NumMap.get(v)) {
				lowpt2VertexMap.put(v, lowpt1VertexMap.get(v));
			} else {
				lowpt2VertexMap.put(v, lowpt2VertexMap.get(w));
			}
			lowpt1NumMap.put(v, (Integer) lowpt1NumMap.get(w));
			lowpt1VertexMap.put(v, lowpt1VertexMap.get(w));
		} else if ((Integer) lowpt1NumMap.get(w) == (Integer) lowpt1NumMap.get(v)) {
			// low1(v) needs not to be changed
			// low2(v) possibly gets low2(w)
			if ((Integer) lowpt2NumMap.get(w) < (Integer) lowpt2NumMap.get(v)) {
				lowpt2NumMap.put(v, (Integer) lowpt2NumMap.get(w));
				lowpt2VertexMap.put(v, lowpt2VertexMap.get(w));
			}
		} else {
			// low1(v) is lower than low1(w)
			// -> low1(v) needs not to be changed
			// low2(v) gets low1(w) if low1(w) is lower
			if ((Integer) lowpt1NumMap.get(w) < (Integer) lowpt2NumMap.get(v)) {
				lowpt2NumMap.put(v, (Integer) lowpt1NumMap.get(w));
				lowpt2VertexMap.put(v, lowpt1VertexMap.get(w));
			}
		}
		// update number of descendants
		numDescMap.put(v, (Integer) numDescMap.get(v) + (Integer) numDescMap.get(w));
	}

	
}
