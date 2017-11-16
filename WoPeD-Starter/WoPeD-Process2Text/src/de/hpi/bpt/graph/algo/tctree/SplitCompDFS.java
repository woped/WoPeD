package de.hpi.bpt.graph.algo.tctree;

import java.util.Stack;
import java.util.UUID;
import java.util.Vector;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * This class corresponds to the final execution of DFS during the
 * triconnectivity algorithm of Hopcroft and Tarjan. It determines 
 * the split components of the given graph using cycle decomposition. 
 * To correctly 
 * do this, an instance of {@link NumberDFS} must have already 
 * been run on the given graph.
 * <br><br>
 * For further details on the algorithm have a look at:<br>
 * [PGD08] - http://kops.ub.uni-konstanz.de/volltexte/2009/8739/<br>
 * chapter 3.4
 * 
 * @author Martin Mader
 * @author Christian Wiggert
 * @Precondition	an instance of {@link NumberDFS} has already 
 * 					been run on the given graph.
 *
 */
@SuppressWarnings("unchecked")
public class SplitCompDFS<E extends IEdge<V>, V extends IVertex> extends AbstractDFS<E, V> {
	
	private boolean showDebugInformation = false;
	private final TSItem EOS = new TSItem();
	private V dfsRoot = null;
	private EdgeList<E, V> eStack = new EdgeList<E, V>();
	private Stack<TSItem> tStack = new Stack<TSItem>();
	private Vector<EdgeList<E, V>> comp;
	// boolean: true if edge is virtual edge
	private EdgeMap<E, V> virtEdge; 
	// edge: the virtual edge identifying the split component 
	// to which each edge belongs
	private EdgeMap<E, V> assignedVirtEdge; 	
	private EdgeMap<E, V> edgeType;
	private EdgeMap<E, V> isHidden;
	private NodeMap<V> parent;
	private NodeMap<V> treeArc;
	private NodeMap<V> highpt;
	private NodeMap<V> numNotVisitedTreeEdges;

	/**
	 * Constructor for SplitCompDFS.
	 * initializes basic data structures
	 * 
	 * @param graph						the graph to execute dfs on
	 * @param container					container used to store additional maps for the graph
	 * @param adjMap					map containing the ordered adjacency lists
	 * @param compVec					the vector in which to store split components
	 * @param parentMap					a NodeMap containing for each vertex its parent in the palm tree
	 * @param treeArcMap				an EdgeMap containing for each vertex the tree edge entering in the palm tree 
	 * @param highptMap					a NodeMap containing for each vertex the list of high-points
	 * @param edgeTypeMap				an EdgeMap containing for each edge its dfs-type
	 * @param virtualEdgeMap			a boolean EdgeMap to store for each edge, whether the edge is a virtual edge
	 * @param assignedVirtualEdgeMap	an EdgeMap to store for each edge the corresponding virtual edge of its component
	 * @param isHiddenMap				a boolean EdgeMap to store if an edge is hidden			
	 */
	public SplitCompDFS(IGraph<E, V> graph, MetaInfoContainer container, 
			NodeMap<V> adjMap, Vector<EdgeList<E, V>> compVec, 
			NodeMap<V> parentMap, NodeMap<V> treeArcMap, 
			NodeMap<V> highptMap,
			EdgeMap<E, V> edgeTypeMap, EdgeMap<E, V> virtualEdgeMap,
			EdgeMap<E, V> assignedVirtualEdgeMap, EdgeMap<E, V> isHiddenMap) {
		super(graph, container, adjMap);
		comp = compVec;
		virtEdge = virtualEdgeMap;
		edgeType = edgeTypeMap;
		parent = parentMap;
		treeArc = treeArcMap;
		highpt = highptMap;
		isHidden = isHiddenMap;
		assignedVirtEdge = assignedVirtualEdgeMap;
		
		numNotVisitedTreeEdges = this.createNodeMap(g);
		
		for (V node:g.getVertices()) {
			numNotVisitedTreeEdges.put(node, 
					 ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM_TREE_EDGES)).getInt(node));
			parent.put(node, ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_PARENT)).get(node));
			((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ADJ_LISTS)).get(node)).retainAll((EdgeList<E, V>) adjMap.get(node));
		}
		
		
	}
	
	/**
	 * set displaying of debug information on or off.  
	 * 
	 * @param b	true for displaying debug information
	 */
	public void doShowDebugInformation(boolean b){
		showDebugInformation = b;
	}
	
	
	
	
	@Override
	public void start(V root) {
		dfsRoot = root;
		tStack.push(EOS);
		super.start(root);
		
		if (showDebugInformation) System.out.println("Splitting off last component...");
		if(!eStack.isEmpty()){
			newComponent(eStack);
		}
	}
	
	
	@Override
	protected void preTraverse(E e, V w, boolean treeEdge) {
		super.preTraverse(e, w, treeEdge);
		if (showDebugInformation) System.out.println("preTraverse: " + e + " is tree edge: " + treeEdge + " startsPath: " + ((EdgeMap<E, V>) meta.getMetaInfo(MetaInfo.DFS_STARTS_NEW_PATH)).getBool(e));
		V v = e.getOtherVertex(w);
		// decrease number of not yet visited tree edges counter
		numNotVisitedTreeEdges.put(v, numNotVisitedTreeEdges.getInt(v)-1);
		
		// if edge starts a new path update TSTACK
		if (((EdgeMap<E, V>) meta.getMetaInfo(MetaInfo.DFS_STARTS_NEW_PATH)).getBool(e)){
			if (showDebugInformation) System.out.println("Traversing first edge "+e+". Tree edge: "+treeEdge);
			updateTStack(v, w, treeEdge);
		}
		
		// if e is a back edge check for possible multiple edges
		if (!treeEdge) {
			if(w == parent.get(v)) {
				EdgeList<E, V> el = new EdgeList<E, V>();
				el.add(e);
				el.add((E) treeArc.get(v));
				EdgeList<E, V> C = newComponent(el);
				E virtE = newVirtualEdge(C, w, v);
				// assign proper virtual edge
				for(E edge:C) {
					assignedVirtEdge.put(edge, virtE);
				}
				makeTreeEdge(virtE, w, v);
			} else {
				eStack.push(e);
			}
		}
	}


	@Override
	protected void postTraverse(E e, V w) {
		super.postTraverse(e, w);
		V v = e.getOtherVertex(w);
		
		// on backtracking the edge could already be hidden by multiple
		// edge case for type1 pairs
		if (isHidden.getBool(e)) {
			E eToPush = (E) assignedVirtEdge.get(e);
			// if assigned virtual edge is also already hidden (must be multiple edge case), 
			// use its assigned virtual edge, and so on
			while(isHidden.getBool(eToPush))
				eToPush = (E) assignedVirtEdge.get(eToPush);
			if (showDebugInformation) System.out.println("Edge ("+e+") was removed previously... pushing virtual edge ("+eToPush+") on EStack instead");
			eStack.push(eToPush);
		} else {
			eStack.push(e);
		}
		
		
		// check for separation pairs
		if (showDebugInformation) {
			System.out.println("\n");
			printEStack();
		}
		checkType2(e, v, w);
		checkType1(e, v, w);
		
		// if edge starts a path remove all triples on TSTACK 
		// down to an including EOS
		if (((EdgeMap<E, V>) meta.getMetaInfo(MetaInfo.DFS_STARTS_NEW_PATH)).getBool(e)) {
			while ((!tStack.isEmpty()) && (EOS != tStack.peek())) {
				TSItem removedItem = (TSItem)tStack.pop();
				if (showDebugInformation) System.out.println("removed item ("+removedItem.numH+
						","+removedItem.numA+","+removedItem.numB+
						") from TSTACK (segment finished)");
			}
			if (!tStack.isEmpty()) {
				// remove EOS
				tStack.pop();
				if (showDebugInformation) System.out.println("removed EOS from TSTACK");
			}
			
		}
		
		if (!tStack.isEmpty()) {
			// check high point condition
			TSItem i = (TSItem) tStack.peek();
			int highV = getHNum(v);
			while ((i != EOS) && (i.a != v) && (i.b != v) && (highV > i.numH)) {
				if (showDebugInformation) System.out.println("removed item ("+i.numH+
						","+i.numA+","+i.numB+") from TSTACK (high-point condition at "+getNum(v)+")");
				tStack.pop();
				i = (TSItem) tStack.peek();
			}
		}
	}




	/**
	 * updates the TSTACK during execution of dfs
	 * <br><br>
	 * see also [PGD08] chapter 3.5.5
	 * 
	 * @param v				the source vertex of the currently traversed edge
	 * @param w				the target vertex of the currently traversed edge
	 * @param isTreeEdge	true if the currently traversed edge is a tree edge, false otherwise
	 */
	protected void updateTStack(V v, V w, boolean isTreeEdge) {
		TSItem lastRemoved = null;
		TSItem itemToPush;
		int y = -1;
		// tree edge
		if (isTreeEdge) {
			while ((!tStack.isEmpty()) && (tStack.peek() != EOS) &&
					(((TSItem) tStack.peek()).numA > getL1Num(w))
			) {
				lastRemoved = (TSItem) tStack.pop();
				if (showDebugInformation) System.out.println("removed item ("+lastRemoved.numH+
						","+lastRemoved.numA+","+lastRemoved.numB+") from TSTACK");
				if (lastRemoved.numH > y) {
					y = lastRemoved.numH;
				}
			}
			if (lastRemoved == null) {
				itemToPush = new TSItem(
						getNum(w)+ getNumDesc(w)-1,
						(V) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_VERTEX)).get(w),
						v);
			} else {
				itemToPush = new TSItem(
						Math.max(y, getNum(w) + getNumDesc(w)-1),
								(V) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_VERTEX)).get(w),
								lastRemoved.b);
			}
			tStack.push(itemToPush);
			if (showDebugInformation) System.out.println("pushed item ("+itemToPush.numH+
					","+itemToPush.numA+","+itemToPush.numB+") on TSTACK");
			tStack.push(EOS);
			if (showDebugInformation) System.out.println("pushed EOS on TSTACK");
		// back edge
		} else {
			while ((!tStack.isEmpty()) && (tStack.peek() != EOS) &&
					(((TSItem) tStack.peek()).numA > getNum(w))
			) {
				lastRemoved = (TSItem) tStack.pop();
				if (showDebugInformation) System.out.println("removed item ("+lastRemoved.numH+
						","+lastRemoved.numA+","+lastRemoved.numB+") from TSTACK");
				if (lastRemoved.numH > y) {
					y = lastRemoved.numH;
				}
			}
			if (lastRemoved == null) {
				itemToPush = new TSItem(getNum(v),w,v);
			} else {
				itemToPush = new TSItem(y,w,lastRemoved.b);
			}
			tStack.push(itemToPush);
			if (showDebugInformation) System.out.println("pushed item ("+itemToPush.numH+
					","+itemToPush.numA+","+itemToPush.numB+") on TSTACK");
		}
	}
	
	
	/**
	 * checks the conditions for type-1 separation pairs and 
	 * splits off the corresponding component if one is found.
	 * <br><br>
	 * see also [PGD08] chapter 3.5.3
	 * 
	 * @param eBacktrack 	the edge over which dfs is currently backtracking
	 * @param v				the source vertex of eBacktrack
	 * @param w				the target vertex of eBacktrack
	 */
	protected void checkType1(E eBacktrack, V v, V w) {
		if (showDebugInformation) {
			System.out.println("checkType1 " + eBacktrack + ": " + v + ", " + w);
			System.out.println(v + ": " + getNum(v) + ", " + getL1Num(v) + ", " + getL2Num(v) + ", " + parent.get(v));
			System.out.println(w + ": " + getNum(w) + ", " + getL1Num(w) + ", " + getL2Num(w) + ", " + parent.get(w));
			System.out.println("numNotVisitedTreeEdges of " + v + ": " + numNotVisitedTreeEdges.getInt(v));
		}
		if ((getL2Num(w) >= getNum(v))
			&& (getL1Num(w) < getNum(v))
			&& ((parent.get(v) != dfsRoot)
				|| (numNotVisitedTreeEdges.getInt(v) > 0))
		) {
			// (v,lowpt1(w)) is a type-1 separation pair
			V lowpt1W = (V) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_VERTEX)).get(w);
			if (showDebugInformation) System.out.println("backtracking over edge "+eBacktrack+
					", found type-1 separation pair: ("+v+","+lowpt1W+") ");
			
			EdgeList<E, V> C = newComponent(new EdgeList<E, V>());
			E virtualEdge = null;
			int numW = getNum(w);
			//highest number in the component
			int h = numW + getNumDesc(w) - 1;
			
			if (showDebugInformation) System.out.println("removing edges with endpoints between "
					+numW+" and "+h);
			E e = null;
			if(!eStack.isEmpty()){
				e = (E) eStack.peek();
			}
			while ((!eStack.isEmpty()) &&
					(
						((numW <= getNum(e.getV1()))
							&& (getNum(e.getV1()) <= h))
						||
						(((numW	<= getNum(e.getV2()))
							&& (getNum(e.getV2()) <= h)))
					)) {
				
				// add edge to component
				e = (E) eStack.pop();
				if (isHidden.getBool(e)) {
					if (showDebugInformation) System.out.println(e+" is hidden!");
				}
				C = addToComponent(new EdgeList<E, V>(e), C);
				if(!eStack.isEmpty()){
					e = (E) eStack.peek();
				}
			}
			
			if (showDebugInformation) System.out.println("...Split component removed");
			
			// add virtual edge
			virtualEdge = newVirtualEdge(C, v, lowpt1W);
			// assign virtual edge
			for(E edge:C) {
				assignedVirtEdge.put(edge, virtualEdge);
			}
			
			// handle possible multiple edge
			if(!eStack.isEmpty()) {
				e = (E) eStack.peek();
				if (isSameEdge(e, v, lowpt1W)) {

					if (showDebugInformation) System.out.println("Multiple edge discovered at "+e);
					e = (E) eStack.pop();
					EdgeList<E, V> el = new EdgeList<E, V>(e);
					el.add(virtualEdge);
					C = newComponent(el);
					virtualEdge = newVirtualEdge(C, v, lowpt1W);
					// assign virtual edge
					for(E edge:C) {
						assignedVirtEdge.put(edge, virtualEdge);
					}
				}	
			}
			
			if (lowpt1W != parent.get(v)) {
				eStack.push(virtualEdge);
			} else {
				// handle another multiple edge
				if (showDebugInformation) System.out.println("Another multiple edge discovered at "+virtualEdge);
				E treeArcOfV = (E) treeArc.get(v);
				if (showDebugInformation && isHidden.getBool(treeArcOfV)) System.out.println("Tree Edge "+treeArcOfV+" is hidden!");
				EdgeList<E, V> el = new EdgeList<E, V>(treeArcOfV);
				el.add(virtualEdge);
				C = newComponent(el);
				virtualEdge = newVirtualEdge(C, lowpt1W, v);
				// assign virtual edge
				for(E edge:C) {
					assignedVirtEdge.put(edge, virtualEdge);
				}
				// update tree arc information
				treeArc.put(v, virtualEdge);
			}
			
			((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(v)).add(virtualEdge);//DFS_ADJ_LISTS
			makeTreeEdge(virtualEdge, lowpt1W, v);
			if (showDebugInformation) printEStack();
		}
		
	}
	
	
	/**
	 * checks the conditions for type-2 separation pairs and 
	 * splits off the corresponding component(s) if any are found
	 * <br><br>
	 * see also [PGD08] chapter 3.5.4
	 * 
	 * @param eBacktrack	the edge over which dfs is currently backtracking
	 * @param v				the source vertex of eBacktrack
	 * @param w				the target vertex of eBacktrack
	 */
	protected void checkType2(E eBacktrack, V v, V w){
		TSItem topTriple = null;
		if (!tStack.isEmpty()) {
			topTriple = (TSItem) tStack.peek();
		}
		EdgeList<E, V> adjOfW = ((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(w));//DFS_ADJ_LISTS
		V firstChildOfW = null;
		if (!adjOfW.isEmpty()) {
			firstChildOfW = (V) ((E) adjOfW.peek()).getOtherVertex(w);
		}
		int edgeCountOfW = ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_EDGE_COUNT)).getInt(w);
		if (showDebugInformation) {
			System.out.println("checkType2 " + eBacktrack + ": " + v + ", " + w);
			System.out.println("firstChildOfW: " + firstChildOfW);
			System.out.println("top triple: " + topTriple);
			System.out.println("edges of " + w + ": " + edgeCountOfW);//g.getEdges(w).size());
			//System.out.println("edges of " + w + ": " + g.getEdges(w));
			System.out.println("meta adj: " + ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(w).toString());//DFS_ADJ_LISTS
		}
		while ((v != dfsRoot) &&
				(
						((topTriple != null) && (topTriple.a == v))
						||
						((edgeCountOfW == 2) && (firstChildOfW != null)//g.getEdges(w).size()
								&& (getNum(firstChildOfW) > getNum(w)))				
				)) {
			
			if (showDebugInformation) printTStack();
			
			EdgeList<E, V> eAB = new EdgeList<E, V>();
			
			if ((topTriple.a == v) 
					&& parent.get(topTriple.b) == topTriple.a) {
				// (a,b) is no type 2 pair <- no inner vertex !!
				if (showDebugInformation) System.out.println("("+topTriple.a+","+topTriple.b+") has no inner vertices");
				tStack.pop();
				if (!tStack.isEmpty()) {
					topTriple = (TSItem) tStack.peek();
				} else {
					topTriple = null;
				}
			} else {
				EdgeList<E, V> C = newComponent(new EdgeList<E, V>());
				E virtEdge = null;
				
				if ((edgeCountOfW == 2) && (firstChildOfW != null) 
						&& (getNum(firstChildOfW) > getNum(w)))	{
					// simple case
					if (showDebugInformation) System.out.println("backtracking over edge "+eBacktrack+
							", found type-2 separation pair: ("+v+","+firstChildOfW+") (simple case)");
					
					E e = (E) eStack.pop();
					EdgeList<E, V> el = new EdgeList<E, V>(e);
					e = (E) eStack.pop();
					el.add(e);
					addToComponent(el, C);
					virtEdge = newVirtualEdge(C, v, firstChildOfW);
					// assign virtual edge
					for(E edge:C) {
						assignedVirtEdge.put(edge, virtEdge);
					}
					// check for possible multiple edge
					if (!eStack.isEmpty()) {
						e = (E) eStack.peek();
						// added for special case of nested p, in some cases multiple edges were stored on the stack -->
						V b = null;
						if (topTriple.b != INVALID_NODE)
							b = topTriple.b;
						else
							b = firstChildOfW;
						// <--
						if (isSameEdge(e, v, topTriple.b) || isSameEdge(e, v, firstChildOfW)) {//topTriple.b
							eAB.add((E) eStack.pop());
						}
					}
					
				} else {
					// complex case, (h,a,b) represents type 2 pair
					if (showDebugInformation) System.out.println("backtracking over edge "+eBacktrack+
							", found type-2 separation pair: ("+v+","+topTriple.b+")");
					topTriple = (TSItem)tStack.pop();
					E e = null;
					if (!eStack.isEmpty()) {
						e = (E) eStack.peek();
					}
					if (showDebugInformation) System.out.println("removing edges with endpoints between "
					+topTriple.numA+" and "+topTriple.numH);
					while ((e != null) && 
							(topTriple.numA <= getNum(e.getV1())) 
							&& (topTriple.numA <= getNum(e.getV2()))
							&& (getNum(e.getV1()) <= topTriple.numH)
							&& (getNum(e.getV2()) <= topTriple.numH)) {
						
						e = (E) eStack.pop();
						if (isSameEdge(e, topTriple.a, topTriple.b)) {
							eAB.add(e);
						} else {
							C = addToComponent(new EdgeList<E, V>(e), C);
						}
						
						if (!eStack.isEmpty()) {
							e = (E) eStack.peek();
						} else {
							e = null;
						}
					}
					if (showDebugInformation) System.out.println("...Split component removed");
					virtEdge = newVirtualEdge(C, topTriple.a, topTriple.b);
					// assign virtual edge
					for(E edge:C) {
						assignedVirtEdge.put(edge, virtEdge);
					}
				}
				// handle possible multiple edge
				if (!eAB.isEmpty()){
					if (showDebugInformation) System.out.println("multiple edges found at "+virtEdge);
					eAB.add(virtEdge);
					C = newComponent(eAB);
					V b = null;
					// additional workaround, because in some cases multiple edges were stored on the stack
					if (topTriple.b == INVALID_NODE || (firstChildOfW != null && isSameEdge(eAB.peek(), v, firstChildOfW)))
						b = firstChildOfW;
					else
						b = topTriple.b;
					virtEdge = newVirtualEdge(C, v, b);//topTriple.b
					if (showDebugInformation) System.out.println("multiple edges found at "+virtEdge);
					// assign virtual edge
					for(E edge:C) {
						assignedVirtEdge.put(edge, virtEdge);
					}
				}
				
				eStack.push(virtEdge);
				makeTreeEdge(virtEdge, v, virtEdge.getOtherVertex(v));
				w = virtEdge.getOtherVertex(v);
				parent.put(w, v);
				if (showDebugInformation) System.out.println("continuing checking ("+v+","+w+")");
				
				if (!tStack.isEmpty()) {
					topTriple = (TSItem) tStack.peek();
				} else {
					topTriple = null;
				}
				adjOfW = ((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(w));//DFS_ADJ_LISTS
				if (!adjOfW.isEmpty()) {
					firstChildOfW = (V) ((E) adjOfW.peek()).getOtherVertex(w);
				}
				edgeCountOfW = ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_EDGE_COUNT)).getInt(w);
			}
			
			
		}
		
	}

	/**
	 * creates a new component based on the given edges and 
	 * edges are removed from the graph. 
	 * The component is added to the component vector.
	 * 
	 * @param compEdges	an EdgeList of the edges for the new component
	 */
	protected EdgeList<E, V> newComponent(EdgeList<E, V> compEdges){
		
		removeEdges(compEdges);
		comp.add(compEdges);
		return compEdges;
	}
	
	/**
	 * adds a List of edges to the given component 
	 * and removes them from the graph.
	 * 	
	 * @param compEdges	list of edges to add to component
	 * @param component the component to which the edges are added
	 * @return 			the updated component
	 */
	protected EdgeList<E, V> addToComponent(EdgeList<E, V> compEdges, EdgeList<E, V> component){
		removeEdges(compEdges);
		component.addAll(compEdges);
		return component;
	}
	
	
	/**
	 * removes the given edges from the graph (by hiding them).
	 * Also updates the proper adjacency lists
	 * 
	 * @param edges
	 */
	protected void removeEdges(EdgeList<E, V> edges){
		for (E e : edges){
			// remove edge from adjacency list
			// should be last element by imposed ordering
			EdgeList<E, V> adj = ((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(((E) e).getV1()));//DFS_ADJ_LISTS
			if (!adj.isEmpty()) {
				if (showDebugInformation) System.out.println("Remove edge adjList of " + e.getV1() +": " + adj);
				boolean e2 = adj.remove(e);//adj.remove(adj.size() - 1); //popLast();
				if (showDebugInformation) System.out.println("Remove edge " + e + " from " + e.getV1() + " : removed edge " + e2);
			}
			try {
				g.removeEdge((E) e);
				updateEdgeCount(e.getV1(), -1);
				updateEdgeCount(e.getV2(), -1);
				isHidden.put(e, true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * creates a new virtual edge leading from the given source node 
	 * to the given target node. The edge added to the given component.
	 * 
	 * @param component	the component to add the virtual edge to
	 * @param v		the source node of the virtual edge
	 * @param w		the target node of the virtual edge
	 * @return		the virtual edge
	 */
	protected E newVirtualEdge(EdgeList component, V v, V w){
		// create virtual edge and add edges to component
		E virtualEdge = (E) ((TCTreeSkeleton) g).addVirtualEdge(v, w);
		updateEdgeCount(v, 1);
		updateEdgeCount(w, 1);
		virtualEdge.setId(UUID.randomUUID().toString());
		if (showDebugInformation) System.out.println("newVirtualEdge " + v + "; " + w + ": " + virtualEdge);
		virtEdge.put(virtualEdge, true);
		component.add(0, virtualEdge);  
		// update adjacency list
		if (showDebugInformation) System.out.println(((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(v)));//DFS_ADJ_LISTS
		((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(v)).add(virtualEdge);//DFS_ADJ_LISTS
		if (showDebugInformation) {
			System.out.println(((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(v)));//DFS_ADJ_LISTS
			System.out.println(((EdgeList<E, V>) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_ORDERED_ADJ_LISTS)).get(v)).peek());//DFS_ADJ_LISTS
		}
		
		return virtualEdge;
	}
	
	/**
	 * makes the given edge a tree edge in the palm tree of g, leading 
	 * from the given source node to the given target node.
	 * 
	 * @param e	the edge to be made a tree edge
	 * @param v	the source node of the tree edge
	 * @param w the target node of the tree edge
	 */
	protected void makeTreeEdge(E e, V v, V w){
		if (showDebugInformation) System.out.println("makeTreeEdge: " + e + "; " + v + "; " + w);
		e.setVertices(v, w);
		edgeType.put(e, TREE_EDGE);
	}
	
	private void updateEdgeCount(V node, int i) {
		((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_EDGE_COUNT)).put(node, 
				((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_EDGE_COUNT)).getInt(node) + i);
	}
	
	/**
	 * returns the number of a given vertex
	 * @param node
	 * @return
	 */
	private int getNum(V node){
		return ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM_V)).getInt(node);
	}
	
	/**
	 * returns the number of the given vertex' low-point 1.
	 * @param node
	 * @return
	 */
	private int getL1Num(V node){
		return (Integer) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT1_NUM)).get(node);
	}
	
	/**
	 * returns the number of the given vertex' low-point 2.
	 * @param node
	 * @return
	 */
	private int getL2Num(V node){
		return (Integer) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_LOWPT2_NUM)).get(node);
	}
	
	/**
	 * returns the number of descendants of the given vertex
	 * @param node
	 * @return
	 */
	private int getNumDesc(V node){
		return (Integer) ((NodeMap<V>) meta.getMetaInfo(MetaInfo.DFS_NUM_DESC)).get(node);
	}
	
	/**
	 * returns the number of the given vertex' high-point.
	 * @param node
	 * @return
	 */
	private int getHNum(V node){
		if (!((NodeList<V>) highpt.get(node)).isEmpty()) {
			return getNum(((NodeList<V>) highpt.get(node)).get(0));
		} else {
			return 0;
		}
	}
	
	/**
	 * checks if the given edge has the two given end-points, 
	 * i.e., if they represent the same edge.
	 * @param e
	 * @param v
	 * @param w
	 * @return
	 */
	private boolean isSameEdge(E e, V v, V w){
		if (
				((e.getV1() == v) && (e.getV2() == w))
				||
				((e.getV1() == w) && (e.getV2() == v))	
		) {
			return true;
		}
		return false;
	}
	
	/**
	 * prints the content of TSTACK
	 */
	private void printTStack() {
		System.out.print("TStack contains: ");
		TSItem t = null;
		for(Object o : tStack) {
			t = (TSItem) o;
			System.out.print(t+" ");
		}
		System.out.print("\n");
	}
	
	/**
	 * prints the content of ESTACK
	 */
	private void printEStack() {
		System.out.print("EStack contains: ");
		E e = null;
		for(Object o : eStack) {
			e = (E) o;
			System.out.print("("+e+") ");
		}
		System.out.print("\n");
	}
	
	
	/**
	 * Inner class implementing the items to be pushed on the TSTACK 
	 * data structure. A TSITEM contains the two vertices of a possible 
	 * type-2 pair, their numbers, and the highest number occurring in 
	 * the corresponding split component.
	 * 
	 * @author Martin Mader
	 *
	 */
	protected class TSItem{
		private V a = (V) INVALID_NODE;
		private V b = (V) INVALID_NODE;
		private int numH = -1;
		private int numA = -1;
		private int numB = -1;
		
		private TSItem(){
			
		}
		
		private TSItem(int numH, V a, V b){
			this.a = a;
			this.b = b;
			this.numH = numH;
			numA = getNum(a);
			numB = getNum(b);
		}
		
		public String toString(){
			if(this == EOS) return "EOS";
			else return "("+numH+","+numA+","+numB+")";
		}
	}
	
	

}

