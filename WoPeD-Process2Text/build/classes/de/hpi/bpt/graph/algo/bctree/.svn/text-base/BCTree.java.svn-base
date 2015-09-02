package de.hpi.bpt.graph.algo.bctree;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.graph.algo.GraphAlgorithms;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * The tree of the biconnected components
 * 
 * @author Artem Polyvyanyy
 * 
 * Hopcroft, J.; Tarjan, R. (1973). "Efficient algorithms for graph manipulation". Communications of the ACM 16: 372–378.
 * 
 * v	- number of vertices
 * e	- number of edges
 * edgelist[l:2 X e] is the initial list of edges of the graph
 * bicomponents[1:3 X e] is the list of edges for each component found (each component is preceded by an entry giving the number of edges of the component)
 * bptr is a pointer to the last entry of bicomponents
 * 
 * global variables
 * ----------------
 * head[v-Fl:v+2Xe] and next[1 :v-t-2 Xe] - structural representation of the graph
 * freenext - last entry in the array next
 * 
 * local variables
 * ---------------
 * number[1 :v+l] - array used for numbering the vertices during the DFS.
 * code - current highest vertex number
 * edgestack[l:2 X e] - storage of edges examined during search
 * eptr - pointer to last entry in edgestack
 * point - current point being examined during DFS
 * v2 - next point to be examined during DFS
 * newlowpt - lowpoint for the biconnected part of graph above and including v2
 * oldptr - pointer to position in bicomponents to place a value of next component
 * 
 * global procedures
 * -----------------
 * - min - munimum
 * - add2(A, B, STACK, PTR): This procedure adds value A followed by value B to the top of stack STACK and increments the pointer to the top of the stack (PTR). Stacks are represented as arrays; the top of the stack is the highest filled location.
 * - nextlink(POINT, VALUE): This procedure is used to build the structural representation of a graph. It adds VALUE to the list of vertices adjacent to POINT. (POINT, VALUE) is an edge (possibly directed) of the graph.
 * 
 * 
 * procedure biconnect(v, e, bptr, edgelist, bicomponents);
 * value v, e; integer v, e, bptr;
 * integer array edgelist, bicomponen/s;
 * 
 * begin
 * 
 * integer array number[l:v+1], edgestack[1:2 X e];
 * integer code, eptr, point, v2, newlowpt, oldptr, i;
 * 
 * //Recursive procedure to search a connected component and find its biconnected components using DFS.
 * procedure biconnector (point, oldpt, lowpoint);
 *   integer point, oldpt, lowpoint;
 *    
 *   // point - startpoint of the search
 *   // oldpt - previous startpoint
 *   // lowpoint - lowest point reachable on a path found during search
 * 
 *   for i := i while next[point] > 0 do // Examine each edge out of point
 *   begin
 *     // v2 is the head of the edge. Delete edge from structural representation
 *     integer v2 := head[next[point]];
 *     next[point] := next[next[point]];
 *     
 *     // if the edge has been searched in the other direction, then look for another edge
 * 	   if ((number[v2] < number[point]) && (v2!=oldpt)) then
 * 	   begin
 *       add2 (point, v2, edgestack, eptr); // add edge to edgestack
 *       if (number[v2] = 0) then
 *       begin
 * 		   number[v2] := code := code + 1; // new point found. Number it;		
 * 		   // initiate a DFS from the new point
 *         newlowpt : = v + 1;
 *         biconnector (v2, point, newlowpt);
 * 		
 *         lowpoint := min(lowpoint, newlowpt); // recalculate lowpoint;
 * 		
 * 		   if (newlowpt >= number[point]) then
 *         begin
 *           // point is an articulation point. Output edges of component from edgestack
 *           oldptr := bptr := bptr + 1;
 *           for i := i while number[edgestack[eptr-1]] > number[point] do
 *           begin
 * 		       add2(edgestack[eptr-1], edgestack[eptr], bicomponents, bptr);
 *             eptr := eptr - 2;
 *           end;
 *           add2(point, v2, bicomponents, bptr); // add last edge;
 *           eptr := eptr - 2;
 *           bicomponents[oldptr] := (bptr-oldptr) / 2; // compute number of edges of component
 *         end
 * 	    end
 * 	    else
 *      begin
 *        lowpoint := min(lowpoint, number[v2]); // new point not found. Recalculate lowpoint
 * 	    end;
 *    end;
 *   
 *    // construct the structural representation of the graph
 *    freenext := v;
 *    for i := 1 step 1 until v do next[i] := 0;
 *    for i := 1 step 1 until e do
 *    begin
 *      // each edge occurs twice, once for each endpoint
 *      nextlink(edgelist[2Xi-1], edgelist[2Xi]);
 *      nextlink(edgelist[2Xi], edgelist[2Xi-1]);
 *    end;
 *   
 *    // initialize variables for search
 *    eptr:=0; bptr:=0; point:=1; v2:=0;
 *    for i:=1 step 1 until v+1 do number[i]:=0;
 *    for i:=i while point < v do
 *    begin
 *      // each execution of biconnector searches a connected component of the graph. After each search, find an unnumbered vertex and search again. Repeat until all vertices are examined
 *      number[point]:=code:=l; newlowpt:=v+1;
 *      biconnector(point, v2, newlowpt);
 *      for i: i while number[point] != 0 do point:=point+1
 *   end
 * end;
 */
public class BCTree<E extends IEdge<V>, V extends IVertex> extends DFS<E,V> {

    protected class NodeAttrs extends DFS<E,V>.NodeAttrs {
        boolean cut;
        V parent;
        int low;
        int dis;

        public NodeAttrs() {
            super();
            cut = false;
            parent = null;
            low = 0;
            dis = 0;
        }
    }

    private Collection<BCTComponent<E,V>> components = new ArrayList<BCTComponent<E,V>>();
    private Collection<V> artPoints = new ArrayList<V>();
    private Stack<E> s = new Stack<E>();
    private int time;
    private V startNode;
    
    private V source = null;
    private V sink = null;
    
    private BCTreeNode<E,V> root;
    
    private GraphAlgorithms<E,V> ga = new GraphAlgorithms<E,V>();
    
    public BCTree(IGraph<E,V> graph) throws FileNotFoundException {
        super(graph);
        
        startNode = graph.getVertices().iterator().next();
        
        boolean backEdge = false;
        Collection<V> bs = ga.getBoundaryVertices(graph);
		if (bs.size() == 2) {
			Iterator<V> bi = bs.iterator();
        	this.source = bi.next();
    		this.sink = bi.next();
    		graph.addEdge(sink, source);
    		backEdge = true;
        }
		
        this.constructBCTree();
		
        if (backEdge) {
        	E e = graph.getEdge(sink, source);
        	graph.removeEdge(e);
        	e = this.root.getGraph().getEdge(sink, source);
        	this.root.getGraph().removeEdge(e);
        }
    }

    protected void prepareNode(V node) {
        NodeAttrs a = new NodeAttrs();
        attrs.put(node, a);
    }

    private void addComponent(E e) {
        BCTComponent<E,V> g = new BCTComponent<E,V>(this.graph);

        E f;
        do {
            f = s.pop();
            g.addEdge(f.getV1(), f.getV2());
        } while (e != f);
        
        components.add(g);
    }

    protected void process(V v) {
        NodeAttrs att = (NodeAttrs)attrs.get(v);
        att.visited = true;
        time++;
        att.dis = time;
        att.low = att.dis;
        V w;
        
        Collection<E> edges = new ArrayList<E>();
        edges.addAll(this.graph.getEdges(v));
        
        for (E e : edges) {
        	w = e.getOtherVertex(v);
            
            NodeAttrs watt = (NodeAttrs)attrs.get(w);
            
            if (!watt.visited) {
                s.push(e);
                watt.parent = v;
                process(w);
                
                if (watt.low >= att.dis) {
                    if (att.dis != 1) {
                        att.cut = true;
                        artPoints.add(v);
                    } else if (watt.dis > 2) {
                        att.cut = true;
                        artPoints.add(v);
                    }
                    addComponent(e);
                }
                if (watt.low < att.low) {
                    att.low = watt.low;
                }
            } else if (!compareNodes(att.parent, w) && (watt.dis < att.dis)) { // (att.parent != w)
                s.push(e);
                if (watt.dis < att.low) {
                    att.low = watt.dis;
                }
            }
        }
        
        time++;
    }
    
    private boolean compareNodes(V i1, V i2) {
    	if (i1==null && i2==null) return true;
    	if (i1!=null) return i1.equals(i2);
    	if (i2!=null) return false;
    	
    	return true;
    }

    protected void constructBCTree() throws FileNotFoundException {        
        time = 0;
        
        if (startNode != null) process(startNode);
        else return;
        /*
        for (BCTComponent<E,V> gi : this.components) {
            for (E edge: gi.getEdges()) {
            	gi.addVertex(edge.getV1());
            	gi.setLabel(edge.getV1(), this.graph.getLabel(edge.getV1()));
            	gi.setTag(edge.getV1(), this.graph.getTag(edge.getV1()));
            	gi.addVertex(edge.getV2());
            	gi.setLabel(edge.getV2(), this.graph.getLabel(edge.getV2()));
            	gi.setTag(edge.getV2(), this.graph.getTag(edge.getV2()));
            }
        }
        */
        constructTree();
    }
    
    protected void constructTree() {
    	if (this.source!=null && this.sink!=null) {
    		for (BCTComponent<E,V> g : this.components) {
        		if (g.getVertices().contains(this.source) && g.getVertices().contains(this.sink)) {
        			root = new BCTreeNode<E,V>(g);
        			break;
        		}
        	}
    		
    		constructTree(root);
    	}
    }
    
    protected void constructTree(BCTreeNode<E,V> node) {
		if (node.getNodeType()==BCType.B) {
			for (V p : this.artPoints) {
				if (node.getGraph().getVertices().contains(p)) {
					V p2 = null;
					if (node.getParentNode()!=null)
						p2 = node.getParentNode().getPoint();
					
					if (!p.equals(p2)) {
						BCTreeNode<E,V> child = new BCTreeNode<E,V>(p);
						node.addChild(child);
						child.setParent(node);
						constructTree(child);
					}
				}
			}
		}
		else if (node.getNodeType()==BCType.C) {
			for (BCTComponent<E,V> g : this.components) {
				if (g.getVertices().contains(node.getPoint()) && g!=node.getParentNode().getGraph()) {
					BCTreeNode<E,V> child = new BCTreeNode<E,V>(g);
					node.addChild(child);
					child.setParent(node);
					constructTree(child);
				}
			}
		}
	}

	public BCTreeNode<E,V> getRoot() {
    	return this.root;
    }
	
	public Collection<V> getArticulationPoints() {
    	return this.artPoints;
    }
	
	public Collection<BCTComponent<E,V>> getBiconnectedComponents() {
		return this.components;
	}
	
	public Collection<BCTreeNode<E,V>> getGraphsInOrder() {
		Collection<BCTreeNode<E,V>> result = new ArrayList<BCTreeNode<E,V>>();
		
		Stack<BCTreeNode<E,V>> nodes = new Stack<BCTreeNode<E,V>>();
		nodes.push(this.root);
		BCTreeNode<E,V> currentNode;
		while (!nodes.isEmpty()) {
			currentNode = nodes.pop();
			
			for (BCTreeNode<E,V> child : currentNode.getChildren()) 
				nodes.push(child);
			
			if (currentNode.getNodeType()==BCType.B)
				result.add(currentNode);
		}
		
		return result;
	}
}
