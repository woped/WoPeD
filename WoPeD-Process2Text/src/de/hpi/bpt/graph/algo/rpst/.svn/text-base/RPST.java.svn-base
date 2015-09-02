package de.hpi.bpt.graph.algo.rpst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.hpi.bpt.graph.abs.AbstractDirectedGraph;
import de.hpi.bpt.graph.abs.IDirectedEdge;
import de.hpi.bpt.graph.abs.IDirectedGraph;
import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.algo.DirectedGraphAlgorithms;
import de.hpi.bpt.graph.algo.tctree.TCTree;
import de.hpi.bpt.graph.algo.tctree.TCTreeEdge;
import de.hpi.bpt.graph.algo.tctree.TCTreeNode;
import de.hpi.bpt.graph.algo.tctree.TCType;
import de.hpi.bpt.hypergraph.abs.IVertex;
import de.hpi.bpt.hypergraph.abs.Vertex;

/**
 * The Refined Process Structure Tree
 * 
 * @author Artem Polyvyanyy
 * 
 * Artem Polyvyanyy, Jussi Vanhatalo, and Hagen Voelzer. 
 * Simplified Computation and Generalization of the Refined Process Structure Tree. 
 * Proceedings of the 7th International Workshop on Web Services and Formal Methods (WS-FM).
 * Hoboken, NJ, US, September 2010;
 */
public class RPST <E extends IDirectedEdge<V>, V extends IVertex>
				extends AbstractDirectedGraph<RPSTEdge<E,V>, RPSTNode<E,V>> {

	private IDirectedGraph<E,V> graph = null;
	
	private E backEdge = null;
	
	private Collection<E> extraEdges = null;
	
	private TCTree<IEdge<V>,V> tct = null;
	
	private DirectedGraphAlgorithms<E,V> dga = new DirectedGraphAlgorithms<E,V>();
	
	private RPSTNode<E,V> root = null;
	
	
	@Override
	public RPSTEdge<E,V> addEdge(RPSTNode<E,V> v1, RPSTNode<E,V> v2) {
		if (v1 == null || v2 == null) return null;
		
		Collection<RPSTNode<E,V>> ss = new ArrayList<RPSTNode<E,V>>(); ss.add(v1);
		Collection<RPSTNode<E,V>> ts = new ArrayList<RPSTNode<E,V>>(); ts.add(v2);
		
		if (!this.checkEdge(ss, ts)) return null;
		
		return new RPSTEdge<E,V>(this, v1, v2);
	}
	
	@SuppressWarnings("unchecked")
	public RPST(IDirectedGraph<E,V> g) {
		if (g==null) return;
		this.graph = g;
		
		Collection<V> sources = dga.getInputVertices(this.graph);
		Collection<V> sinks = dga.getOutputVertices(this.graph);
		if (sources.size()!=1 || sinks.size()!=1) return;
		
		V src = sources.iterator().next();
		V snk = sinks.iterator().next();
		
		this.backEdge = this.graph.addEdge(snk, src);
		
		// expand mixed vertices
		this.extraEdges = new ArrayList<E>();
		Map<V,V> map = new HashMap<V,V>();
		for (V v : this.graph.getVertices()) {
			if (this.graph.getIncomingEdges(v).size()>1 &&
					this.graph.getOutgoingEdges(v).size()>1) {
				V newV = (V) (new Vertex());
				newV.setName(v.getName()+"*");
				map.put(newV, v);
				this.graph.addVertex(newV);
				
				for (E e : this.graph.getOutgoingEdges(v)) {
					this.graph.addEdge(newV,e.getTarget());
					this.graph.removeEdge(e);
				}
				
				E newE = this.graph.addEdge(v, newV);
				this.extraEdges.add(newE);
			}
		}
		
		// compute TCTree
		this.tct = new TCTree(this.graph,this.backEdge);
		
		// remove extra edges
		Set<TCTreeNode<IEdge<V>,V>> quasi = new HashSet<TCTreeNode<IEdge<V>,V>>();
		for (TCTreeNode trivial : this.tct.getVertices(TCType.T)) {
			
			if (this.isExtraEdge(trivial.getBoundaryNodes())) {
				quasi.add(tct.getParent(trivial));
				this.tct.removeEdges(this.tct.getIncomingEdges(trivial));
				this.tct.removeVertex(trivial);
			}
		}
		
		// CONSTRUCT RPST

		// remove dummy nodes
		for (TCTreeNode<IEdge<V>,V> node: this.tct.getVertices()) {
			if (tct.getChildren(node).size()==1) {
				TCTreeEdge<IEdge<V>,V> e = tct.getOutgoingEdges(node).iterator().next();
				this.tct.removeEdge(e);
				
				if (this.tct.isRoot(node)) {
					this.tct.removeEdge(e);
					this.tct.removeVertex(node);
					this.tct.setRoot(e.getTarget());
				}
				else {
					TCTreeEdge<IEdge<V>,V> e2 = tct.getIncomingEdges(node).iterator().next();
					this.tct.removeEdge(e2);
					this.tct.removeVertex(node);
					this.tct.addEdge(e2.getSource(), e.getTarget());
				}
			}
		}
		
		// construct RPST nodes
		Map<TCTreeNode<IEdge<V>,V>,RPSTNode<E,V>> map2 = new HashMap<TCTreeNode<IEdge<V>,V>, RPSTNode<E,V>>();
		for (TCTreeNode<IEdge<V>,V> node: this.tct.getVertices()) {
			if (node.getType()==TCType.T && node.getBoundaryNodes().contains(src) &&
					node.getBoundaryNodes().contains(snk)) continue;
			
			RPSTNode<E,V> n = new RPSTNode<E,V>();
			n.setType(node.getType());
			n.setName(node.getName());
			Iterator<V> bs = node.getBoundaryNodes().iterator();
			V b1 = bs.next();
			b1 = (map.get(b1)==null) ? b1 : map.get(b1); 
			n.setEntry(b1);
			V b2 = bs.next();
			b2 = (map.get(b2)==null) ? b2 : map.get(b2);
			n.setExit(b2);
			if (quasi.contains(node)) n.setQuasi(true);
			
			for (IEdge<V> e : node.getSkeleton().getEdges()) {
				IEdge<V> oe = node.getSkeleton().getOriginal(e);
				if (oe == null) {
					if (node.getSkeleton().isVirtual(e)) {
						V s = map.get(e.getV1()); 
						V t = map.get(e.getV2());
						if (s == null) s = e.getV1();
						if (t == null) t = e.getV2();
						n.getSkeleton().addVirtualEdge(s,t);
					}
					
					continue;
				}
				
				if (oe instanceof IDirectedEdge<?>) {
					IDirectedEdge<V> de = (IDirectedEdge<V>) oe;
					
					if (de.getSource().equals(map.get(de.getTarget()))) continue;
					
					V s = map.get(de.getSource()); 
					V t = map.get(de.getTarget());
					if (s == null) s = de.getSource();
					if (t == null) t = de.getTarget();
					
					if (s.equals(snk) && t.equals(src)) continue;
					n.getSkeleton().addEdge(s,t);
				}
			}
			
			this.addVertex(n);			
			map2.put(node, n);
		}
		
		// build tree
		for (TCTreeEdge<IEdge<V>,V> edge : this.tct.getEdges()) {
			this.addEdge(map2.get(edge.getSource()), map2.get(edge.getTarget()));
		}
		this.root = map2.get(this.tct.getRoot());
		
		// fix graph
		for (E e : this.extraEdges) {
			for (E e2 : this.graph.getOutgoingEdges(e.getTarget())) {
				this.graph.addEdge(e.getSource(), e2.getTarget());
				this.graph.removeEdge(e2);
			}
			this.graph.removeVertex(e.getTarget());
		}
		
		this.iterativePreorder();
		// fix entries/exits
		for (RPSTNode<E,V> n : this.getVertices()) {
			if (this.isRoot(n)) { n.setEntry(src); n.setExit(snk); }
			else {
				V entry = n.getEntry();
				
				int cinf = n.getFragment().getIncomingEdges(entry).size();
				int coutf = n.getFragment().getOutgoingEdges(entry).size();
				//int cing = this.graph.getIncomingEdges(entry).size();
				int coutg = this.graph.getOutgoingEdges(entry).size();
				
				if (cinf==0 || coutf==coutg) continue;
				
				//System.err.println(n.getName());
				V exit = n.getExit();
				n.setEntry(exit);
				n.setExit(entry);
			}
		}
		
		this.graph.removeEdge(this.backEdge);
	}
	
	public void iterativePreorder() {
		Stack<RPSTNode<E,V>> nodes = new Stack<RPSTNode<E,V>>();
		nodes.push(this.getRoot());
		RPSTNode<E,V> currentNode;
		List<RPSTNode<E,V>> list = new ArrayList<RPSTNode<E,V>>();
		//List<String> list2 = new ArrayList<String>();
		while (!nodes.isEmpty()) {
			currentNode = nodes.pop();
			list.add(0, currentNode);
			//list2.add(0,currentNode.getName());
			for (RPSTNode<E,V> n : this.getChildren(currentNode)) {
				nodes.push(n);
			}
		}
		
		//System.err.println(list2);
		for (RPSTNode<E,V> n : list) {
			for (E e : n.getSkeleton().getEdges()) n.getFragment().addEdge(e.getSource(),e.getTarget());
			for (RPSTNode<E,V> c : this.getChildren(n)) {
				if (c.getType()!=TCType.T)
					for (E e2 : c.getFragment().getEdges()) n.getFragment().addEdge(e2.getSource(),e2.getTarget());
			}
		}
	}
	
	private boolean isExtraEdge(Collection<V> vs) {
		for (E e : this.extraEdges) {
			if (vs.contains(e.getSource()) && vs.contains(e.getTarget()))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Get original graph
	 * @return original graph
	 */
	public IDirectedGraph<E,V> getGraph() {
		return this.graph;
	}
	
	/**
	 * Get root node
	 * @return root node
	 */
	public RPSTNode<E,V> getRoot() {
		return this.root;
	}
	
	/**
	 * Get children of the RPST node
	 * @param node RPST node
	 * @return children of the node
	 */
	public Collection<RPSTNode<E,V>> getChildren(RPSTNode<E,V> node) {
		return this.getSuccessors(node);
	}
	
	/**
	 * Get parent node
	 * @param node node
	 * @return parent of the node
	 */
	public RPSTNode<E,V> getParent(RPSTNode<E,V> node) {
		return this.getFirstPredecessor(node);
	}
	
	@Override
	public String toString() {
		return toStringHelper(this.getRoot(), 0);
	}
	
	private String toStringHelper(RPSTNode<E,V> tn, int depth) {
		String result = "";
		for (int i = 0; i < depth; i++){
			result += "   ";
		}
		result += tn.toString();
		result += "\n";
		for (RPSTNode<E,V> c: this.getChildren(tn)){
			result += toStringHelper(c, depth+1);
		}
		return result;
	}
	
	public boolean isRoot(RPSTNode<E,V> node) {
		if (node == null) return false;
		return node.equals(this.root);
	}
	
	public Collection<RPSTNode<E,V>> getVertices(TCType type) {
		Collection<RPSTNode<E,V>> result = new ArrayList<RPSTNode<E,V>>();
		
		Iterator<RPSTNode<E,V>> i = this.getVertices().iterator();
		while (i.hasNext()) {
			RPSTNode<E,V> n = i.next();
			if (n.getType()==type)
				result.add(n);
		}
		
		return result;
	}
}
