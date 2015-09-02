package de.hpi.bpt.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hpi.bpt.graph.abs.AbstractDirectedGraph;

/**
 * Basic process model implementation
 * 
 * @author Artem Polyvyanyy
 */
public class Process extends AbstractDirectedGraph<ControlFlow, Node> {
	private String name;
	
	/**
	 * Construct an empty process
	 */
	public Process() {
		this.name = "";
	}
	
	/**
	 * Construct an empty process with name
	 */
	public Process(String name) {
		this.name = name;
	}

	/**
	 * Create a control flow
	 * @param from Source node
	 * @param to Target node
	 * @return The fresh control flow, or <code>null</code> if control flow between source and target already exists
	 */
	public ControlFlow addControlFlow(Node from, Node to) {
		if (from == null || to == null) return null;
		
		Collection<Node> ss = new ArrayList<Node>(); ss.add(from);
		Collection<Node> ts = new ArrayList<Node>(); ts.add(to);
		
		if (!this.checkEdge(ss, ts)) return null;
		
		return new ControlFlow(this, from, to);
	}
	
	/**
	 * Remove control flow from the process
	 * @param flow Control flow to remove
	 * @return Control flow that was removed from the process, or <code>null</code> if control flow was not removed
	 */
	public ControlFlow removeControlFlow(ControlFlow flow) {
		return this.removeEdge(flow)!=null ? flow : null;
	}
	
	/**
	 * Remove control flow from the process
	 * @param flows A collection of control flows to be removed
	 * @return Control flows that were removed from the process, <code>null</code> if no control flow was removed
	 */
	public Collection<ControlFlow> removeControlFlows(Collection<ControlFlow> flows) {
		return this.removeEdges(flows);
	}
	
	/**
	 * Add task to the process
	 * @param task Task to add
	 * @return Task that was added to the process, <code>null</code> upon failure
	 */
	public Task addTask(Task task) {
		return this.addVertex(task)!=null ? task : null;
	}
	
	/**
	 * Remove task from the process
	 * @param task Task to remove
	 * @return Task that was removed from the process, <code>null</code> upon failure
	 */
	public Task removeTask(Task task) {
		return this.removeVertex(task)!=null ? task : null;
	}
	
	/**
	 * Add gateway to the process
	 * @param gateway Gateway to add
	 * @return Gateway that was added to the process, <code>null</code> upon failure
	 */
	public Gateway addGateway(Gateway gateway) {
		return this.addVertex(gateway)!=null ? gateway : null;
	}
	
	/**
	 * Remove gateway from the process
	 * @param task Gateway to remove
	 * @return Gateway that was removed from the process, <code>null</code> upon failure
	 */
	public Gateway removeGateway(Gateway gateway) {
		return this.removeVertex(gateway)!=null ? gateway : null;
	}
	
	/**
	 * Get tasks of the process
	 * @return A collection of process tasks
	 */
	public Collection<Task> getTasks() {
		Collection<Task> result = new ArrayList<Task>();
		
		Collection<Node> nodes = this.getVertices();
		Iterator<Node> i = nodes.iterator();
		while (i.hasNext()) {
			Node obj = i.next();
			if (obj instanceof Task)
				result.add((Task)obj);
		}
		
		return result;
	}
	
	private Collection<Gateway> getGateways(Collection<GatewayType> types) {
		Collection<Gateway> result = new ArrayList<Gateway>();
		
		Collection<Node> nodes = this.getVertices();
		Iterator<Node> i = nodes.iterator();
		while (i.hasNext()) {
			Node obj = i.next();
			if (obj instanceof Gateway) {
				Gateway g = (Gateway) obj;
				if (types.contains(g.getGatewayType()))
					result.add(g);
			}		
		}
		
		return result;
	}
	
	/**
	 * Get gateways of the process
	 * @return A collection of process gateways
	 */
	public Collection<Gateway> getGateways() {
		Collection<GatewayType> types = new ArrayList<GatewayType>();
		types.add(GatewayType.AND);
		types.add(GatewayType.XOR);
		types.add(GatewayType.OR);
		types.add(GatewayType.UNDEFINED);
		return this.getGateways(types);
	}
	
	/**
	 * Get gateways of the process of certain type 
	 * @param type Gateway type
	 * @return A collection of process gateways of the type specified
	 */
	public Collection<Gateway> getGateways(GatewayType type) {
		Collection<GatewayType> types = new ArrayList<GatewayType>();
		types.add(type);
		return this.getGateways(types);
	}
	
	/**
	 * Get process nodes
	 * @return A collection of process nodes
	 */
	public Collection<Node> getNodes() {
		return this.getVertices();
	}
	
	/**
	 * Get control flow of the process
	 * @return A collection of process flows of the process
	 */
	public Collection<ControlFlow> getControlFlow() {
		return this.getEdges();
	}
	
	/**
	 * Get process name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set process name
	 * @param name Process name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
