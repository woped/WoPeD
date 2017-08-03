package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import nodes.FlowObject;
import nodes.ProcessEdge;
import nodes.ProcessNode;
import epc.Association;
import epc.Connector;
import epc.ConnectorAND;
import epc.ConnectorOR;
import epc.ConnectorXOR;
import epc.Event;
import epc.Function;
import epc.InformationObject;
import epc.Organisation;
import epc.SequenceFlow;

public class EPCModel extends ProcessModel {
	
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<ConnectorAND> connsAND = new ArrayList<ConnectorAND>();
	private ArrayList<ConnectorOR> connsOR = new ArrayList<ConnectorOR>();
	private ArrayList<ConnectorXOR> connsXOR = new ArrayList<ConnectorXOR>();
	
	public enum conType {
		AND, OR, XOR
	}
	
	public EPCModel(){
		super();
	}
	
	public EPCModel(String name) {
        super(name);
    }
	
	public void addFlowObject(FlowObject o) {
        super.addNode(o);
    }
	
	public void addFlow(ProcessEdge e) {
        addEdge(e);
    }
	
	public List<ProcessEdge> getFlows() {
        return getEdges();
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
	
	@Override
    public String toString() {
        if (getProcessName() == null) {
            return super.toString();
        }
        return getProcessName() + " (EPC)";
    }

	@Override
    public List<Class<? extends ProcessEdge>> getSupportedEdgeClasses() {
        List<Class<? extends ProcessEdge>> result = new LinkedList<Class<? extends ProcessEdge>>();
        result.add(SequenceFlow.class);
        result.add(Association.class);
        return result;
    }

	@Override
    public List<Class<? extends ProcessNode>> getSupportedNodeClasses() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(Event.class);
        result.add(Function.class);
        result.add(InformationObject.class);
        result.add(Connector.class);
        return result;
    }

	@Override
	public String getDescription() {
		return "EPML 2.0";
	}
	
	public void addEvent (Event e){
		events.add(e);
	}
	
	public ArrayList<Event> getEvents (){
		return events;
	}
	
	public void extractConnectors(){
		for (ProcessNode pn : super.getNodes()){
			if (pn instanceof ConnectorAND){
				ConnectorAND ca = (ConnectorAND) pn;
				connsAND.add(ca);
			}
			if (pn instanceof ConnectorOR){
				ConnectorOR co = (ConnectorOR) pn;
				connsOR.add(co);
			}
			if (pn instanceof ConnectorXOR){
				ConnectorXOR cx = (ConnectorXOR) pn;
				connsXOR.add(cx);
			}
		}
	}
	
	public ArrayList<ConnectorAND> getConnectorAND(){
		return connsAND;
	}
	
	public ArrayList<ConnectorOR> getConnectorOR(){
		return connsOR;
	}
	
	public ArrayList<ConnectorXOR> getConnectorXOR(){
		return connsXOR;
	}
	
	

}
