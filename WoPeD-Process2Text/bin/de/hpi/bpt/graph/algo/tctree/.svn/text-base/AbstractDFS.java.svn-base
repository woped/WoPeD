package de.hpi.bpt.graph.algo.tctree;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * A standard Depth-first-search (DFS) implementation. It implements
 * the same dummy-methods as the DFS algorithm in {@link y.algo.Dfs}, but operates
 * on a given adjacency structure to determine the order in which paths
 * are generated.
 * 
 * @author Martin Mader
 * @author Christian Wiggert
 * 
 */
public class AbstractDFS<E extends IEdge<V>, V extends IVertex> {

	protected final V INVALID_NODE = null;
	protected final E INVALID_EDGE = null;
	
	/**
	 * Vertex not yet visited
	 */
	public static final int WHITE = 0;
	
	/**
	 * Vertex visited, but not yet finished
	 */
	public static final int GRAY = 1;
	
	/**
	 * Vertex processed completely
	 */
	public static final int BLACK = 2;
	protected static final int EDGE_NOT_VISITED = 0;
	
	/**
	 * Tree edge
	 */
	public static final int TREE_EDGE = 1;
	
	/**
	 * Back edge
	 */
	public static final int BACK_EDGE = 2;
	
	/**
	 * Adjacency Map
	 */
	protected NodeMap<V> adj;
	
	/**
	 * The graph to operate on
	 */
	protected IGraph<E, V> g;
	
	/**
	 * The MetaInfoContainer for the additional maps related to the graph
	 */
	protected MetaInfoContainer meta;
	
	/**
	 * NodeMap storing DFS-Numbers
	 */
	protected NodeMap<V> dfsNumMap;
	
	/**
	 * NodeMap storing Completion Numbers
	 */
	protected NodeMap<V> complNumMap;
	
	/**
	 * NodeMap storing current vertex status: WHITE, GRAY, or BLACK
	 */
	protected NodeMap<V> nodeStateMap;
	
	/**
	 * EdgeMap storing current edge type: EDGE_NOT_VISITED, TREE_EDGE or BACK_EDGE
	 */
	protected EdgeMap<E, V> edgeTypeMap;
	private int dfsNum = 0;
	private int complNum = 0;
	
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
	public AbstractDFS(IGraph<E, V> graph, MetaInfoContainer container, NodeMap<V> adjMap) {
		g = graph;
		adj = adjMap;
		meta = container;
		nodeStateMap = this.createNodeMap(g);
		dfsNumMap = this.createNodeMap(g);
		complNumMap = this.createNodeMap(g);
		for (V node:g.getVertices()){
			nodeStateMap.put(node, WHITE);
			dfsNumMap.put(node, -1);
			complNumMap.put(node, -1);
		}
		edgeTypeMap = this.createEdgeMap(g);
		for (E edge:g.getEdges()){
			edgeTypeMap.put(edge, EDGE_NOT_VISITED);
		}
		// add data provider
		meta.setMetaInfo(MetaInfo.DFS_NUM, dfsNumMap);
		meta.setMetaInfo(MetaInfo.DFS_COMPL_NUM, complNumMap);
		meta.setMetaInfo(MetaInfo.DFS_NODE_STATE, nodeStateMap);
		meta.setMetaInfo(MetaInfo.DFS_EDGE_TYPE, edgeTypeMap);
	}
	
	/**
	 * starts a depth-first-search (DFS) beginning at the given 
	 *  
	 * @param root the root node of the DFS
	 */
	public void start(V root) {
		dfsNum = 0;
		complNum = 0;
		dfs(root);
	}
	
	@SuppressWarnings("unchecked")
	protected void dfs(V v){
		dfsNum++;
		dfsNumMap.put(v, dfsNum);
		nodeStateMap.put(v, GRAY);
		EdgeList<E, V> adjV = (EdgeList<E, V>) adj.get(v);
		
		//System.out.println(v + ": " + adjV);
		preVisit(v, (Integer) dfsNumMap.get(v));
		
		for (E e:adjV){
			// traverse only not yet visited edges
			//System.out.println("Visit: " + e);
			if ((Integer) edgeTypeMap.get(e) == EDGE_NOT_VISITED) {
				
				V w = e.getOtherVertex(v);
				// re-orient edge
				e.setVertices(v, w);
				
				if ((Integer) nodeStateMap.get(w) == WHITE) {
					// tree edge found -> traverse edge
					edgeTypeMap.put(e, TREE_EDGE);

					preTraverse(e, w, true);

					dfs(w);

					postTraverse(e, w);

				} else {
					// back edge found
					edgeTypeMap.put(e, BACK_EDGE);

					preTraverse(e, w, false);
				}
			}
			
		}
		
		// backtrack
		nodeStateMap.put(v, BLACK);
		complNum++;
		complNumMap.put(v, complNum);
		
		postVisit(v, (Integer) dfsNumMap.get(v), (Integer) complNumMap.get(v));
		
		
	}
	
	protected void preVisit(V v, int dfsNumber){}
	
	protected  void preTraverse(E e, V w, boolean treeEdge){}
	
	protected  void postTraverse(E e, V w) {}
	
	protected  void postVisit(V v, int dfsNumber, int complNumber){}

	public EdgeMap<E, V> getEdgeTypeMap() {
		return edgeTypeMap;
	}

	
	/**
	 * Move to graph algorithms.
	 */
	@Deprecated
	protected EdgeMap<E, V> createEdgeMap(IGraph<E,V> g) {
		EdgeMap<E, V> map = new EdgeMap<E, V>();
		for (E e:g.getEdges()) {
			map.put(e, null);
		}
		return map;
	}
	
	/**
	 * Move to graph algorithms.
	 */
	@Deprecated
	protected NodeMap<V> createNodeMap(IGraph<E, V> g) {
		NodeMap<V> map = new NodeMap<V>();
		for (V v:g.getVertices()) {
			map.put(v, null);
		}
		return map;
	}

}
