package models;

import java.util.*;

import nodes.Cluster;
import nodes.FlowObject;
import nodes.ProcessEdge;
import nodes.ProcessNode;
import bpmn.Activity;
import bpmn.Artifact;
import bpmn.Association;
import bpmn.ChoreographyActivity;
import bpmn.ComplexGateway;
import bpmn.Conversation;
import bpmn.ConversationLink;
import bpmn.EndEvent;
import bpmn.Event;
import bpmn.EventBasedGateway;
import bpmn.ExclusiveGateway;
import bpmn.Gateway;
import bpmn.InclusiveGateway;
import bpmn.IntermediateEvent;
import bpmn.Lane;
import bpmn.LaneableCluster;
import bpmn.Message;
import bpmn.MessageFlow;
import bpmn.ParallelGateway;
import bpmn.Pool;
import bpmn.SequenceFlow;
import bpmn.StartEvent;

public class BPMNModel extends ProcessModel {
	
	private ArrayList<Event> events = new ArrayList<Event>();
	
	ArrayList<ComplexGateway> comGates = new ArrayList<ComplexGateway>();
	ArrayList<EventBasedGateway> eBasGates = new ArrayList<EventBasedGateway>();
	ArrayList<ExclusiveGateway> exGates = new ArrayList<ExclusiveGateway>();
	ArrayList<InclusiveGateway> incGates = new ArrayList<InclusiveGateway>();
	ArrayList<ParallelGateway> parGates = new ArrayList<ParallelGateway>();

    public BPMNModel() {
        super();
    }
    
    public BPMNModel(String name) {
        super(name);
    }

    public String getDescription() {
        return "BPMN 2.0";
    }

    public void addFlowObject(FlowObject o) {
        super.addNode(o);
    }

    public void addFlow(ProcessEdge e) {
        addEdge(e);
    }

    public List<FlowObject> getFlowObjects() {
        // Figure out all flow objects
        List<FlowObject> result = new LinkedList<FlowObject>();
        for (ProcessNode n : super.getNodes()) {
            if (n instanceof FlowObject) {
                result.add((FlowObject) n);
            }
        }

        return result;
    }

    public List<SequenceFlow> getSequenceFlows() {
        List<SequenceFlow> result = new LinkedList<SequenceFlow>();
        for (ProcessEdge f : super.getEdges()) {
            if (f instanceof SequenceFlow) {
                result.add((SequenceFlow) f);
            }
        }
        return result;
    }

    public LinkedList<Association> getAssociations() {
        LinkedList<Association> result = new LinkedList<Association>();
        for (ProcessEdge f : super.getEdges()) {
            if (f instanceof Association) {
                result.add((Association) f);
            }
        }
        return result;
    }

    /**
     * Detects the Pool where the ProcessNode is contained inside. Returns
     * <b>null</b> if not in any Pool.
     * @param node
     * @return
     */
    public Pool getPoolForNode(ProcessNode node) {
        // Get Cluster
        Cluster c = getClusterForNode(node);
        if (c==null) return null; // Not in any Cluster
        while (c!=null) {
            if (c instanceof Pool) return (Pool)c;
            c = getClusterForNode(c);
        }
        return null;
    }

    @Override
    public String toString() {
        if (getProcessName() == null) {
            return super.toString();
        }
        return getProcessName() + " (BPMN)";
    }

    @Override
    public List<Class<? extends ProcessNode>> getSupportedNodeClasses() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(Activity.class);
        result.add(StartEvent.class);
        result.add(IntermediateEvent.class);
        result.add(EndEvent.class);
        result.add(Gateway.class);
        result.add(Artifact.class);
        result.add(Pool.class);
        result.add(ChoreographyActivity.class);
        result.add(Conversation.class);
        result.add(Message.class);        
        //result.add(StickyNote.class);
        return result;
    }

    @Override
    public List<Class<? extends ProcessEdge>> getSupportedEdgeClasses() {
        List<Class<? extends ProcessEdge>> result = new LinkedList<Class<? extends ProcessEdge>>();
        result.add(SequenceFlow.class);
        result.add(MessageFlow.class);
        result.add(Association.class);
        result.add(ConversationLink.class);
        return result;
    }
    
    @Override
    public void removeNode(ProcessNode node) {
    	super.removeNode(node);
    	if(node instanceof LaneableCluster) {
    		LaneableCluster _lc = (LaneableCluster) node;
        	for(Lane l:_lc.getLanes()) {
        		this.removeNode(l);
        	}
        }
    }
    
    public void extractGateways(){
		for (ProcessNode pn:super.getNodes()){
			if (pn instanceof ComplexGateway){
				ComplexGateway cg = (ComplexGateway) pn;
				comGates.add(cg);
			}
			if (pn instanceof EventBasedGateway){
				EventBasedGateway cg = (EventBasedGateway) pn;
				eBasGates.add(cg);
			}
			if (pn instanceof ExclusiveGateway){
				ExclusiveGateway cg = (ExclusiveGateway) pn;
				exGates.add(cg);
			}
			if (pn instanceof InclusiveGateway){
				InclusiveGateway cg = (InclusiveGateway) pn;
				incGates.add(cg);
			}
			if (pn instanceof ParallelGateway){
				ParallelGateway cg = (ParallelGateway) pn;
				parGates.add(cg);
			}
		}
	}
    
    public ArrayList<ComplexGateway> getComplexGateways(){
    	return comGates;
    }
    
    public ArrayList<EventBasedGateway> getEventBasedGateways(){
    	return eBasGates;
    }
    
    public ArrayList<ExclusiveGateway> getExclusiveGateways(){
    	return exGates;
    }
    
    public ArrayList<InclusiveGateway> getInclusiveGateways(){
    	return incGates;
    }
    
    public ArrayList<ParallelGateway> getParallelGateways(){
    	return parGates;
    }

	public void addEvent (Event e){
		events.add(e);
	}
	
	public ArrayList<Event> getEvents (){
		return events;
	}
    
}

