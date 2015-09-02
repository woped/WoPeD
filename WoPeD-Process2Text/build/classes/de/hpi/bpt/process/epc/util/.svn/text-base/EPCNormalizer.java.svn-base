package de.hpi.bpt.process.epc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.bpt.graph.algo.TransitiveClosure;
import de.hpi.bpt.process.epc.Connection;
import de.hpi.bpt.process.epc.Connector;
import de.hpi.bpt.process.epc.ConnectorType;
import de.hpi.bpt.process.epc.ControlFlow;
import de.hpi.bpt.process.epc.Event;
import de.hpi.bpt.process.epc.FlowObject;
import de.hpi.bpt.process.epc.Function;
import de.hpi.bpt.process.epc.IEPC;
import de.hpi.bpt.process.epc.IFlowObject;
import de.hpi.bpt.process.epc.Node;
import de.hpi.bpt.process.epc.NonFlowObject;
import de.hpi.bpt.process.epc.ProcessInterface;

/**
 * The normalizer does a lot of structural transformations on an EPC. 
 * Please note that it aims at simplifying the EPC and is NOT behaviour-preserving.
 * 
 * @author matthias.weidlich
 *
 */
public class EPCNormalizer {
	
	protected IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject> epc;
	
	protected TransitiveClosure<ControlFlow, FlowObject> closure;
	
	protected int idCounter = 0;
	
	public EPCNormalizer(IEPC<ControlFlow, FlowObject, Event, Function, Connector, ProcessInterface, Connection, Node, NonFlowObject> epc) {
		this.epc = epc;
		this.closure = new TransitiveClosure<ControlFlow, FlowObject>(this.epc);
	}

	public boolean containsORConnector() {
		boolean containsORConnector = false;
		for(IFlowObject v : this.epc.getFlowObjects()) {
			if (v instanceof Connector) {
				containsORConnector |= ((Connector)v).isOR();
			}
		}
		return containsORConnector;
	}
	
	public Collection<FlowObject> getStartEvents() {
		Collection<FlowObject> events = new HashSet<FlowObject>();
		for (FlowObject o : this.epc.getFlowObjects()) {
			if (o instanceof Event && this.epc.getPredecessors(o).size() == 0)
				events.add(o);
		}
		return events;
	}
	
	public Collection<FlowObject> getEndEvents() {
		Collection<FlowObject> events = new HashSet<FlowObject>();
		for (FlowObject o : this.epc.getFlowObjects()) {
			if (o instanceof Event && this.epc.getSuccessors(o).size() == 0)
				events.add(o);
		}
		return events;
	}

	
	protected String getIdString() {
		return "start " + this.idCounter++;
	}


	protected boolean isPureSplit(Connector connector) {
		return ((this.epc.getPredecessors(connector).size() == 1) && (this.epc.getSuccessors(connector).size() > 1));
	}
	
	protected boolean isPureJoin(Connector connector) {
		return ((this.epc.getSuccessors(connector).size() == 1) && (this.epc.getPredecessors(connector).size() > 1));
	}
	
	protected boolean isSplit(Connector connector) {
		return this.epc.getSuccessors(connector).size() > 1;
	}
	
	protected boolean isJoin(Connector connector) {
		return this.epc.getPredecessors(connector).size() > 1;
	}
	
	protected boolean containsConnector(Collection<FlowObject> objects) {
		boolean result = false;
		for(FlowObject o : objects) {
			result |= o instanceof Connector;
		}
		return result;
	}
	
	protected boolean containsSplit(Collection<FlowObject> objects) {
		boolean result = false;
		for(FlowObject o : objects) {
			if (o instanceof Connector) {
				result |= isSplit((Connector)o);
			}
		}
		return result;
	}
	
	protected boolean containsJoin(Collection<FlowObject> objects) {
		boolean result = false;
		for(FlowObject o : objects) {
			if (o instanceof Connector) {
				result |= isJoin((Connector)o);
			}
		}
		return result;
	}

	protected FlowObject getCommonPredecessor(FlowObject object) {
		List<FlowObject> objects = new ArrayList<FlowObject>(this.epc.getPredecessors(object));
		return getCommonPredecessor(objects);
	}
	
	protected FlowObject getCommonSuccessor(FlowObject object) {
		List<FlowObject> objects = new ArrayList<FlowObject>(this.epc.getSuccessors(object));
		return getCommonSuccessor(objects);
	}

	
	protected FlowObject getCommonPredecessor(List<FlowObject> objects) {
		List<FlowObject> predecessors = new ArrayList<FlowObject>();
		
		if (objects.size() < 2)
			return (objects.size() == 0) ? null : objects.get(0);

		boolean noCommonPredecessor = false;
		
		outer:
		for (FlowObject o : objects) {
			if (o instanceof Connector) {
				Connector connector = (Connector) o;
				if (isSplit(connector)) {
					if (!predecessors.contains(connector))
						predecessors.add(connector);
					continue outer;
				}
				if (isJoin(connector)) {
					FlowObject cP = getCommonPredecessor(connector);
					if (cP == null) {
						noCommonPredecessor = true;
						break outer;
					}
					else {
						if (!predecessors.contains(cP))
							predecessors.add(cP);
					}
				}
			}
			else {
				if (this.epc.getPredecessors(o).size() == 0) {
					noCommonPredecessor = true;
					break outer;
				}
				for (FlowObject p : this.epc.getPredecessors(o)) {
					if (!predecessors.contains(p))
						predecessors.add(p);
				}
			}
		}
		
		if (noCommonPredecessor || objects.equals(predecessors))
			predecessors.clear();
		
		return getCommonPredecessor(predecessors);
	}
	
	protected FlowObject getCommonSuccessor(List<FlowObject> objects) {
		List<FlowObject> successors = new ArrayList<FlowObject>();
		
		if (objects.size() < 2)
			return (objects.size() == 0) ? null : objects.get(0);

		boolean noCommonSuccessor = false;
		
		outer:
		for (FlowObject o : objects) {
			if (o instanceof Connector) {
				Connector connector = (Connector) o;
				if (isJoin(connector)) {
					if (!successors.contains(connector))
						successors.add(connector);
					continue outer;
				}
				if (isSplit(connector)) {
					FlowObject cS = getCommonSuccessor(connector);
					if (cS == null) {
						noCommonSuccessor = true;
						break outer;
					}
					else {
						if (!successors.contains(cS))
							successors.add(cS);
					}
				}
			}
			else {
				if (this.epc.getSuccessors(o).size() == 0) {
					noCommonSuccessor = true;
					break outer;
				}
				for (FlowObject p : this.epc.getSuccessors(o)) {
					if (!successors.contains(p))
						successors.add(p);
				}
			}
		}
		
		if (noCommonSuccessor || objects.equals(successors))
			successors.clear();
		
		return getCommonSuccessor(successors);
	}

	
	protected boolean directlyFollowsStartEvents(Collection<FlowObject> objects) {
		boolean followsStartEvents = true;
		
		for (FlowObject o : objects) {
			if (this.closure.isInLoop(o)) {
				followsStartEvents = false;
				break;
			}
			if (o instanceof Connector) {
				Connector connector = (Connector) o;
				
				if (isSplit(connector)) {
					if (!(connector.isAND())) {
						followsStartEvents = false;
						break;
					}
					followsStartEvents &= directlyFollowsStartEvents(this.epc.getPredecessors(connector));
				}
				if (isJoin(connector)) {
					FlowObject cP = getCommonPredecessor(connector);
					if (cP == null) {
						followsStartEvents &= directlyFollowsStartEvents(this.epc.getPredecessors(connector));
					}
					else {
						if (cP instanceof Connector) {
							if (((Connector)cP).getConnectorType().equals(connector.getConnectorType())) {
								followsStartEvents &= directlyFollowsStartEvents(this.epc.getPredecessors(cP));
							}
							else {
								followsStartEvents = false;
								break;
							}
						}
						else {
							followsStartEvents = false;
							break;
						}
					}
				}
			} 
			else {
				followsStartEvents &= directlyFollowsStartEvents(this.epc.getPredecessors(o));
			}
		}
		return followsStartEvents;
	}
	
	public void handleORJoins() {
		for(FlowObject v : this.epc.getFlowObjects()) {
			if ((v instanceof Connector) && !(this.closure.isInLoop(v))) {
				Connector connector =  (Connector)v;
				if (isPureJoin(connector) && connector.isOR()) {
					IFlowObject predecessor = getCommonPredecessor(connector);
					if (predecessor  instanceof Connector) {
						if (((Connector) predecessor).getConnectorType().equals(ConnectorType.OR)) {
							connector.setConnectorType(ConnectorType.AND);
							((Connector) predecessor).setConnectorType(ConnectorType.AND);
						}
						else {
							connector.setConnectorType(((Connector) predecessor).getConnectorType());
						}
					}
					if (directlyFollowsStartEvents(this.epc.getPredecessors(connector))) {
						connector.setConnectorType(ConnectorType.AND);
					}
				}
				/*
				 * Handle degenerated connectors with one incoming and one outgoing flow
				 */
				if (this.epc.getPredecessors(connector).size() == 1 && this.epc.getSuccessors(connector).size() == 1) {
					connector.setConnectorType(ConnectorType.AND);
				}
			}
		}
	}

	public void replaceORSplitsByANDSplits() {
		for(IFlowObject v : this.epc.getFlowObjects()) {
			if (v instanceof Connector) {
				Connector connector =  (Connector)v;
				if (isPureSplit(connector) && connector.isOR()) {
					((Connector)v).setConnectorType(ConnectorType.AND);
				}
			}
		}
	}
	
	protected Connector createStartClosure(Connector end) {
		Connector start = new Connector(ConnectorType.AND);
		start.setId(getIdString());
		start.setConnectorType(end.getConnectorType());

		for (FlowObject o : this.epc.getPredecessors(end)) {
			if (this.closure.hasPath(end, o))
				continue;
			FlowObject tmp = getEntryPoint(o);
			if (!tmp.equals(start))
				this.epc.addControlFlow(start, tmp);
		}
		return start;
	}
	
	protected Connector createEndClosure(Connector start) {
		Connector end = new Connector(ConnectorType.AND);
		end.setId(getIdString());
		end.setConnectorType(start.getConnectorType());

		for (FlowObject o : this.epc.getSuccessors(start)) {
			if (this.closure.hasPath(o, start))
				continue;
			FlowObject tmp = getExitPoint(o);
			if (!tmp.equals(end))
				this.epc.addControlFlow(tmp, end);
			
		}
		return end;
	}

	protected FlowObject getExitPoint(FlowObject object) {
		
		if (object instanceof Connector) {
			Connector connector = (Connector) object;
			if (isSplit(connector)) {
				FlowObject cS = getCommonSuccessor(connector);
				
				if (cS != null) {
					return getExitPoint(cS);
				}
				else {
					return createEndClosure(connector);
				}
			}
			/*
			 * Degenerated connectors
			 */
			else if ((this.epc.getPredecessors(connector).size() == 1) && (this.epc.getSuccessors(connector).size() == 1)){
				return getExitPoint(this.epc.getSuccessors(connector).iterator().next());
			}
			else if (isPureJoin(connector)){
				/*
				 * Note that this is only possible, if the Join is well-structured.
				 * Thus, be sure to call canCreateEndClosure to check this before you call
				 * createEndClosure, which invokes this method.
				 */
				return getExitPoint(this.epc.getSuccessors(connector).iterator().next());
			}
			else if ((this.epc.getSuccessors(connector).size() == 0)) {
				/*
				 * We might have reached an artificial exit point, that was already created
				 */
				return connector;
			}
			else {
				return null;
			}
		}
		else {
			return (this.epc.getSuccessors(object).size() == 0) ? object : getExitPoint(this.epc.getSuccessors(object).iterator().next());
		}
	}
	
	protected FlowObject getEntryPoint(FlowObject object) {
		
		if (object instanceof Connector) {
			Connector connector = (Connector) object;
			if (isJoin(connector)) {
				FlowObject cP = getCommonPredecessor(connector);
				
				if (cP != null) {
					return getEntryPoint(cP);
				}
				else {
					return createStartClosure(connector);
				}
			}
			else if ((this.epc.getPredecessors(connector).size() == 1) && (this.epc.getSuccessors(connector).size() == 1)){
				/*
				 * Degenerated connectors
				 */
				return getEntryPoint(this.epc.getPredecessors(connector).iterator().next());
			}
			else if (isPureSplit(connector)){
				/*
				 * Note that this is only possible, if the Split is well-structured.
				 * Thus, be sure to call canCreateStartClosure to check this before you call
				 * createStartClosure, which invokes this method.
				 */
				return getEntryPoint(this.epc.getPredecessors(connector).iterator().next());
			}
			else if ((this.epc.getPredecessors(connector).size() == 0)) {
				/*
				 * We might have reached an artificial entry point, that was already created
				 */
				return connector;
			}
			else {
				return null;
			}
		}
		else {
			return (this.epc.getPredecessors(object).size() == 0) ? object : getEntryPoint(this.epc.getPredecessors(object).iterator().next());
		}
	}

	public void handleStartJoin() {
		Connector startJoin = determineStartJoin();
		
		if (startJoin != null) {
			if (canCreateStartClosure(new HashSet<FlowObject>(),startJoin,startJoin,startJoin)) {
				Connector processStart = createStartClosure(startJoin);
	
				Event n = new Event();
				n.setId(getIdString());
				n.setName("START EVENT");
				this.epc.addFlowObject(n);
				
				Function a = new Function();
				a.setId(getIdString());
				a.setName("START FUNCTION");
				this.epc.addFlowObject(a);
	
				this.epc.addControlFlow(n, a);
				this.epc.addControlFlow(a, processStart);
				
				/*
				 * Closure needs to be recalculated as we changed the model structure
				 */
				this.closure = new TransitiveClosure<ControlFlow, FlowObject>(this.epc);
			}
		}
	}
	
	protected boolean isPathWithoutContainingNode(Set<FlowObject> checked, FlowObject from, FlowObject to, FlowObject withoutNode) {

		if (to.equals(withoutNode))
			return false;
		
		if (!from.equals(withoutNode) && this.epc.getSuccessors(from).contains(to))
			return true;

		if (from.equals(withoutNode))
			return false;
		
		boolean result = false;
		for (FlowObject o : this.epc.getSuccessors(from)) {
			if (!checked.contains(o)) {
				if (this.closure.hasPath(o, to)) {
					checked.add(o);
					result |= isPathWithoutContainingNode(checked,o,to,withoutNode);
				}
			}
		}
		return result;
	}
	
	public boolean canCreateStartClosure(Set<FlowObject> checked, Connector startJoin, Connector anchor, FlowObject current) {
		/*
		 * We have to be careful with multiple successors.
		 * They are only allowed, if all paths from the current
		 * node always lead to the current anchor.
		 */
		if (!current.equals(startJoin)) {
			if (this.epc.getSuccessors(current).size() > 1) {
				/*
				 * Any path not leading to the start join?
				 */
				for (FlowObject o : this.epc.getSuccessors(current))
					if (!this.closure.hasPath(o, startJoin))
						return false;
				/*
				 * All paths lead to current anchor?
				 */
				if (isPathWithoutContainingNode(new HashSet<FlowObject>(),current,startJoin,anchor))
					return false;
			}
		}
		
		/*
		 * No predecessor, that is ok (in case the successor are ok as well)
		 */
		if (this.epc.getPredecessors(current).size() == 0)
			return true;
		
		/*
		 * One predecessor, check this predecessor without changing anchor
		 */
		if (this.epc.getPredecessors(current).size() == 1) {
			FlowObject p = this.epc.getPredecessors(current).iterator().next();
			if (!checked.contains(p)) {
				checked.add(p);
				return canCreateStartClosure(checked,startJoin,anchor,p);
			}
		}
		
		/*
		 * We have a connector, check each predecessor
		 */
		boolean result = true;
		for (FlowObject o : this.epc.getPredecessors(current)) {
			if (!checked.contains(o)) {
				checked.add(o);
				result &= canCreateStartClosure(checked,startJoin,(Connector) current,o);
			}
		}
		return result;
	}
	
	public boolean canCreateEndClosure(Set<FlowObject> checked, Connector endSplit, Connector anchor, FlowObject current) {
		/*
		 * We have to be careful with multiple predecessors.
		 * They are only allowed, if all paths from the end split 
		 * always lead to the current anchor.
		 */
		if (!current.equals(endSplit)) {
			if (this.epc.getPredecessors(current).size() > 1) {

				/*
				 * Any path not coming from the end split?
				 */
				for (FlowObject o : this.epc.getPredecessors(current))
					if (!this.closure.hasPath(endSplit, o))
						return false;
				/*
				 * All paths from anchor lead to current node?
				 */
				if (isPathWithoutContainingNode(new HashSet<FlowObject>(),endSplit,current,anchor))
					return false;
			}
		}
		
		/*
		 * No successors, that is ok (in case the predecessors are ok as well)
		 */
		if (this.epc.getSuccessors(current).size() == 0)
			return true;
		
		/*
		 * One successors, check this successors without changing anchor
		 */
		if (this.epc.getSuccessors(current).size() == 1) {
			FlowObject s = this.epc.getSuccessors(current).iterator().next();
			if (!checked.contains(s)) {
				checked.add(s);
				return canCreateEndClosure(checked,endSplit,anchor,s);
			}
		}
		
		/*
		 * We have a connector, check each successor
		 */
		boolean result = true;
		for (FlowObject o : this.epc.getSuccessors(current)) {
			if (!checked.contains(o)) {
				checked.add(o);
				result &= canCreateEndClosure(checked, endSplit,(Connector) current,o);
			}
		}
		return result;
	}

	
	public void handleEndSplit() {
		Connector endSplit = determineEndSplit();
		
		if (endSplit != null) {
			if (canCreateEndClosure(new HashSet<FlowObject>(),endSplit,endSplit,endSplit)) {
				Connector processEnd = createEndClosure(endSplit);
	
				Function a = new Function();
				a.setId(getIdString());
				a.setName("END FUNCTION");
				this.epc.addFlowObject(a);
	
				Event n = new Event();
				n.setId(getIdString());
				n.setName("END EVENT");
				this.epc.addFlowObject(n);
				
				this.epc.addControlFlow(processEnd,a);
				this.epc.addControlFlow(a, n);
				
				/*
				 * Closure needs to be recalculated as we changed the model structure
				 */
				this.closure = new TransitiveClosure<ControlFlow, FlowObject>(this.epc);
			}
		}
	}

	public Connector determineStartJoin() {
		Connector startJoin = null;
		int numPredecessorsOfStartJoin = 0; // used for determining the left most start join
		for (FlowObject o : this.epc.getFlowObjects()) {
			if (o instanceof Connector) {
				Connector connector = (Connector) o;
				if (isJoin(connector)) {
					boolean isStartJoin = true;
					int numPredTemp = 0;

					for (FlowObject e : this.getStartEvents()) {
						if (!this.closure.hasPath(e, o)) {
							isStartJoin = false;
							break;
						}
					}
					
					if (!isStartJoin)
						continue;
					
					for (FlowObject e : this.getEndEvents()) {
						if (!this.closure.hasPath(o, e)) {
							isStartJoin = false;
							break;
						}
					}
					
					if (!isStartJoin)
						continue;
					
					for (FlowObject o2 : this.epc.getFlowObjects()) {
						if (!o.equals(o2)) {
							if (this.closure.hasPath(o2, o))
								numPredTemp++;
						}
					}
					
					if (isStartJoin) {
						if (numPredecessorsOfStartJoin == 0 || numPredTemp < numPredecessorsOfStartJoin) {
							startJoin = connector;
							numPredecessorsOfStartJoin = numPredTemp;
						}
					}
				}
			}
		}
		return startJoin;
	}

	
	protected Connector determineEndSplit() {
		Connector endSplit = null;
		int numSuccessorsOfEndSplit = Integer.MAX_VALUE; // used for determining the right most end split
		for (FlowObject o : this.epc.getFlowObjects()) {
			if (o instanceof Connector) {
				Connector connector = (Connector) o;
				if (isSplit(connector)) {
					
					boolean isEndSplit = true;
					int numSuccTemp = 0;

					for (FlowObject e : this.getStartEvents()) {
						if (!this.closure.hasPath(e, o)) {
							isEndSplit = false;
							break;
						}
					}
					
					if (!isEndSplit)
						continue;
					
					for (FlowObject e : this.getEndEvents()) {
						if (!this.closure.hasPath(o, e)) {
							isEndSplit = false;
							break;
						}
					}
					
					if (!isEndSplit)
						continue;
					
					for (FlowObject o2 : this.epc.getFlowObjects()) {
						if (!o.equals(o2)) {
							if (this.closure.hasPath(o, o2))
								numSuccTemp++;
						}
					}

					if (isEndSplit) {
						if (numSuccessorsOfEndSplit == 0 || numSuccTemp < numSuccessorsOfEndSplit) {
							endSplit = connector;
							numSuccessorsOfEndSplit = numSuccTemp;
						}
					}
				}
			}
		}
		return endSplit;
	}

}
