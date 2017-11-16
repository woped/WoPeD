package de.hpi.bpt.hypergraph.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract hyper edge implementation
 * 
 * @author Artem Polyvyanyy
 *
 * @param <V> Vertex type employed in the edge
 */
public class AbstractHyperEdge<V extends IVertex> extends GObject implements IHyperEdge<V>
{
	@SuppressWarnings("unchecked")
	private AbstractMultiHyperGraph graph = null;
	
	protected Collection<V> vertices;
	
	@SuppressWarnings("unchecked")
	protected AbstractHyperEdge(AbstractMultiHyperGraph g) {
		super();
		this.vertices = new ArrayList<V>();
		this.graph = g;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#addVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public V addVertex(V v) {
		if (this.graph == null) return null;
		if (v==null) return null;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vs = new ArrayList<V>(this.vertices);
			vs.add(v);
			
			if (!this.checkEdge(vs)) return null;
		}
		
		boolean result = this.vertices.add(v);
		this.graph.addIndex(this, v);
		
		return result ? v : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#addVertices(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> addVertices(Collection<V> vs) {
		if (this.graph == null) return null;
		if (vs==null) return null;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vvs = new ArrayList<V>(this.vertices);
			vvs.addAll(vs);
			
			if (!this.checkEdge(vs)) return null;
		}
		
		boolean result = this.vertices.addAll(vs);
		this.graph.addIndex(this, vs);
		
		return result ? vs : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#connectsVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public boolean connectsVertex(V v) {
		return this.vertices.contains(v);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#connectsVertices(java.util.Collection)
	 */
	public boolean connectsVertices(Collection<V> vs) {
		return this.vertices.containsAll(vs);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#getOtherVertices(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getOtherVertices(V v) {
		Collection<V> result = new ArrayList<V>(this.vertices);
		result.remove(v);
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#getOtherVertices(java.util.Collection)
	 */
	public Collection<V> getOtherVertices(Collection<V> vs) {
		Collection<V> result = new ArrayList<V>(this.vertices);
		result.removeAll(vs);
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#getVertices()
	 */
	public Collection<V> getVertices() {
		return new ArrayList<V>(this.vertices);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#removeVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public V removeVertex(V v) {
		if (this.graph == null) return null;
		if (v==null) return null;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vs = new ArrayList<V>(this.vertices);
			vs.remove(v);
			
			if (!this.checkEdge(vs)) return null;
		}
		
		boolean result = this.vertices.remove(v);
		if (result) this.graph.removeIndex(this, v);
		
		return result ? v : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#removeVertices(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> removeVertices(Collection<V> vs) {
		if (this.graph == null) return null;
		if (vs==null) return null;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vvs = new ArrayList<V>(this.vertices);
			vvs.removeAll(vs);
			
			if (!this.checkEdge(vvs)) return null;
		}
		
		boolean result = this.vertices.remove(vs);
		if (result) this.graph.removeIndex(this, vs);
		
		return result ? vs : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IHyperEdge#destroy()
	 */
	@SuppressWarnings("unchecked")
	public void destroy() {
		this.graph.removeIndex(this, this.vertices);
		this.graph.addVertices(this.vertices);
		this.graph = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.GObject#equals(java.lang.Object)
	 */
	/*@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof AbstractHyperEdge)) return false;
		
		return this.compareMultiSets(this.getVertices(), ((AbstractHyperEdge<V>) obj).getVertices());
	}*/
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.GObject#toString()
	 */
	@Override
	public String toString() {
		String res = "";
		
		Iterator<V> i = this.vertices.iterator();
		if (i.hasNext()) res = String.format("%1s",i.next());
		while (i.hasNext())
			res = String.format("%1s,%1s", res, i.next());
		
		return String.format("[{%1s}]", res);
	}
	
	protected boolean compareMultiSets(Collection<V> c1, Collection<V> c2) {
		if (c1 == c2) return true;
		if (c1.size() != c2.size()) return false;
		
		Iterator<V> i = c1.iterator();
		Collection<V> c3 = new ArrayList<V>(c2);
		
		while (i.hasNext())
		{
			V v = i.next();
			if (c3.contains(v)) c3.remove(v);
			else return false;
		}
		
		if (c3.size()==0) return true;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkEdge(Collection<V> vs) {
		Collection<IHyperEdge<V>> es = this.graph.getEdges(vs);
		if (es.size()>0) {
			Iterator<IHyperEdge<V>> i = es.iterator();
			while (i.hasNext()) {
				IHyperEdge<V> e = i.next();
				if (e.getVertices().size()==vs.size())
					return false;
			}
		}
		
		return true;
	}
}
