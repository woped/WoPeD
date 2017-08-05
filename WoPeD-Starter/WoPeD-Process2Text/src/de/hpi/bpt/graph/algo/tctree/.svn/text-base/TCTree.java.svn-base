package de.hpi.bpt.graph.algo.tctree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.graph.abs.AbstractDirectedGraph;
import de.hpi.bpt.graph.abs.AbstractMultiGraphFragment;
import de.hpi.bpt.graph.abs.IEdge;
import de.hpi.bpt.graph.abs.IGraph;
import de.hpi.bpt.hypergraph.abs.IVertex;

/**
 * The tree of the triconnected components
 * 
 * @author Artem Polyvyanyy
 * @author Christian Wiggert
 *
 * @param <E> template for edge (extends IEdge)
 * @param <V> template for vertex (extends IVertex)
 */
public class TCTree<E extends IEdge<V>, V extends IVertex> extends AbstractDirectedGraph<TCTreeEdge<E,V>, TCTreeNode<E,V>> {

	protected IGraph<E,V> graph;
	
	protected Collection<TCTreeNode<E,V>> nodes = new ArrayList<TCTreeNode<E,V>>();
	
	protected E backEdge;
	
	protected TCTreeNode<E,V> root = null;
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.graph.abs.AbstractDirectedGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public TCTreeEdge<E,V> addEdge(TCTreeNode<E,V> v1, TCTreeNode<E,V> v2) {
		if (v1 == null || v2 == null) return null;
		
		Collection<TCTreeNode<E,V>> ss = new ArrayList<TCTreeNode<E,V>>(); ss.add(v1);
		Collection<TCTreeNode<E,V>> ts = new ArrayList<TCTreeNode<E,V>>(); ts.add(v2);
		
		if (!this.checkEdge(ss, ts)) return null;
		
		return new TCTreeEdge<E,V>(this, v1, v2);
	}
	
	/**
	 * This class decomposes the given a given biconnected graph into a tree of triconnected components.
	 * If the given back edge is null, a random edge of the graph is chosen as back edge
	 * @param graph - a biconnected graph 
	 * @param edge - the according back edge
	 */
	public TCTree(IGraph<E,V> g, E edge) {
		if (g==null) return;
		this.graph = g;
		
		if (isTrivialCase()) {
			TCTreeSkeleton<E,V> sk = new TCTreeSkeleton<E,V>(this.graph);
			sk.copyOriginalGraph();
			this.root = new TCTreeNode<E,V>("T0");
			root.setSkeleton(sk);
			root.setType(TCType.T);
			this.addVertex(this.root);
			this.root.setBoundaryNodes(this.graph.getVertices());
		} else {
			TCTreeSkeleton<E,V> sk = new TCTreeSkeleton<E,V>(this.graph);
			sk.copyOriginalGraph();
			if (edge == null)
				// choose one edge to be the backEdge
				this.backEdge = sk.getEdges().iterator().next();
			else
				this.backEdge = edge;
			// graph must be biconnected
			BiconnectivityCheck<E,V> check = new BiconnectivityCheck<E,V>(sk);
			if (!check.isBiconnected()) return;
			
			// make first node
			this.root = new TCTreeNode<E,V>("0");
			root.setSkeleton(sk);
	
			// decompose
			ModelDecomposer<E, V> decomposer = new ModelDecomposer<E, V>();
			Collection<TCTreeNode<E, V>> newNodes = decomposer.getTriconnectedComponents(graph, root, backEdge);
			if (newNodes != null) {
				for (TCTreeNode<E, V> node:newNodes) {
					this.putNode(node);
				}
			}
			
			// classify node types
			this.classifyNodes();
			
			// construct tree
			this.constructTree();
		
			this.nodes.clear();
		}
	}
	
	/**
	 * A shot cut for TCTree(graph, null);
	 * @param graph - a biconnected graph
	 */
	public TCTree(IGraph<E,V> g) {
		this(g, null);
	}
	
	private boolean isTrivialCase() {
		if (this.graph.getEdges().size() == 1 && !(this.graph.getVertices().size() > 2))
			return true;
		return false;
	}
	
	/**
	 * Get original graph
	 * @return Graph
	 */
	public IGraph<E,V> getGraph() {
		return this.graph;
	}
	
	/**
	 * Add TC-tree node, if it was not added before
	 * @param node Node to add
	 */
	private void putNode(TCTreeNode<E,V> node) {
		Iterator<TCTreeNode<E,V>> i = this.nodes.iterator();
		
		Collection<V> vs1 = node.getSkeleton().getVertices();
		boolean flag = true;
		while (i.hasNext()) {
			TCTreeNode<E,V> n = i.next();
			
			Collection<V> vs2 = n.getSkeleton().getVertices();
			
			if (vs2.containsAll(vs1) && vs2.size()==vs1.size()) {
				flag = false;
				break;
			}
		}
		
		if (flag)
			this.nodes.add(node);
	}
	
	/**
	 * @param type
	 * @return
	 */
	public Collection<TCTreeNode<E,V>> getVertices(TCType type) {
		Collection<TCTreeNode<E,V>> result = new ArrayList<TCTreeNode<E,V>>();
		
		Iterator<TCTreeNode<E,V>> i = this.getVertices().iterator();
		while (i.hasNext()) {
			TCTreeNode<E,V> n = i.next();
			if (n.getType()==type)
				result.add(n);
		}
		
		return result;
	}
	
	/**
	 * Classify TCTree nodes on types: P,B,R
	 */
	private void classifyNodes() {
		int Pc, Bc, Rc;
		Pc = Bc = Rc = 0;
		
		Iterator<TCTreeNode<E,V>> i = this.nodes.iterator();
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
	
	private void constructTree() {
		// get root node - a node with back edge
		Iterator<TCTreeNode<E,V>> i = this.nodes.iterator();
		while (i.hasNext()) {
			TCTreeNode<E,V> n = i.next();
			E e = n.getSkeleton().getEdge(this.backEdge.getV1(), this.backEdge.getV2());
			if (e!=null) // && n.getSkeleton().isVirtual(e))
			{				
				this.root = n;
				break;
			}
		}
		if (this.root == null) return;
		this.addVertex(this.root);
		this.root.setBoundaryNodes(this.backEdge.getVertices());
	
		Collection<TCTreeNode<E,V>> ns = new ArrayList<TCTreeNode<E,V>>(this.nodes);
		ns.remove(this.root);
		constructChildren(this.root,ns);
		
		// construct trivial nodes
		int Tc = 0;
		for (TCTreeNode<E,V> node : this.getVertices()) {
			Collection<E> ts = new ArrayList<E>(node.getSkeleton().getEdges());
			ts.removeAll(node.getSkeleton().getVirtualEdges());
			for (E t : ts) {
				TCTreeNode<E,V> n = new TCTreeNode<E,V>();
				n.setType(TCType.T);
				n.setBoundaryNodes(t.getVertices());
				TCTreeSkeleton<E,V> sk = new TCTreeSkeleton<E,V>(this.graph);
				sk.addEdge(t.getV1(), t.getV2());
				n.setSkeleton(sk);
				n.setName("T"+(Tc++));
				this.addEdge(node,n);
			}
		}
	}
	
	private void constructChildren(TCTreeNode<E,V> n, Collection<TCTreeNode<E,V>> ns)
	{
		Collection<TCTreeNode<E,V>> nss = new ArrayList<TCTreeNode<E,V>>(ns);
		Collection<TCTreeNode<E,V>> ncs = new ArrayList<TCTreeNode<E,V>>();
		Collection<TCTreeNode<E,V>> nps = new ArrayList<TCTreeNode<E,V>>();
		
		Iterator<E> i = n.getSkeleton().getVirtualEdges().iterator();
		while (i.hasNext()) {
			E ve = i.next();
			
			Iterator<TCTreeNode<E,V>> j = ns.iterator();
			while (j.hasNext()) {
				TCTreeNode<E,V> vn = j.next();
				
				Collection<E> ves = vn.getSkeleton().getEdges(ve.getV1(), ve.getV2());
				if (this.containsVirtual(vn, ves)) {
					
					vn.setBoundaryNodes(ve.getVertices());
					
					nss.remove(vn);
					
					if (vn.getType() == TCType.B)
						nps.add(vn);
					else {
						ncs.add(vn);
					}
				}
			}
		}
		
		// first work with Bs
		Collection<TCTreeNode<E,V>> cncs = new ArrayList<TCTreeNode<E,V>>(ncs);
		Iterator<TCTreeNode<E,V>> j = nps.iterator();
		while (j.hasNext()) {
			TCTreeNode<E,V> p = j.next();
			
			// look for children
			Collection<TCTreeNode<E,V>> npc = new ArrayList<TCTreeNode<E,V>>();
			
			Iterator<TCTreeNode<E,V>> k = cncs.iterator();
			while (k.hasNext()) {
				TCTreeNode<E,V> cc = k.next();
				
				Iterator<V> pi = p.getSkeleton().getVertices().iterator();
				
				Collection<E> ves = cc.getSkeleton().getEdges(pi.next(), pi.next());
				if (this.containsVirtual(cc, ves)) {
					npc.add(cc);
					nss.remove(cc);
					ncs.remove(cc);
				}
			}
			
			// call recursion
			this.addEdge(n,p);
			Collection<TCTreeNode<E,V>> u = new ArrayList<TCTreeNode<E,V>>(nss);
			u.addAll(npc);
			this.constructChildren(p,u);
		}
		
		// now rest
		j = ncs.iterator();
		while (j.hasNext()) {
			TCTreeNode<E,V> vn = j.next();
			this.addEdge(n,vn);
			this.constructChildren(vn,nss);
		}
	}
	
	private boolean containsVirtual(TCTreeNode<E,V> n, Collection<E> ves) {
		
		Iterator<E> i = ves.iterator();
		while (i.hasNext()) {
			if (n.getSkeleton().getVirtualEdges().contains(i.next()))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Get TCTree node containing vertex of original graph
	 * @param v Vertex of original graph
	 * @return TCTree node that contains vertex
	 */
	public TCTreeNode<E,V> getVertex(V v)
	{
		Iterator<TCTreeNode<E,V>> i = this.getVertices().iterator();
		
		while (i.hasNext()) {
			TCTreeNode<E,V> n = i.next();
			if (n.getSkeleton().contains(v))
				return n;
		}
		
		return null;
	}
	
	/**
	 * Check if TCTree node is root
	 * @param node Node of the TCTree
	 * @return <code>true</code> if node is the root of TCTree, <code>false</code> otherwise
	 */
	public boolean isRoot(TCTreeNode<E,V> node)
	{
		if (node == null) return false;
		return node.equals(this.root);
	}
	
	/**
	 * Get root node
	 * @return root node
	 */
	public TCTreeNode<E,V> getRoot() {
		return this.root;
	}
	
	public void setRoot(TCTreeNode<E,V> node) {
		if (this.getVertices().contains(node))
			this.root = node;
	}
	
	/**
	 * Get parent of the TCTree node
	 * @param node TCTree node
	 * @return Parent of the node
	 */
	public TCTreeNode<E,V> getParent(TCTreeNode<E,V> node)
	{
		return this.getFirstPredecessor(node);
	}
	
	/**
	 * Get children of the TCTree node
	 * @param node TCTree node
	 * @return Children of the node
	 */
	public Collection<TCTreeNode<E,V>> getChildren(TCTreeNode<E,V> node)
	{
		return this.getSuccessors(node);
	}
	
	/**
	 * Get process graph fragment represented by TCTree node
	 * @param node TCTree node
	 * @return Process fragment
	 */
	public AbstractMultiGraphFragment<E,V> getFragment(TCTreeNode<E,V> node)
	{
		AbstractMultiGraphFragment<E,V> result = new AbstractMultiGraphFragment<E, V>(this.getGraph());
		this.getFragment(result, node);
		
		return result;
	}
	
	private void getFragment(AbstractMultiGraphFragment<E,V> frag, TCTreeNode<E,V> node)
	{
		Iterator<E> i = node.getSkeleton().getEdges().iterator();
		while (i.hasNext()) {
			E e = i.next();
			frag.addEdge(e.getV1(), e.getV2());
		}
		
		Iterator<TCTreeNode<E,V>> j = this.getChildren(node).iterator();
		while (j.hasNext()) {
			TCTreeNode<E,V> n = j.next();
			this.getFragment(frag, n);
		}
	}
	
	public AbstractMultiGraphFragment<E,V> getFragment(TCTreeNode<E,V> parent, TCTreeNode<E,V> child)
	{
		if (parent==null || child==null) return new AbstractMultiGraphFragment<E, V>(this.getGraph());
			
		Collection<TCTreeNode<E,V>> children = this.getChildren(parent);
		
		if (children.size() <= 1) return this.getFragment(parent);
		
		// combine 2 branches
		AbstractMultiGraphFragment<E,V> result = new AbstractMultiGraphFragment<E, V>(this.getGraph());
		
		int min = Integer.MAX_VALUE;
		TCTreeNode<E,V> minc = null; 
		
		Iterator<TCTreeNode<E,V>> i = children.iterator();
		while (i.hasNext()) {
			TCTreeNode<E,V> c = i.next();
			if (c.equals(child)) continue;
			
			int nv = this.getFragment(c).countVertices();
			if (nv < min)
			{
				min = nv;
				minc = c;
			}
		}
		
		if (minc!=null) {
			result = this.getFragment(minc);
			
			Iterator<E> j = child.getSkeleton().getEdges().iterator();
			while (j.hasNext()) {
				E e = j.next();
				result.addEdge(e.getV1(), e.getV2());
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return toStringHelper(this.getRoot(), 0);
	}
	
	private String toStringHelper(TCTreeNode<E,V> tn, int depth) {
		String result = "";
		for (int i = 0; i < depth; i++){
			result += "   ";
		}
		result += tn.toString();
		result += "\n";
		for (TCTreeNode<E,V> c: this.getChildren(tn)){
			result += toStringHelper(c, depth+1);
		}
		return result;
	}
}
