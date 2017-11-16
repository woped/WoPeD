package de.hpi.bpt.process.epc;

import java.util.Collection;

import de.hpi.bpt.graph.abs.IDirectedGraph;

/**
 * A business process captured in the event-driven process chain (EPC) notation.
 * 
 * @author Artem Polyvyanyy
 */
public interface IEPC<CF extends IControlFlow<FO>, 
						FO extends IFlowObject,
						E extends IEvent,
						F extends IFunction,
						C extends IConnector,
						P extends IProcessInterface,
						X extends IConnection<N>,
						N extends INode,
						NFO extends INonFlowObject> extends IDirectedGraph<CF,FO> {
	
	/**
	 * Add flow object to this EPC
	 * @param obj FlowObject to add
	 * @return Object that was added, <code>null</code> upon failure
	 */
	FO addFlowObject(FO obj);
	
	/**
	 * Add node, a flow object or a non flow object, to this EPC
	 * @param obj Node to add
	 * @return Object that was added, <code>null</code> upon failure
	 */
	N addNode(N obj);
	
	/**
	 * Returns all the flow objects of this EPC
	 * The flow objects are: events, functions, connectors, and process interfaces
	 *  
	 * @return Collection of flow objects of this EPC
	 */
	Collection<FO> getFlowObjects();
	
	/**
	 * Remove flow object
	 * @param obj Object to remove
	 * @return Object removed, <code>null</code> upon failure
	 */
	FO removeFlowObject(FO obj);
	
	/**
	 * Get all the events of this EPC
	 * 
	 * @return Collection of EPC events
	 */
	Collection<E> getEvents();
	
	/**
	 * Get all the functions of this EPC
	 * 
	 * @return Collection of EPC functions
	 */
	Collection<F> getFunctions();
	
	/**
	 * Get all the connectors of this EPC
	 * 
	 * @return collection of EPC connectors
	 */
	Collection<C> getConnectors();
	
	/**
	 * Get all the process interfaces of this EPC
	 * 
	 * @return Collection of EPC process interfaces
	 */
	Collection<P> getProcessInterfaces();
	
	/**
	 * Check if connector is split, has one incoming and multiple outgoing control flow edges 
	 * @param c Connector
	 * @return <code>true</code> if connector is a split connector, <code>false</code> otherwise
	 */
	boolean isSplit(C c);
	
	/**
	 * Check if connector is join, has one outgoing and multiple incoming control flow edges
	 * @param c Connector
	 * @return <code>true</code> if connector is a join connector, <code>false</code> otherwise
	 */
	boolean isJoin(C c);
	
	/**
	 * Add control flow to this EPC
	 * @param from Flow object from
	 * @param to Flow object to
	 * @return Control flow object added to the EPC, <code>null</code> upon failure 
	 */
	CF addControlFlow(FO from, FO to);
	

	CF addControlFlow(FO from, FO to, float probability);

	/**
	 * Remove control flow from this EPC
	 * @param controlFlow Control flow to remove
	 * @return Control flow object removed from the EPC, <code>null</code> upon failure 
	 */
	CF removeControlFlow(CF controlFlow);
	
	/**
	 * Remove control flow from this EPC
	 * @param controlFlow Control flow to remove
	 * @return Collection of control flow edges removed, <code>null</code> is no edge was removed
	 */
	Collection<CF> removeControlFlow(Collection<CF> controlFlow);
	
	/**
	 * Get the connections which are part of the control flow of this EPC
	 * 
	 * @return Collection of control flow edges
	 */
	Collection<CF> getControlFlow();
	
	/**
	 * Get flow object incoming control flow
	 * @param obj Flow object
	 * @return Collection of control flow elements incoming to the flow object
	 */
	Collection<CF> getIncomingControlFlow(FO obj);
	
	/**
	 * Get flow object outgoing control flow
	 * @param obj Flow object
	 * @return Collection of control flow elements outgoing from the flow object
	 */
	Collection<CF> getOutgoingControlFlow(FO obj);
	
	/**
	 * Add a non flow object to this EPC
	 * @param obj Object to add
	 * @return Object added to the EPC, <code>null</code> upon failure
	 */
	NFO addNonFlowObject(NFO obj);
	
	/**
	 * Remove a non flow object from this EPC
	 * @param obj Object to remove
	 * @return Object removed from the EPC, <code>null</code> upon failure
	 */
	NFO removeNonFlowObject(NFO obj);
	
	/**
	 * Get all non flow objects
	 * @return Collection of all non flow objects of this EPC
	 */
	Collection<NFO> getNonFlowObjects();
	
	/**
	 * Get all non flow objects of certain type
	 * @param type Type of non flow objects (system, document, etc. ...)
	 * @return Collection of all non flow objects of this EPC of a certain type
	 */
	Collection<NFO> getNonFlowObjects(NonFlowObjectType type);
	
	/**
	 * Get all non flow objects attached to a flow object
	 * @param obj Flow object
	 * @return Collection of all non flow objects of this EPC attached to a flow object
	 */
	Collection<NFO> getNonFlowObjects(FO obj);
	
	/**
	 * Get all input non flow objects attached to a flow object
	 * @param obj Flow object
	 * @return Collection of non flow objects that are input to a given flow object
	 */
	Collection<NFO> getInputNonFlowObjects(FO obj);
	
	/**
	 * Get all output non flow objects attached to a flow object
	 * @param obj Flow object
	 * @return Collection of non flow objects that are output to a given flow object
	 */
	Collection<NFO> getOutputNonFlowObjects(FO obj);
	
	/**
	 * Get all flow objects attached to a non flow object
	 * @param obj Non flow object
	 * @return Collection of all flow objects of this EPC attached to a non flow object
	 */
	Collection<FO> getFlowObjects(NFO obj);
	
	/**
	 * Get all input flow objects attached to a non flow object
	 * @param obj Non flow object
	 * @return Collection of flow objects that are input to a given non flow object
	 */
	Collection<FO> getInputFlowObjects(NFO obj);
	
	/**
	 * Get all output flow objects attached to a non flow object
	 * @param obj Non flow object
	 * @return Collection of flow objects that are output to a given non flow object
	 */
	Collection<FO> getOutputFlowObjects(NFO obj);
	
	/**
	 * Filter out flow objects of a certain type
	 * @param objs Collection of flow objects
	 * @param type Flow object type
	 * @return Sub-collection of flow objects of certain type  
	 */
	Collection<FO> filter(Collection<FO> objs, FlowObjectType type);
	
	/**
	 * Filter out non flow objects of a certain type
	 * @param objs Collection of flow objects
	 * @param type Flow object type
	 * @return Sub-collection of flow objects of certain type  
	 */
	Collection<NFO> filter(Collection<NFO> objs, NonFlowObjectType type);
	
	/**
	 * Add a non flow object to this EPC and connect it with a flow object
	 * @param obj Object to add
	 * @param to Flow object to route to
	 * @return Connection object between two nodes, <code>null</code> upon failure
	 */
	X connectNonFlowObject(NFO obj, FO to);
	
	/**
	 * Add a non flow object to this EPC and connect it with a flow object
	 * @param from Flow object to route from
	 * @param obj Object to add
	 * @return Connection object between two nodes, <code>null</code> upon failure
	 */
	X connectNonFlowObject(FO from, NFO obj);
	
	/**
	 * Remove connection object from the EPC
	 * @param cxn Connection
	 * @return Connection object that was removed, <code>null</code> upon failure
	 */
	X disconnectNonFlowObject(X cxn);

	/**
	 * Get EPC entry flow objects
	 * @return Collection of EPC entry objects
	 */
	Collection<FO> getEntries();
	
	/**
	 * Get EPC exit flow objects
	 * @return Collection of EPC exit objects
	 */
	Collection<FO> getExits();
}