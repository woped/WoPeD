package de.hpi.bpt.graph.algo.tctree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

public class BiconnectivityCheck<E extends IEdge<V>, V extends IVertex> {
	
	protected class NodeAttrs {
		boolean visited;
        boolean cut;
        V parent;
        int low;
        int dis;

        public NodeAttrs() {
            visited = false;
            cut = false;
            parent = null;
            low = 0;
            dis = 0;
        }
    }
	
	private IGraph<E,V> graph;
	private Iterator<V> nodes = null;
    private Hashtable<V,NodeAttrs> attrs = null;
    
    private Collection<EdgeList<E,V>> components = new ArrayList<EdgeList<E,V>>();
    private Stack<E> s = new Stack<E>();
    private int time;
    private V startNode;
    
    private boolean isBiconnected;
	
	public BiconnectivityCheck(IGraph<E,V> graph) {
		this.nodes = graph.getVertices().iterator();
        this.attrs = new Hashtable<V,NodeAttrs>(graph.getVertices().size());
		this.graph = graph;
		while (nodes.hasNext()) {
            prepareNode(nodes.next());
        }
		
		startNode = graph.getVertices().iterator().next();
		
		this.time = 0;
        
        if (startNode != null) {
        	process(startNode);
        	this.isBiconnected = this.components.size() == 1;
        } else
        	this.isBiconnected = false;
	}
	
	public boolean isBiconnected() {
		return this.isBiconnected;
	}
	
	private void process(V v) {
		NodeAttrs att = (NodeAttrs)attrs.get(v);
        att.visited = true;
        time++;
        att.dis = time;
        att.low = att.dis;
        V w;
        
        Collection<E> edges = new ArrayList<E>();
        edges.addAll(this.graph.getEdges(v));
        
        for (E e : edges) {
            if (v.equals(e.getV1())) w = e.getV2();
            else w = e.getV1();
            
            NodeAttrs watt = (NodeAttrs)attrs.get(w);
            
            if (!watt.visited) {
                s.push(e);
                watt.parent = v;
                process(w);
                
                if (watt.low >= att.dis) {
                    if (att.dis != 1) {
                        att.cut = true;
                    } else if (watt.dis > 2) {
                        att.cut = true;
                    }
                    addComponent(e);
                }
                if (watt.low < att.low) {
                    att.low = watt.low;
                }
            } else if (!compareInts(att.parent, w) && (watt.dis < att.dis)) { // (att.parent != w)
                s.push(e);
                if (watt.dis < att.low) {
                    att.low = watt.dis;
                }
            }
        }
        
        time++;
	}

	private void addComponent(E e) {
       EdgeList<E,V> comp = new EdgeList<E,V>();

        E f;
        do {
            f = s.pop();
            comp.add(f);
        } while (e != f);
        
        components.add(comp);
    }

    private boolean compareInts(V i1, V i2) {
    	if (i1==null && i2==null) return true;
    	if (i1!=null) return i1.equals(i2);
    	if (i2!=null) return false;
    	
    	return true;
    }
    
	private void prepareNode(V node) {
        attrs.put(node, new NodeAttrs());
    }
    
    private boolean visited(V node) {
        return ((NodeAttrs)attrs.get(node)).visited;
    }
}
