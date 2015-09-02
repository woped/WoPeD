package de.hpi.bpt.graph.algo.tctree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * This class takes a biconnected graph and decomposes it into 
 * TCTreeNodes. Therefore it coordinates all the runs of the different DFS
 * algorithms and reassembles the found components to TCTreeNodes.
 * <br><br>
 * This implementation is an adaption of the algorithm implemented by
 * Martin Mader. The general process of this decomposition is described
 * in his master thesis, which can be found here:<br> 
 * [PGD08] chapter 3 - http://kops.ub.uni-konstanz.de/volltexte/2009/8739/ 
 * 
 * @author Martin Mader
 * @author Christian Wiggert
 *
 * @param Implementation of IEdge
 * @param Implementation of IVertex
 */
public class ModelDecomposer<E extends IEdge<V>, V extends IVertex> {

	/**
	 * controls the debug output
	 */
	private static boolean showDebugInformation = false;
	
	public ModelDecomposer() {}
	
	public Collection<TCTreeNode<E, V>> getTriconnectedComponents(IGraph<E, V> graph, TCTreeNode<E, V> root, E backEdge) {//, V root) {
		Vector<EdgeList<E, V>> components = new Vector<EdgeList<E, V>>();
		
		EdgeMap<E, V> virtEdgeMap = this.createEdgeMap(graph); 
		virtEdgeMap.initialiseWithFalse();
		virtEdgeMap.put(backEdge, true);
		EdgeMap<E, V> assignedVirtEdgeMap = this.createEdgeMap(graph);
		EdgeMap<E, V> isHiddenMap = this.createEdgeMap(graph);
		isHiddenMap.initialiseWithFalse();
		MetaInfoContainer meta = new MetaInfoContainer();
		
		meta.setMetaInfo(MetaInfo.VIRTUAL_EDGES, virtEdgeMap);
		meta.setMetaInfo(MetaInfo.ASSIGNED_VIRTUAL_EDGES, assignedVirtEdgeMap);
		meta.setMetaInfo(MetaInfo.HIDDEN_EDGES, isHiddenMap);
		
		// check for self-loops, biconnectivity was already checked in the TCTree class
		for (E edge:root.getSkeleton().getEdges()) {
			if (edge.isSelfLoop()) return null;
		}
		if(showDebugInformation) System.out.println("\nSplitting off initial multiple edges...");
		splitOffInitialMultipleEdges(root.getSkeleton(), components, virtEdgeMap, assignedVirtEdgeMap, isHiddenMap );
		if(showDebugInformation) {
			System.out.println("\nSplitting off initial multiple edges finished !");
			System.out.println("backEdge: " + backEdge);
			System.out.println("\nFinding split components...");
		}
		findSplitComponents(root.getSkeleton(), components, virtEdgeMap,  
				assignedVirtEdgeMap, isHiddenMap, meta, backEdge.getV1());
		if(showDebugInformation) System.out.println("\nFinding split components finished !");
		
		// create TCTreeNodes and Skeletons from components
		if(showDebugInformation) System.out.println("\nCreating TCTreeNodes...");
		HashMap<E, TCTreeNode<E, V>> virtEdgeComps = new HashMap<E, TCTreeNode<E, V>>(); 
		LinkedList<TCTreeNode<E, V>> nodes = new LinkedList<TCTreeNode<E, V>>();
		int i = 0;
		if(showDebugInformation) System.out.println("\n Graph edges: " + graph.getEdges());
		for (EdgeList<E, V> el:components) {
			TCTreeSkeleton<E, V> skeleton = new TCTreeSkeleton<E, V>(graph);
			TCTreeNode<E, V> node = new TCTreeNode<E, V>(String.valueOf(i++));
			for (E edge:el) {
				if (virtEdgeMap.getBool(edge)) {
					E virtEdge = skeleton.addVirtualEdge(edge.getV1(), edge.getV2());
					virtEdge.setDescription(edge.getDescription());
					virtEdge.setId(edge.getId());
					virtEdgeComps.put(virtEdge, node);
				} else {
					E newEdge = skeleton.addEdge(edge.getV1(), edge.getV2());
					newEdge.setDescription(edge.getDescription());
				}
			}
			node.setSkeleton(skeleton);
			nodes.add(node);
		}
		if(showDebugInformation) System.out.println("\nCreating TCTreeNodes finished!");
		classifyNodes(nodes);
		
		// merge bonds and polygons
		ArrayList<TCTreeNode<E, V>> result = new ArrayList<TCTreeNode<E, V>>();
		while (nodes.size() > 0) {
			TCTreeNode<E, V> node = nodes.poll();
			if (node.getType() == TCType.R) {
				// R nodes won't be merged, so add them to the result and skip the rest
				result.add(node);
				if(showDebugInformation) { 
					System.out.println("\n" + node + ": " + node.getSkeleton().getEdges());
					System.out.println(String.valueOf(node) + ": " + node.getSkeleton().getVirtualEdges());
				}
				continue;
			}
			TCTreeNode<E, V> remove = null, replace = null;
			boolean delete = false;
			for (TCTreeNode<E, V> n:nodes) {
				if (node.getType() != n.getType()) continue;
				for (E edge:node.getSkeleton().getVirtualEdges()) {
					E e = n.getSkeleton().getEdge(edge.getV1(), edge.getV2());
					if (e != null && n.getSkeleton().isVirtual(e) &&
							edge.getId().equals(e.getId())) {
						// there are two different TCTreeNodes with the same type and one virtual edge in common
						if(showDebugInformation) System.out.println("\nUUID: " + edge.getId() + "; " + edge + "; " + node + "; " + n);
						remove = n;
						replace = new TCTreeNode<E, V>(n.getName());
						replace.setSkeleton(new TCTreeSkeleton<E, V>(graph));
						replace.setType(n.getType());
						// because of a bug in the JBPT framework, we need to create a new
						// TCTreeNode and copy all necessary edges, excluding the common virtual edge.
						for (E edge2:node.getSkeleton().getEdges()) {
							if (edge == edge2) { 
								continue;
							}
							E newEdge;
							if (node.getSkeleton().isVirtual(edge2))
								newEdge = replace.getSkeleton().addVirtualEdge(edge2.getV1(), edge2.getV2());	
							else
								newEdge = replace.getSkeleton().addEdge(edge2.getV1(), edge2.getV2());
							newEdge.setDescription(edge2.getDescription());
							newEdge.setId(edge2.getId());
						}
						for (E e2:n.getSkeleton().getEdges()) {
							if (e == e2)
								continue;
							E newEdge;
							if (n.getSkeleton().isVirtual(e2))
								newEdge = replace.getSkeleton().addVirtualEdge(e2.getV1(), e2.getV2());
							else
								newEdge = replace.getSkeleton().addEdge(e2.getV1(), e2.getV2());
							newEdge.setDescription(e2.getDescription());
							newEdge.setId(e2.getId());
						}
						delete = true;
						break;
					}
				}
				if (delete) break;
			}
			if (!delete) {
				if(showDebugInformation) {
					System.out.println("\n" + node + ": " + node.getSkeleton().getEdges());
					System.out.println(String.valueOf(node) + ": " + node.getSkeleton().getVirtualEdges());
				}
				result.add(node);
			} else {
				nodes.remove(remove);
				nodes.addLast(replace);
			}
		}
		
		return result;
	}
	
	/**
	 * Classify TCTree nodes on types: P,B,R
	 */
	private void classifyNodes(Collection<TCTreeNode<E, V>> nodes) {
		int Pc, Bc, Rc;
		Pc = Bc = Rc = 0;
		
		Iterator<TCTreeNode<E,V>> i = nodes.iterator();
		while (i.hasNext()) {
			TCTreeNode<E,V> n = i.next();
			
			if (n.getSkeleton().countVertices()==2) { n.setType(TCType.B); n.setName("B" + Bc++); continue; }
			
			boolean isS = true;
			Iterator<V> vs = n.getSkeleton().getVertices().iterator();
			while (vs.hasNext()) {
				V v = vs.next();
				if (n.getSkeleton().getEdges(v).size()!=2) {
					isS = false;
					break;
				}
			}
			
			if (isS) {
				n.setType(TCType.P);
				n.setName("P" + Pc++);
			}
			else {
				n.setType(TCType.R);
				n.setName("R" + Rc++);
			}
		}
	}
	
	/**
	 * Runs the different DFS algorithms and creates the triconnected components based
	 * on the given graph and maps.
	 */
	@SuppressWarnings("unchecked")
	protected void findSplitComponents(IGraph<E, V> graph,
			Vector<EdgeList<E, V>> components, EdgeMap<E, V> virtEdgeMap,
			EdgeMap<E, V> assignedVirtEdgeMap, EdgeMap<E, V> isHiddenMap,
			MetaInfoContainer meta, V root) {
		// initial adjacency map
		NodeMap<V> adjMap = this.createNodeMap(graph);
		for (V v:graph.getVertices()){
			EdgeList<E, V> adj = new EdgeList<E, V>();
			for (E e:graph.getEdges(v)){
				adj.add(e);
			}
			adjMap.put(v, adj);
		}
		meta.setMetaInfo(MetaInfo.DFS_ADJ_LISTS, adjMap);
		// first DFS -- calculate lowpoint information
		LowAndDescDFS<E, V> dfs1 = new LowAndDescDFS<E, V>(graph, meta, adjMap);
		dfs1.start(root);
		
		// debug
		if(showDebugInformation) {
			System.out.println("\nDFS status after first DFS...");
			for (V n:graph.getVertices()) {
				System.out.println("Node "+n+": "+
						" DFSNum " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM)).getInt(n) +
						" CplNum " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_COMPL_NUM)).getInt(n) +
						" State " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NODE_STATE)).getInt(n) +
						" Low1Num " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_NUM)).getInt(n) +
						" Low2Num " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT2_NUM)).getInt(n) +
						" NumDesc " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM_DESC)).getInt(n) +
						" Parent " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_PARENT)).get(n)
				);
				
			}
			for (E e:graph.getEdges()) {
				System.out.println("Edge " + e + ": " +
						" startsPath " + ((EdgeMap<E, V>) meta.getMetaInfo(MetaInfo.DFS_STARTS_NEW_PATH)).get(e));
			}
		}
		// order adjacency lists according to low-point values
		NodeMap<V> orderedAdjMap = orderAdjLists(graph, meta);
		
		NodeMap<V> copiedOrderedAdjMap = new NodeMap<V>();
		for (V node:orderedAdjMap.keySet()) {
			copiedOrderedAdjMap.put(node, ((EdgeList<E,V>) orderedAdjMap.get(node)).clone());
		}
		// second DFS -- renumber the vertices
		NumberDFS<E, V> dfs2 = new NumberDFS<E, V>(graph, meta, copiedOrderedAdjMap);
		dfs2.start(root);
		
		// debug
		if(showDebugInformation) {
			System.out.print("\nHigh-Points after second DFS");
			for (V n:graph.getVertices()){
				System.out.print("\nNode "+n+": ");
				NodeList<V> hpList = (NodeList<V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_HIGHPT_LISTS)).get(n);
				for (V node:hpList) {
					System.out.print(" " + node + " ");
				}
			}
		}
		
		// workaround to circumvent a problem in the JBPT framework
		// which leads to not properly removed virtual edges in the TCTreeSkeleton
		// therefore this count is used to store the current state during dfs3
		NodeMap<V> edgeCount = new NodeMap<V>();
		for (V node:graph.getVertices()) {
			edgeCount.put(node, graph.getEdges(node).size());
		}
		meta.setMetaInfo(MetaInfo.DFS_EDGE_COUNT, edgeCount);
		// third DFS -- find the actual split components
		if(showDebugInformation) System.out.println("\n\nThird DFS - Finding split components...");
		SplitCompDFS<E, V> dfs3 = new SplitCompDFS<E, V>(graph, meta, copiedOrderedAdjMap, components, dfs2
				.getParentMap(), dfs2.getTreeArcMap(), dfs2.getHighptMap(),
				dfs2.getEdgeTypeMap(), virtEdgeMap, assignedVirtEdgeMap, 
				isHiddenMap);
		if (showDebugInformation) dfs3.doShowDebugInformation(true);
		dfs3.start(root);
		
		//debug
		if(showDebugInformation) {
			for (EdgeList<E, V> el : components) {
				System.out.print("\n Component: ");
				for (E e : el) {
					System.out.print(" (" + e + ")");
					if (virtEdgeMap.getBool(e))
						System.out.print("v ");
					else
						System.out.print(" ");
				}
				
			}
		}
		
	}

	/**
	 *  
	 */
	@SuppressWarnings("unchecked")
	protected NodeMap<V> orderAdjLists(IGraph<E, V> graph, MetaInfoContainer meta) {
		if(showDebugInformation) System.out.println("\nOrdering adjacency lists w.r.t. low-point values...");
		Collection<E> edges = graph.getEdges();
		ArrayList<EdgeList<E, V>> bucket = new ArrayList<EdgeList<E, V>>();
		int bucketSize = 3 * (graph.countVertices()) + 2;
		if(showDebugInformation) System.out.println("\n vertices: " + graph.countVertices() + ", bucket size: " + bucketSize);
		for (int i=0; i< bucketSize; i++){
			bucket.add(new EdgeList<E, V>());
		}
		int phi;
		if(showDebugInformation) System.out.println("Potentials: ");
		for (E e:edges) {

			phi = -1;
			// assign each edge its potential phi
			if (((EdgeMap<E, V>) meta.getMetaInfo(MetaInfo.DFS_EDGE_TYPE)).getInt(e) == AbstractDFS.TREE_EDGE){
				// e is tree edge
				if (((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT2_NUM)).getInt(e.getV2()) 
						< ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM)).getInt(e.getV1())){
					// low2(w) < v
					phi = 3 * ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_NUM)).getInt(e.getV2());
				} else {
					// low2(w) >= v
					phi = 3 * ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_NUM)).getInt(e.getV2()) + 2;
				}
			} else {
				// e is back edge
				phi = 3 * ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM)).getInt(e.getV2()) + 1;
			}
			if(showDebugInformation) System.out.print(" ["+e+"]="+phi);
			
			// put edge into bucket according to phi
			// ! bucket's index start with 0
			(bucket.get(phi-1)).add(e);
		}
		
		// create a new node map for the ordered adj list
		NodeMap<V> orderedAdjMap = this.createNodeMap(graph);
		for (V node:graph.getVertices()) {
			EdgeList<E, V> adj = new EdgeList<E, V>();
			orderedAdjMap.put(node, adj);
		}
		meta.setMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS, orderedAdjMap);
		
		// put edges into adj list according to order in buckets
		for (EdgeList<E, V> el : bucket){
			while (!el.isEmpty()){
				E e = el.pop();
				((EdgeList<E ,V>) orderedAdjMap.get(e.getV1())).add(e);
			}
		}
		if(showDebugInformation) System.out.println("\nOrdering finished");
		return orderedAdjMap;
	}

	/**
	 * This method is used for the deletion of multiple edges. 
	 * The edges are sorted in a manner, so that multiple edges
	 * are positioned consecutively in the returned EdgeList.
	 */
	@SuppressWarnings("unchecked") 
	protected EdgeList<E, V> sortConsecutiveMultipleEdges(IGraph<E, V> g){
		NodeMap<V> indices = new NodeMap<V>();
		int count = 0;
		for (V vertex:g.getVertices()) {
			indices.put(vertex, count++);
		}
		// bucketSort edges such that multiple edges come after each other
		if(showDebugInformation) System.out.println("\nSorting edges...");
		ArrayList<E> edges = (ArrayList<E>) g.getEdges();
		ArrayList<EdgeList<E, V>> bucket = new ArrayList<EdgeList<E, V>>();
		// place edges into buckets according to vertex with smaller index
		for (int i = 0; i < g.getVertices().size(); i++) {//edges.size(); i++) {
			bucket.add(new EdgeList<E, V>());
		}
		for (E e:edges) {
			int i = Math.min((Integer) indices.get( e.getV1()), (Integer) indices.get(e.getV2()));
			bucket.get(i).add(e);
		}
		
		// sort within buckets according to enDP_NAMESoint with larger index
		EdgeList<E, V> sortedEdges = new EdgeList<E, V>();
		for  (EdgeList<E, V> l : bucket){
			HashMap<Integer, EdgeList<E, V>> map = new HashMap<Integer, EdgeList<E, V>>();
			for (Object e : l){
				// add up indices of enDP_NAMESoints
				Integer i = (Integer) indices.get(((E)e).getV1()) + (Integer) indices.get(((E)e).getV2());
				// take this as key for the map
				EdgeList<E, V> el = map.get(i);
				// and add the edge to the corresponding edge list
				if (el == null) {
					el = new EdgeList<E, V>();
					el.add((E) e);
					map.put(i, el);
				} else {
					el.add((E) e);
				}
			}
			// put edges into output list
			Collection<EdgeList<E, V>> col = map.values();
			for (EdgeList el : col){
				if (el != null){
					sortedEdges.addAll(el);
				}
			}
			
		}
		
		//debug
		if(showDebugInformation) {
			System.out.println();
			for (E e : sortedEdges){
				System.out.println(" [" + e.toString() + "]");
			}
		}
		
		return sortedEdges;
	}
	
	/**
	 * Simply deletes found multiple edges in the given graph.
	 */
	protected void splitOffInitialMultipleEdges(IGraph<E, V> graph,
			Vector<EdgeList<E, V>> components, EdgeMap<E, V> virtEdgeMap,
			EdgeMap<E, V> assignedVirtEdgeMap, EdgeMap<E, V> isHiddenMap) {
		//debug
		if(showDebugInformation) {
			System.out.println("Edges of the whole graph :");
			for (E edge:graph.getEdges()) {
				System.out.print(" ["+edge.toString()+"]  ");
			}
			System.out.println("");
		}
		
		// sort edges such that multiple edges are consecutive
		EdgeList<E, V> edges = sortConsecutiveMultipleEdges(graph);
		
		//debug
		if(showDebugInformation) System.out.println("Edges sorted !");
		
		// split off multiple edge components
		EdgeList<E, V> tempComp = new EdgeList<E, V>();
		E lastEdge=null, currentEdge=null;
		int tempCompSize = 0;
		for (E e:edges){
			currentEdge = e;
			if (lastEdge != null){
				// multiple edge if enDP_NAMESoint correspond to lastEdge's enDP_NAMESoints
				if ((currentEdge.getV1()==lastEdge.getV1() && 
						currentEdge.getV2()==lastEdge.getV2())
						|| 
						(currentEdge.getV1()==lastEdge.getV2() &&
						currentEdge.getV2()==lastEdge.getV1())){
					// add lastEdge to new component
					tempComp.add(lastEdge);
					tempCompSize++;
				}
				// current edge is different from last edge
				else{
					// if tempCompSize is greater than zero, there has been split off
					// at least one edge, and the corresponding component needs to be
					// finished
					if (tempCompSize>0){
						// add lastEdge to component
						tempComp.add(lastEdge);
						// finish component, i.e. add virtual edge and store the
						// component
						newComponent(graph, components, tempComp, virtEdgeMap, 
								assignedVirtEdgeMap, isHiddenMap,
								lastEdge.getV1(), lastEdge.getV2());
						// look for new multiple edges next time
						tempComp = new EdgeList<E, V>();
						tempCompSize=0;
					}
				}
			}
			lastEdge = currentEdge;
		}
		// possible finishing of the last component due to multiple edges
		if (tempCompSize>0){
			// add lastEdge to component
			tempComp.add(lastEdge);
			// finish component, i.e. add virtual edge and store the
			// component
			newComponent(graph, components, tempComp, virtEdgeMap, 
					assignedVirtEdgeMap, isHiddenMap,
					lastEdge.getV1(), lastEdge.getV2());
		}
		
		//debug
		if (showDebugInformation) {
			for (EdgeList<E, V> el : components) {
				System.out.println("\nComponent");
				for (E e:el) {
					System.out.print(" [" + e.toString() + "] ");
				}
			}
		}
	}

	/**
	 * Creates a new component based on the given list of contained edges.
	 */
	protected void newComponent(IGraph<E, V> graph,
			Vector<EdgeList<E, V>> components, EdgeList<E, V> tempComp,
			EdgeMap<E, V> virtEdgeMap, EdgeMap<E, V> assignedVirtEdgeMap,
			EdgeMap<E, V> isHiddenMap, V v1, V v2) {
		// remove edges from graph
		if(showDebugInformation) System.out.print("Hiding edge ");
		for (E e : tempComp){	
			try {
				graph.removeEdge(e);
				if(showDebugInformation) System.out.print("("+e+") ");
				isHiddenMap.put(e, true);
			} catch (Exception ex) {
				if(showDebugInformation) System.err.println("Edge "+e+ ": "+ex.getMessage());
				ex.printStackTrace();
			}
		}
		// create virtual edge and add edges to component
		E virtualEdge = graph.addEdge(v1, v2);
		virtEdgeMap.put(virtualEdge, true);
		tempComp.add(0, virtualEdge);
		// assign virtual edge
		for(E e:tempComp){
			assignedVirtEdgeMap.put(e, virtualEdge);
		}
		components.add(tempComp);
		
	}

	/**
	 * Creates an edgeMap for the given graph containing all edges of the graph.
	 * -- Move this method to graph algorithms. --
	 * @param g
	 * @return
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
	 * Creates a NodeMap for the given graph containing all nodes of the graph.
	 * -- Move this method to graph algorithms. --
	 * @param g
	 * @return
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
