package de.hpi.bpt.process.epc.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.bpt.process.epc.Connection;
import de.hpi.bpt.process.epc.Connector;
import de.hpi.bpt.process.epc.ControlFlow;
import de.hpi.bpt.process.epc.EPC;
import de.hpi.bpt.process.epc.Event;
import de.hpi.bpt.process.epc.FlowObject;
import de.hpi.bpt.process.epc.Function;
import de.hpi.bpt.process.epc.IEPC;
import de.hpi.bpt.process.epc.Node;
import de.hpi.bpt.process.epc.NonFlowObject;
import de.hpi.bpt.process.epc.ProcessInterface;

/**
 * It might be the case that one EPC model actually contains two EPC processes
 * that are not connected. The EPCSplitter class checks whether this is the case
 * and splits the model into multiple models if necessary.
 * 
 * @author gero.decker, matthias.weidlich
 *
 */
public class EPCSplitter {

	protected IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject> model;
	protected List<Set<FlowObject>> nodeSets = null;

	public EPCSplitter(IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject> model) {
		this.model = model;
	}
	
	/**
	 * Decide whether the EPC model actually contains more than one EPC process
	 * and therefore needs to be split up.
	 * 
	 * @return true, if the model contains more than one process
	 */
	public boolean needsSplitting() {
		Set<FlowObject> nodes = new HashSet<FlowObject>();
	
		FlowObject n = (FlowObject) model.getFlowObjects().toArray()[0];
		nodes.add(n);
		checkIsConnected(nodes, n);
		
		if (nodes.size() == model.getFlowObjects().size()) {
			return false;
		} else {
			nodeSets = new ArrayList<Set<FlowObject>>();
			nodeSets.add(nodes);
			int count = nodes.size();
			while (count < model.getFlowObjects().size()) {
				nodes = new HashSet<FlowObject>();
				FlowObject n2 = getNextNode();
				nodes.add(n2);
				checkIsConnected(nodes, n2);
				count += nodes.size();
				nodeSets.add(nodes);
			}
			return true;
		}
	}

	protected FlowObject getNextNode() {
		for (FlowObject n: model.getFlowObjects()) {
			boolean found = false;
			for (Set<FlowObject> nodes: nodeSets)
				if (nodes.contains(n)) {
					found = true;
					break;
				}
			if (!found)
				return n;
		}
		return null;
	}

	/**
	 * Checks whether a set of nodes is connected to a given flow object.
	 * 
	 * @param nodes
	 * @param n
	 */
	protected void checkIsConnected(Set<FlowObject> nodes, FlowObject n) {
		for (ControlFlow e: model.getControlFlow()) {
			if (e.getTarget().equals(n)) {
				FlowObject n2 = e.getSource();
				if (!nodes.contains(n2)) {
					nodes.add(n2);
					checkIsConnected(nodes, n2);
				}
			}
		}
		for (ControlFlow e: model.getControlFlow()) {
			if (e.getSource().equals(n)) {
				FlowObject n2 = e.getTarget();
				if (!nodes.contains(n2)) {
					nodes.add(n2);
					checkIsConnected(nodes, n2);
				}
			}
		}
	}

	/**
	 * Splits up an EPC model into multiple EPC models, each containing exactly one
	 * EPC process.
	 * 
	 * @return a list of EPC models
	 */
	public List<IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject>> splitModel() {
		List<IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject>> models = new ArrayList<IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject>>(nodeSets.size());
		int i=1;
		for (Set<FlowObject> nodes: nodeSets) {
			IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject> newm = new EPC();
			newm.setId(model.getId());
			newm.setName(model.getName()+"_"+i);
			models.add(newm);
			
			for (FlowObject n: nodes)
				newm.addFlowObject(n);
			for (ControlFlow e: model.getControlFlow())
				if (nodes.contains(e.getSource())) {
					newm.addControlFlow(e.getSource(), e.getTarget());
				}
			
			i++;
		}
		return models;
	}

}


