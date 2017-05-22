package epc;

import java.util.ArrayList;

import nodes.Cluster;
import nodes.ProcessEdge;
import nodes.ProcessNode;
import models.EPCModel;
import epc.Connector;
import epc.ConnectorAND;
import epc.ConnectorOR;
import epc.ConnectorXOR;
import epc.Event;
import epc.Function;
import epc.Organisation;
import epc.SequenceFlow;

public class EPCRepairer {
	
	private ArrayList<Organisation> orgs = new ArrayList<Organisation>();
	private ArrayList<Function> functions = new ArrayList<Function>();
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<Connector> connBuffer = new ArrayList<Connector>();
	private ArrayList<SequenceFlow> flows = new ArrayList<SequenceFlow>();
	private ArrayList<Connector> andSplits = new ArrayList<Connector>();
	private ArrayList<Connector> andJoins = new ArrayList<Connector>();
	private ArrayList<Connector> orSplits = new ArrayList<Connector>();
	private ArrayList<Connector> orJoins = new ArrayList<Connector>();
	private ArrayList<Connector> xorSplits = new ArrayList<Connector>();
	private ArrayList<Connector> xorJoins = new ArrayList<Connector>();
	
	private EPCModel model = null;
	
	public EPCRepairer(EPCModel epc){
		model=epc;
	}
	
	public void repairModel(){
		this.getIncomingOutgoingFlows();
		this.extractElements();
		this.checkConnectorIO(connBuffer);
		this.specifyConnectors();
		this.eventsCannotDecide();
		this.checkEvents();
		this.checkFunctions();
		this.checkConnectorsOutDecisionsEvents();
		this.checkConnectorIO(andJoins);
		this.checkConnectorIO(andSplits);
		this.checkConnectorIO(orJoins);
		this.checkConnectorIO(orSplits);
		this.checkConnectorIO(xorJoins);
		this.checkConnectorIO(xorSplits);
	}

	private void checkConnectorsOutDecisionsEvents() {
		for (Connector connector : xorSplits) {
			ArrayList<SequenceFlow> outgoings = connector.getOutgoing();
			for (SequenceFlow sequenceFlow : outgoings) {
				if ((sequenceFlow.getTarget() instanceof Function)){
					// add event
					
					ProcessNode out = sequenceFlow.getTarget();
					
					// insert dummy event
					String text= ((Function)out).getAdvMod();
					Event e = new Event(text);
//					sequenceFlow.setTarget(e);
					SequenceFlow sf = new SequenceFlow(e, out);
					sf.setSource(e);
					sf.setTarget(out);
					e.setOutgoing(sf);
					
					SequenceFlow e_in = new SequenceFlow(connector, e);
					e_in.setSource(connector);
					e_in.setTarget(e);
					e.setIncoming(e_in);
					
					if (out instanceof Function) {
						((Function) out).getIncoming().setSource(e);
					}
					
					events.add(e);
					flows.add(sf);
					flows.add(e_in);
					
					
				}
			}
		}
		
		for (Connector connector : orSplits) {
			
		}
	}

	public ArrayList<Organisation> getOrgs() {
		return orgs;
	}

	public ArrayList<Function> getFunctions() {
		return functions;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public ArrayList<Connector> getConnBuffer() {
		return connBuffer;
	}

	public ArrayList<SequenceFlow> getFlows() {
		return flows;
	}

	public ArrayList<Connector> getAndSplits() {
		return andSplits;
	}

	public ArrayList<Connector> getAndJoins() {
		return andJoins;
	}

	public ArrayList<Connector> getOrSplits() {
		return orSplits;
	}

	public ArrayList<Connector> getOrJoins() {
		return orJoins;
	}

	public ArrayList<Connector> getXorSplits() {
		return xorSplits;
	}

	public ArrayList<Connector> getXorJoins() {
		return xorJoins;
	}

	public void setOrgs(ArrayList<Organisation> orgs) {
		this.orgs = orgs;
	}

	public void setFunctions(ArrayList<Function> functions) {
		this.functions = functions;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public void setConnBuffer(ArrayList<Connector> connBuffer) {
		this.connBuffer = connBuffer;
	}

	public void setFlows(ArrayList<SequenceFlow> flows) {
		this.flows = flows;
	}

	public void setAndSplits(ArrayList<Connector> andSplits) {
		this.andSplits = andSplits;
	}

	public void setAndJoins(ArrayList<Connector> andJoins) {
		this.andJoins = andJoins;
	}

	public void setOrSplits(ArrayList<Connector> orSplits) {
		this.orSplits = orSplits;
	}

	public void setOrJoins(ArrayList<Connector> orJoins) {
		this.orJoins = orJoins;
	}

	public void setXorSplits(ArrayList<Connector> xorSplits) {
		this.xorSplits = xorSplits;
	}

	public void setXorJoins(ArrayList<Connector> xorJoins) {
		this.xorJoins = xorJoins;
	}

	private void getIncomingOutgoingFlows(){
		for (ProcessEdge a : new ArrayList<ProcessEdge>(model.getFlows())){
			if (a instanceof SequenceFlow){
				if (a.getSource()!=null && a.getTarget()!=null){
					SequenceFlow f = (SequenceFlow) a;
					a.getSource().setOutgoing(f);
					a.getTarget().setIncoming(f);
				}
			}
		}
	}
	
	private void extractElements(){
		for (Cluster c : new ArrayList<Cluster>(model.getClusters())){
    		if (c instanceof Organisation){
    			Organisation org = (Organisation) c;
    			orgs.add(org);
    		}
    	}
		for (ProcessNode a : new ArrayList<ProcessNode>(model.getFlowObjects())){
    		if (a instanceof Function){
    			Function f = (Function) a;
    			functions.add(f);
    		}
    		if (a instanceof Connector){
    			Connector c = (Connector) a;
    			connBuffer.add(c);
    		}
    	}
		events=model.getEvents();
		for (ProcessEdge a : new ArrayList<ProcessEdge>(model.getFlows())){
    		if (a instanceof SequenceFlow){
    			SequenceFlow f = (SequenceFlow) a;
    			flows.add(f);
    		}
    	}
	}
	
	private void checkConnectorIO(ArrayList<Connector> conns){
		ArrayList<Connector> newCon = new ArrayList<Connector>();
		for (Connector con : conns){
			if (con.getIncoming().size()>1 && con.getOutgoing().size()>1){
				Connector first = Connector.class.cast(con);
				Connector sec = Connector.class.cast(con);
				SequenceFlow f = new SequenceFlow(first,sec);
				first.overrideIncoming(con.getIncoming());
				first.setOutgoing(f);
				sec.setIncoming(f);
				sec.overrideOutgoing(con.getOutgoing());
				flows.add(f);
				newCon.add(first);
				newCon.add(sec);
				for (SequenceFlow i:con.getIncoming()){
					i.setTarget(first);
				}
				for (SequenceFlow o:con.getOutgoing()){
					o.setSource(sec);
				}
			} else {
				newCon.add(con);
			}
		}
		conns=newCon;
	}
	
	private void specifyConnectors(){
		for (Connector con:connBuffer){
			if (con instanceof ConnectorAND){
				ConnectorAND c = (ConnectorAND) con;
				if (con.getIncoming().size()>1){
					andJoins.add(c);
				} else {
					andSplits.add(c);
				}
			} else {
				if (con instanceof ConnectorOR){
					ConnectorOR c = (ConnectorOR) con;
					if (con.getIncoming().size()>1){
						orJoins.add(c);
					} else {
						orSplits.add(c);
					}
				} else {
					ConnectorXOR c = (ConnectorXOR) con;
					if (con.getIncoming().size()>1){
						xorJoins.add(c);
					} else {
						xorSplits.add(c);
					}
				}
			}
		}
	}
	
	private void eventsCannotDecide(){
		ArrayList<Connector> buffer = new ArrayList<Connector>();
		buffer.addAll(orSplits);
		buffer.addAll(xorSplits);
		for (Connector c:buffer){
			boolean allEvents=true;
			boolean allFunctions=true;
			for (SequenceFlow f:c.getOutgoing()){
				if (f.getTarget() instanceof Event){
					allFunctions=false;
				}
				if (f.getTarget() instanceof Function){
					allEvents=false;
				}
			}
			if (allEvents && c.getIncoming().get(0).getSource() instanceof Event){
				Function func = new Function("make decision");
				c.getIncoming().get(0).setTarget(func);
				SequenceFlow sf = new SequenceFlow(func,c);
				functions.add(func);
				flows.add(sf);
			} else {
				if (c.getIncoming().get(0).getSource() instanceof Event){
					Event toMove = (Event) c.getIncoming().get(0).getSource();
					if (allFunctions){
						ArrayList<SequenceFlow> flowBuffer = new ArrayList<SequenceFlow>();
						for (SequenceFlow sf:c.getOutgoing()){
							Event e = (Event) toMove.clone();
							e.setId("" + e.hashCode());
							sf.setSource(e);
							SequenceFlow toAdd = new SequenceFlow(c,e);
							toAdd.setSource(c);
							toAdd.setTarget(e);
							events.add(e);
							flows.add(toAdd);
							flowBuffer.add(toAdd);
						}
						c.overrideOutgoing(flowBuffer);
						c.getIncoming().get(0).setSource(toMove.getIncoming().getSource());
						flows.remove(toMove.getIncoming());
						events.remove(toMove);
					} else {
						ArrayList<SequenceFlow> flowBuffer = new ArrayList<SequenceFlow>();
						for (SequenceFlow sf:c.getOutgoing()){
							if (sf.getTarget() instanceof Function){
								Event e = (Event) toMove.clone();
								e.setId("" + e.hashCode());
								sf.setSource(e);
								SequenceFlow toAdd = new SequenceFlow(c,e);
								toAdd.setSource(c);
								toAdd.setTarget(e);
								events.add(e);
								flows.add(toAdd);
								flowBuffer.add(toAdd);
							} else {
								flowBuffer.add(sf);
							}
							c.overrideOutgoing(flowBuffer);
							c.getIncoming().get(0).setSource(toMove.getIncoming().getSource());
							flows.remove(toMove.getIncoming());
							events.remove(toMove);
						}
					}
				}
			}
		}
	}
	
	private void checkFunctions(){
		ArrayList<Function> toDelete = new ArrayList<Function>();
		for (Function f:functions){
			if (f.getIncoming()==null){
				addStartEvent(f);
			} else {
				if(f.getIncoming().getSource()==null){
					flows.remove(f.getIncoming());
					addStartEvent(f);
				} else {
					if (f.getIncoming().getSource().getText().contains("Dummy")){
						// old stuff (Projekarbeit)
//						toDelete.add((Function) f.getIncoming().getSource());
//						addStartFromDummy(f);
						
						// author: Simon
						toDelete.add((Function) f.getIncoming().getSource());
						Function f_incoming = (Function) f.getIncoming().getSource();
						if (f_incoming.getIncoming() == null){
							addStartFromDummy(f);
						} else {
							removeDummy(f);
						}
						
						
						
					}
				}
			}
			if (f.getOutgoing()==null){
				Event e = new Event("end");
				SequenceFlow sf = new SequenceFlow(f,e);
				sf.setSource(f);
				sf.setTarget(e);
				events.add(e);
				flows.add(sf);
			} else {
				// f has outgoing nodes
				
				if (f.getOutgoing().getTarget() instanceof Function){
					// insert dummy event
//					String text= f.getName()+" is done";
//					Event e = new Event(text);
//					f.getOutgoing().setSource(e);
//					e.setOutgoing(f.getOutgoing());
//					e.getOutgoing().getTarget().setIncoming(e.getOutgoing());
//					SequenceFlow sf = new SequenceFlow(f,e);
//					sf.setSource(f);
//					sf.setTarget(e);
//					f.setOutgoing(sf);
//					e.setIncoming(sf);
//					events.add(e);
//					flows.add(sf);
				} else {
					if (f.getOutgoing().getTarget() instanceof Connector){
						Connector c = (Connector) f.getOutgoing().getTarget();
						
						boolean allEvents = true;
						for (SequenceFlow outFlow : c.getOutgoing()) {
							if (! (outFlow.getTarget() instanceof Event)){
								allEvents = false;
								break;
							}
						}
						
						if (allEvents) {
							// do nothing
							
							// insert dummy event
//							String text= f.getName()+" is done";
//							Event e = new Event(text);
//							f.getOutgoing().setSource(e);
//							e.setOutgoing(f.getOutgoing());
//							e.getOutgoing().getTarget().setIncoming(e.getOutgoing());
//							SequenceFlow sf = new SequenceFlow(f,e);
//							sf.setSource(f);
//							sf.setTarget(e);
//							f.setOutgoing(sf);
//							e.setIncoming(sf);
//							events.add(e);
//							flows.add(sf);
						} else {
							
//							if (c.get)
//							for (SequenceFlow flow : c.getOutgoing()) {
//								if (! (flow.getTarget() instanceof Event)){
//									
//								}
//							}
//							
							
							
							
							
							
//							ArrayList<SequenceFlow> newOuts = new ArrayList<SequenceFlow>();
//							for (SequenceFlow sf:c.getOutgoing()){
//								if (sf.getTarget() instanceof Function){
									// insert dummy event
//									String text= f.getName()+" is done";
//									Event e = new Event(text);
//									f.getOutgoing().setSource(e);
//									e.setOutgoing(f.getOutgoing());
//									e.getOutgoing().getTarget().setIncoming(e.getOutgoing());
//									SequenceFlow sf2 = new SequenceFlow(f,e);
//									sf2.setSource(f);
//									sf2.setTarget(e);
//									f.setOutgoing(sf2);
//									e.setIncoming(sf2);
//									events.add(e);
//									flows.add(sf2);
//								} else {
//									newOuts.add(sf);
//								}
//							}
//							c.overrideOutgoing(newOuts);
						}
						
						
					}
				}
			}
		}
		functions.removeAll(toDelete);
	}
	
	private void checkEvents(){
		ArrayList<Event> toDelete = new ArrayList<Event>();
		for (Event e:events){
			if (e.getOutgoing() != null){
				if (e.getOutgoing().getTarget() instanceof Event){
					e.getIncoming().setTarget(e.getOutgoing().getTarget());
					flows.remove(e.getOutgoing());
					toDelete.add(e);
				} else {
					if (e.getOutgoing().getTarget() instanceof Connector){
						Connector c = (Connector) e.getOutgoing().getTarget();
						e.getIncoming().setTarget(c);
						c.getIncoming().remove(e.getOutgoing());
						c.setIncoming(e.getIncoming());
						flows.remove(e.getOutgoing());
						toDelete.add(e);
					}
				}
			}
		}
		events.removeAll(toDelete);
	}
	
	private void addStartFromDummy(Function f){
		if(f.getIncoming().getSource() instanceof Event){
			events.remove(f.getIncoming().getSource());
		}
		if(f.getIncoming().getSource() instanceof Connector){
			andJoins.remove(f.getIncoming().getSource());
			andSplits.remove(f.getIncoming().getSource());
			orJoins.remove(f.getIncoming().getSource());
			orSplits.remove(f.getIncoming().getSource());
			xorJoins.remove(f.getIncoming().getSource());
			xorSplits.remove(f.getIncoming().getSource());
		}
		flows.remove(f.getIncoming());
		addStartEvent(f);
	}
	
	// author: Simon
	private void removeDummy(Function f){
		SequenceFlow flow = f.getIncoming();
		flows.remove(flow);
		Function f_source = (Function)(flow.getSource());
		flow.setSource(f_source.getIncoming().getSource());
		flows.add(flow);
	}
	
	private void addStartEvent(Function f){
		Event e = new Event("start");
		SequenceFlow sf = new SequenceFlow(e,f);
		sf.setSource(e);
		sf.setTarget(f);
		e.setOutgoing(sf);
		f.setIncoming(sf);
		events.add(e);
		flows.add(sf);
	}
	
}
