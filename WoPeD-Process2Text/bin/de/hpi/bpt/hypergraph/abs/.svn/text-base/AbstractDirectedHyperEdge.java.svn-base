package de.hpi.bpt.hypergraph.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract directed hyper edge implementation
 * 
 * @author Artem Polyvyanyy
 *
 * @param <V> Vertex type employed in the edge
 */
public class AbstractDirectedHyperEdge<V extends IVertex> extends AbstractHyperEdge<V> implements IDirectedHyperEdge<V>
{	
	protected Collection<V> source;
	protected Collection<V> target;
	
	@SuppressWarnings("unchecked")
	private AbstractMultiDirectedHyperGraph graph = null;

	@SuppressWarnings("unchecked")
	public AbstractDirectedHyperEdge(AbstractMultiDirectedHyperGraph g) {
		super(g);
		this.source = new ArrayList<V>();
		this.target = new ArrayList<V>();
		this.graph = g;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#addSourceVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public V addSourceVertex(V v) {
		if (this.graph == null) return null;
		if (v==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vs = new ArrayList<V>(this.source); vs.add(v);
			
			if (!this.checkEdge(vs, this.target)) return null;
		}
		
		super.addVertex(v);
		result = this.source.add(v);
		this.graph.addIndex(this, v);
		
		return result ? v : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#addSourceVertices(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> addSourceVertices(Collection<V> vs) {
		if (this.graph == null) return null;
		if (vs==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vvs = new ArrayList<V>(this.source); vvs.addAll(vs);
			
			if (!this.checkEdge(vvs, this.target)) return null;
		}
		
		super.addVertices(vs);
		result = this.source.addAll(vs);
		this.graph.addIndex(this, vs);
		
		return result ? vs : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#addTargetVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public V addTargetVertex(V v) {
		if (this.graph == null) return null;
		if (v==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vs = new ArrayList<V>(this.target); vs.add(v);
			
			if (!this.checkEdge(this.source, vs)) return null;
		}
		
		super.addVertex(v);
		result = this.target.add(v);
		this.graph.addIndex(this, v);
		
		return result ? v : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#addTargetVertices(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> addTargetVertices(Collection<V> vs) {
		if (this.graph == null) return null;
		if (vs==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vvs = new ArrayList<V>(this.target); vvs.addAll(vs);
			
			if (!this.checkEdge(this.source, vvs)) return null;
		}
		
		super.addVertices(vs);
		result = this.target.addAll(vs);
		this.graph.addIndex(this, vs);
		
		return result ? vs : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#addSourceAndTagetVertices(java.util.Collection, java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> addSourceAndTagetVertices(Collection<V> ss, Collection<V> ts) {
		if (this.graph == null) return null;
		if (ss==null && ts==null) return null;
		if (ss.size()==0 && ts.size()==0) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> nss = new ArrayList<V>(this.source);
			Collection<V> nts = new ArrayList<V>(this.target);
			
			if (ss!=null && ss.size()!=0) nss.addAll(ss);
			if (ts!=null && ts.size()!=0) nts.addAll(ts);
			
			if (!this.checkEdge(nss, nts))
				return null;
		}
		
		Collection<V> vresult = new ArrayList<V>();
		if (ss!=null && ss.size()!=0) {
			super.addVertices(ss);
			result = this.source.addAll(ss);
			this.graph.addIndex(this, ss);
			vresult.addAll(ss);
		}
		
		if (ts!=null && ts.size()!=0) {
			super.addVertices(ts);
			result = this.target.addAll(ts);
			this.graph.addIndex(this, ts);
			vresult.addAll(ts);
		}
		
		return result ? vresult : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#getOtherSourceVertices(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getOtherSourceVertices(V v) {
		Collection<V> res = new ArrayList<V>(this.source);
		res.remove(v);
		
		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#getOtherSourceVertices(java.util.Collection)
	 */
	public Collection<V> getOtherSourceVertices(Collection<V> vs) {
		Collection<V> res = new ArrayList<V>(this.source);
		res.removeAll(vs);
		
		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#getOtherTargetVertices(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public Collection<V> getOtherTargetVertices(V v) {
		Collection<V> res = new ArrayList<V>(this.target);
		res.remove(v);
		
		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#getOtherTargetVertices(java.util.Collection)
	 */
	public Collection<V> getOtherTargetVertices(Collection<V> vs) {
		Collection<V> res = new ArrayList<V>(this.target);
		res.removeAll(vs);
		
		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#removeSourceVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public V removeSourceVertex(V v) {
		if (this.graph == null) return null;
		if (v==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vs = new ArrayList<V>(this.source); vs.remove(v);
			
			if (!this.checkEdge(vs, this.target)) return null;
		}
		
		super.removeVertex(v);
		result = this.source.remove(v);
		this.graph.removeIndex(this, v);
		
		return result ? v : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#removeSourceVertices(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> removeSourceVertices(Collection<V> vs) {
		if (this.graph == null) return null;
		if (vs==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vvs = new ArrayList<V>(this.source); vvs.removeAll(vs);
			
			if (!this.checkEdge(vvs, this.target)) return null;
		}
		
		super.removeVertices(vs);
		result = this.source.removeAll(vs);
		this.graph.removeIndex(this, vs);
		
		return result ? vs : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#removeTargetVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@SuppressWarnings("unchecked")
	public V removeTargetVertex(V v) {
		if (this.graph == null) return null;
		if (v==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vs = new ArrayList<V>(this.target); vs.remove(v);
			
			if (!this.checkEdge(this.source, vs)) return null;
		}
		
		super.removeVertex(v);
		result = this.target.remove(v);
		this.graph.removeIndex(this, v);
		
		return result ? v : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#removeTargetVertices(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> removeTargetVertices(Collection<V> vs) {
		if (this.graph == null) return null;
		if (vs==null) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> vvs = new ArrayList<V>(this.target); vvs.removeAll(vs);
			
			if (!this.checkEdge(this.source, vvs)) return null;
		}
		
		super.removeVertices(vs);
		result = this.target.removeAll(vs);
		this.graph.removeIndex(this, vs);
		
		return result ? vs : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#removeSourceAndTagetVertices(java.util.Collection, java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> removeSourceAndTagetVertices(Collection<V> ss, Collection<V> ts) {
		if (this.graph == null) return null;
		if (ss==null && ts==null) return null;
		if (ss.size()==0 && ts.size()==0) return null;
		boolean result = false;
		
		if (!this.graph.isMultiGraph()) {
			Collection<V> nss = new ArrayList<V>(this.source);
			Collection<V> nts = new ArrayList<V>(this.target);
			
			if (ss!=null && ss.size()!=0) nss.removeAll(ss);
			if (ts!=null && ts.size()!=0) nts.removeAll(ts);
			
			if (!this.checkEdge(nss, nts))
				return null;
		}
		
		Collection<V> vresult = new ArrayList<V>();
		if (ss!=null && ss.size()!=0) {
			super.removeVertices(ss);
			result = this.source.removeAll(ss);
			this.graph.removeIndex(this, ss);
			vresult.addAll(ss);
		}
		
		if (ts!=null && ts.size()!=0) {
			super.removeVertices(ts);
			result = this.target.removeAll(ts);
			this.graph.removeIndex(this, ts);
			vresult.addAll(ts);
		}
		
		return result ? vresult : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#getSourceVertices()
	 */
	public Collection<V> getSourceVertices() {
		return new ArrayList<V>(this.source);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#getTargetVertices()
	 */
	public Collection<V> getTargetVertices() {
		return new ArrayList<V>(this.target);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperEdge#equals(java.lang.Object)
	 */
	/*@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof AbstractDirectedHyperEdge)) return false;
		AbstractDirectedHyperEdge<V> e = (AbstractDirectedHyperEdge<V>) obj;
		
		return this.compareMultiSets(this.source, e.getSourceVertices()) &&
					this.compareMultiSets(this.target, e.getTargetVertices());
	}*/
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperEdge#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		String t = "";
		
		Iterator<V> i = this.source.iterator();
		if (i.hasNext()) s = String.format("%1s",i.next());
		while (i.hasNext())
			s = String.format("%1s,%1s", s, i.next());
		
		i = this.target.iterator();
		if (i.hasNext()) t = String.format("%1s",i.next());
		while (i.hasNext())
			t = String.format("%1s,%1s", t, i.next());
		
		return String.format("[{%1s}->{%1s}]", s, t);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#hasSource()
	 */
	public boolean hasSource() {
		return !this.source.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#hasTarget()
	 */
	public boolean hasTarget() {
		return !this.target.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#hasSource(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public boolean hasSource(V v) {
		return this.source.contains(v);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#hasTarget(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	public boolean hasTarget(V v) {
		return this.target.contains(v);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#hasSources(java.util.Collection)
	 */
	public boolean hasSources(Collection<V> vs) {
		return this.source.containsAll(vs);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.IDirectedHyperEdge#hasTargets(java.util.Collection)
	 */
	public boolean hasTargets(Collection<V> vs) {
		return this.target.containsAll(vs);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperEdge#removeVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public V removeVertex(V v) {
		if (this.graph == null) return null;
		Collection<V> vs = new ArrayList<V>(); vs.add(v);
		boolean result = (this.hasSource(v) || this.hasTarget(v)); 
		this.removeSourceAndTagetVertices(vs, vs);
		
		return result ? v : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperEdge#removeVertices(java.util.Collection)
	 */
	@Override
	public Collection<V> removeVertices(Collection<V> vs) {
		if (this.graph == null) return null;
		return this.removeSourceAndTagetVertices(vs, vs);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperEdge#addVertex(de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public V addVertex(V v) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperEdge#addVertices(java.util.Collection)
	 */
	@Override
	public Collection<V> addVertices(Collection<V> vs) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	protected boolean checkEdge(Collection<V> ss, Collection<V> ts) {
		Collection<IDirectedHyperEdge<V>> es = this.graph.getEdgesWithSourcesAndTargets(ss, ts);
		if (es.size()>0) {
			Iterator<IDirectedHyperEdge<V>> i = es.iterator();
			while (i.hasNext()) {
				IDirectedHyperEdge<V> e = i.next();
				if (e.getSourceVertices().size()==ss.size() && e.getTargetVertices().size()==ts.size())
					return false;
			}
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractHyperEdge#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
		this.graph = null;
	}
}
