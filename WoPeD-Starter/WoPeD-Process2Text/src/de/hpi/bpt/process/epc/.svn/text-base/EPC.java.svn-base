package de.hpi.bpt.process.epc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.hpi.bpt.graph.abs.AbstractDirectedGraph;
import de.hpi.bpt.graph.algo.DirectedGraphAlgorithms;

/**
 * EPC implementation
 * 
 * @author Artem Polyvyanyy
 */
public class EPC extends AbstractDirectedGraph<ControlFlow,FlowObject> implements IEPC<ControlFlow, 
				FlowObject,Event,Function,Connector,ProcessInterface,Connection,Node,NonFlowObject>
{
	private DirectedGraphAlgorithms<ControlFlow, FlowObject> directedGraphAlgorithms = new DirectedGraphAlgorithms<ControlFlow, FlowObject>();
	
	protected Set<NonFlowObject> meta = new HashSet<NonFlowObject>();
	protected Map<NonFlowObject,Set<Connection>> nf = new Hashtable<NonFlowObject, Set<Connection>>();
	protected Map<FlowObject,Set<Connection>> fn = new Hashtable<FlowObject, Set<Connection>>();

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#addControlFlow(de.hpi.bpt.process.epc.FlowObject, de.hpi.bpt.process.epc.FlowObject)
	 */
	public ControlFlow addControlFlow(FlowObject from, FlowObject to) {
		return addControlFlow(from, to, 1f);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#addControlFlow(de.hpi.bpt.process.epc.FlowObject, de.hpi.bpt.process.epc.FlowObject)
	 */
	public ControlFlow addControlFlow(FlowObject from, FlowObject to, float probability) {
		if (from == null || to == null) return null;
		
		Collection<FlowObject> ss = new ArrayList<FlowObject>(); ss.add(from);
		Collection<FlowObject> ts = new ArrayList<FlowObject>(); ts.add(to);
		
		if (!this.checkEdge(ss, ts)) return null;
		
		return new ControlFlow(this, from, to, probability);
	}
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#addFlowObject(de.hpi.bpt.process.epc.FlowObject)
	 */
	public FlowObject addFlowObject(FlowObject obj) {
		return super.addVertex(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#addNode(de.hpi.bpt.process.epc.Node)
	 */
	public Node addNode(Node obj) {
		if (obj instanceof FlowObject) {
			return (Node) this.addFlowObject((FlowObject)obj);
		} else if (obj instanceof NonFlowObject) {
			return (Node) this.addNonFlowObject((NonFlowObject)obj);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#addNonFlowObject(de.hpi.bpt.process.epc.NonFlowObject)
	 */
	public NonFlowObject addNonFlowObject(NonFlowObject obj) {
		return (this.meta.add(obj)) ? obj : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#connectNonFlowObject(de.hpi.bpt.process.epc.NonFlowObject, de.hpi.bpt.process.epc.FlowObject)
	 */
	public Connection connectNonFlowObject(NonFlowObject obj,FlowObject to) {
		boolean result = false;
		this.addFlowObject(to);
		this.addNonFlowObject(obj);
		
		Connection cxn = new Connection(obj,to);
		Set<Connection> h1 = new HashSet<Connection>(); h1.add(cxn);
		if (this.nf.get(obj) == null) { this.nf.put(obj, h1); result = true; } 
		else result |= this.nf.get(obj).add(cxn);
		Set<Connection> h2 = new HashSet<Connection>(); h2.add(cxn);
		if (this.fn.get(to) == null) { this.fn.put(to, h2); result = true; } 
		else result |= this.fn.get(to).add(cxn);
		
		return result ? cxn : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#connectNonFlowObject(de.hpi.bpt.process.epc.FlowObject, de.hpi.bpt.process.epc.NonFlowObject)
	 */
	public Connection connectNonFlowObject(FlowObject from, NonFlowObject obj) {
		boolean result = false;
		this.addFlowObject(from);
		this.addNonFlowObject(obj);
		
		Connection cxn = new Connection(from,obj);
		Set<Connection> h1 = new HashSet<Connection>(); h1.add(cxn);
		if (this.nf.get(obj) == null) { this.nf.put(obj, h1); result = true; } 
		else result |= this.nf.get(obj).add(cxn);
		Set<Connection> h2 = new HashSet<Connection>(); h2.add(cxn);
		if (this.fn.get(from) == null) { this.fn.put(from, h2); result = true; } 
		else result |= this.fn.get(from).add(cxn);
		
		return result ? cxn : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#disconnectNonFlowObject(de.hpi.bpt.process.epc.Connection)
	 */
	public Connection disconnectNonFlowObject(Connection cxn) {
		Node from = cxn.getSource();
		Node to = cxn.getTarget();
		
		boolean result = false;
		if (from instanceof NonFlowObject) {
			Node swap = from; from = to; to = swap;
		}
		
		if (this.nf.get(from) == null && this.fn.get(to) == null) return null;
		
		Iterator<Connection> i = this.nf.get(from).iterator();
		while (i.hasNext()) {
			Connection icxn = i.next();
			if (icxn.hasSource(from) && icxn.hasTarget(to)) {
				result |= this.nf.get(from).remove(icxn);
				if (this.nf.get(from).size()==0) this.nf.remove(from);
				break;
			}
		}
		
		i = this.fn.get(to).iterator();
		while (i.hasNext()) {
			Connection icxn = i.next();
			if (icxn.hasSource(from) && icxn.hasTarget(to)) {
				result |= this.fn.get(to).remove(icxn);
				if (this.fn.get(to).size()==0) this.fn.remove(to);
				break;
			}
		}
		
		return result ? cxn : null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#filter(java.util.Collection, de.hpi.bpt.process.epc.FlowObjectType)
	 */
	public Collection<FlowObject> filter(Collection<FlowObject> objs, FlowObjectType type) {
		Collection<FlowObject> result = new ArrayList<FlowObject>();
		
		Iterator<FlowObject> i = objs.iterator();
		while (i.hasNext()) {
			FlowObject obj = i.next();
			if (obj.getType()==type)
				result.add(obj);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#filter(java.util.Collection, de.hpi.bpt.process.epc.NonFlowObjectType)
	 */
	public Collection<NonFlowObject> filter(Collection<NonFlowObject> objs, NonFlowObjectType type) {
		Collection<NonFlowObject> result = new ArrayList<NonFlowObject>();
		
		Iterator<NonFlowObject> i = objs.iterator();
		while (i.hasNext()) {
			NonFlowObject obj = i.next();
			if (obj.getType()==type)
				result.add(obj);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getConnectors()
	 */
	public Collection<Connector> getConnectors() {
		Collection<Connector> result = new ArrayList<Connector>();
		
		Collection<FlowObject> flowObjs = this.getFlowObjects();
		Iterator<FlowObject> i = flowObjs.iterator();
		while (i.hasNext()) {
			FlowObject obj = i.next();
			if (obj instanceof Connector)
				result.add((Connector)obj);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getControlFlow()
	 */
	public Collection<ControlFlow> getControlFlow() {
		return super.getEdges();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getEntries()
	 */
	public Collection<FlowObject> getEntries() {
		return directedGraphAlgorithms.getInputVertices(this);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getEvents()
	 */
	public Collection<Event> getEvents() {
		Collection<Event> result = new ArrayList<Event>();
		
		Collection<FlowObject> flowObjs = this.getFlowObjects();
		Iterator<FlowObject> i = flowObjs.iterator();
		while (i.hasNext()) {
			FlowObject obj = i.next();
			if (obj instanceof Event)
				result.add((Event)obj);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getExits()
	 */
	public Collection<FlowObject> getExits() {
		return directedGraphAlgorithms.getOutputVertices(this);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getFlowObjects()
	 */
	public Collection<FlowObject> getFlowObjects() {
		return super.getVertices();
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getFlowObjects(de.hpi.bpt.process.epc.NonFlowObject)
	 */
	public Collection<FlowObject> getFlowObjects(NonFlowObject obj) {
		Set<FlowObject> result = new HashSet<FlowObject>();
		result.addAll(this.getInputFlowObjects(obj));
		result.addAll(this.getOutputFlowObjects(obj));
		return new ArrayList<FlowObject>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getFunctions()
	 */
	public Collection<Function> getFunctions() {
		Collection<Function> result = new ArrayList<Function>();
		
		Collection<FlowObject> flowObjs = this.getFlowObjects();
		Iterator<FlowObject> i = flowObjs.iterator();
		while (i.hasNext()) {
			FlowObject obj = i.next();
			if (obj instanceof Function)
				result.add((Function)obj);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getIncomingControlFlow(de.hpi.bpt.process.epc.FlowObject)
	 */
	public Collection<ControlFlow> getIncomingControlFlow(FlowObject obj) {
		return super.getIncomingEdges(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getInputFlowObjects(de.hpi.bpt.process.epc.NonFlowObject)
	 */
	public Collection<FlowObject> getInputFlowObjects(NonFlowObject obj) {
		Set<FlowObject> result = new HashSet<FlowObject>();
		
		if (this.nf.get(obj) != null) {
			Iterator<Connection> i = this.nf.get(obj).iterator();
			while (i.hasNext())
			{
				Connection cxn = i.next();
				if (cxn.getSource() instanceof FlowObject) result.add((FlowObject)cxn.getSource());
			}
		}
		
		return new ArrayList<FlowObject>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getInputNonFlowObjects(de.hpi.bpt.process.epc.FlowObject)
	 */
	public Collection<NonFlowObject> getInputNonFlowObjects(FlowObject obj) {
		Set<NonFlowObject> result = new HashSet<NonFlowObject>();
		
		if (this.fn.get(obj) != null) {
			Iterator<Connection> i = this.fn.get(obj).iterator();
			while (i.hasNext())
			{
				Connection cxn = i.next();
				if (cxn.getSource() instanceof NonFlowObject) result.add((NonFlowObject)cxn.getSource());
			}
		}
		
		return new ArrayList<NonFlowObject>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getNonFlowObjects()
	 */
	public Collection<NonFlowObject> getNonFlowObjects() {
		return new ArrayList<NonFlowObject>(this.meta);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getNonFlowObjects(de.hpi.bpt.process.epc.NonFlowObjectType)
	 */
	public Collection<NonFlowObject> getNonFlowObjects(NonFlowObjectType type) {
		return this.filter(this.getNonFlowObjects(), type);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getNonFlowObjects(de.hpi.bpt.process.epc.FlowObject)
	 */
	public Collection<NonFlowObject> getNonFlowObjects(FlowObject obj) {
		Set<NonFlowObject> result = new HashSet<NonFlowObject>();
		result.addAll(this.getInputNonFlowObjects(obj));
		result.addAll(this.getOutputNonFlowObjects(obj));
		return new ArrayList<NonFlowObject>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getOutgoingControlFlow(de.hpi.bpt.process.epc.FlowObject)
	 */
	public Collection<ControlFlow> getOutgoingControlFlow(FlowObject obj) {
		return this.getOutgoingEdges(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getOutputFlowObjects(de.hpi.bpt.process.epc.NonFlowObject)
	 */
	public Collection<FlowObject> getOutputFlowObjects(NonFlowObject obj) {
		Set<FlowObject> result = new HashSet<FlowObject>();
		
		if (this.nf.get(obj) != null) {
			Iterator<Connection> i = this.nf.get(obj).iterator();
			while (i.hasNext())
			{
				Connection cxn = i.next();
				if (cxn.getTarget() instanceof FlowObject) result.add((FlowObject)cxn.getTarget());
			}
		}
		
		return new ArrayList<FlowObject>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getOutputNonFlowObjects(de.hpi.bpt.process.epc.FlowObject)
	 */
	public Collection<NonFlowObject> getOutputNonFlowObjects(FlowObject obj) {
		Set<NonFlowObject> result = new HashSet<NonFlowObject>();
		
		if (this.fn.get(obj) != null) {
			Iterator<Connection> i = this.fn.get(obj).iterator();
			while (i.hasNext())
			{
				Connection cxn = i.next();
				if (cxn.getTarget() instanceof NonFlowObject) result.add((NonFlowObject)cxn.getTarget());
			}
		}
		
		return new ArrayList<NonFlowObject>(result);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#getProcessInterfaces()
	 */
	public Collection<ProcessInterface> getProcessInterfaces() {
		Collection<ProcessInterface> result = new ArrayList<ProcessInterface>();
		
		Collection<FlowObject> flowObjs = this.getFlowObjects();
		Iterator<FlowObject> i = flowObjs.iterator();
		while (i.hasNext()) {
			FlowObject obj = i.next();
			if (obj instanceof ProcessInterface)
				result.add((ProcessInterface)obj);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#isJoin(de.hpi.bpt.process.epc.Connector)
	 */
	public boolean isJoin(Connector c) {
		if (this.getIncomingControlFlow(c).size()>1 && this.getOutgoingControlFlow(c).size()==1)
			return true;
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#isSplit(de.hpi.bpt.process.epc.Connector)
	 */
	public boolean isSplit(Connector c) {
		if (this.getIncomingControlFlow(c).size()==1 && this.getOutgoingControlFlow(c).size()>1)
			return true;
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#removeControlFlow(de.hpi.bpt.process.epc.ControlFlow)
	 */
	public ControlFlow removeControlFlow(ControlFlow controlFlow) {
		return super.removeEdge(controlFlow);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#removeControlFlow(java.util.Collection)
	 */
	public Collection<ControlFlow> removeControlFlow(Collection<ControlFlow> controlFlow) {
		return super.removeEdges(controlFlow);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#removeFlowObject(de.hpi.bpt.process.epc.FlowObject)
	 */
	public FlowObject removeFlowObject(FlowObject obj) {
		return super.removeVertex(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.IEPC#removeNonFlowObject(de.hpi.bpt.process.epc.NonFlowObject)
	 */
	public NonFlowObject removeNonFlowObject(NonFlowObject obj) {
		if (this.nf.get(obj) != null) {
			Iterator<Connection> i = this.nf.get(obj).iterator();
			while (i.hasNext()) {
				Connection cxn = i.next();
				if (cxn.getSource() instanceof FlowObject) this.fn.get(cxn.getSource()).remove(cxn);
				if (cxn.getTarget() instanceof FlowObject) this.fn.get(cxn.getTarget()).remove(cxn);
			}	
		}
		
		this.nf.remove(obj);
		return this.meta.remove(obj) ? obj : null;
	}
}

