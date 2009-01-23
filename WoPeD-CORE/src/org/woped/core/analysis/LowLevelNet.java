package org.woped.core.analysis;

import java.util.Iterator;
import java.util.LinkedList;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.analysis.*;

public class LowLevelNet {
	private LinkedList<FlowNode> m_netNodes;

	private Iterator<FlowNode> getM_netNodesIter() {
		return m_netNodes.iterator();
	}

	public LowLevelNet() {
		m_netNodes = new LinkedList<FlowNode>();
	}

	public void addNode(FlowNode n) {
		m_netNodes.add(n);
	}

	public void addArc(FlowNode n1, FlowNode n2) {
		FlowArc a = new FlowArc(n1, n2);

		n1.addOutgoingArc(a);
		n2.addIncomingArc(a);
	}

	public FlowNode getNodeForElement(AbstractElementModel petrinetElement,
			boolean first) {
		// liefere für lowlevel Petrinet-Element das passende FlowNet Element
		// (1. oder 2.)

		FlowNode returnValue = null;
		Iterator<FlowNode> i = m_netNodes.iterator();

		while (i.hasNext()) {
			FlowNode currentFlowNode = i.next();

			if (currentFlowNode.getPetriNetNode().equals(petrinetElement)
					&& (currentFlowNode.isFirst() == first)) {
				returnValue = currentFlowNode;
			}
		}
		return returnValue;
	}

	public void dumpList(LinkedList<FlowNode> list, String pref)
	{
		StringBuffer out = new StringBuffer(pref);
		for (Iterator<FlowNode> i=list.iterator();i.hasNext();)
		{
			FlowNode next = i.next();
			out.append(next.getPetriNetNode().getNameValue() + ",");
		}
		
    	LoggerManager.debug(Constants.STRUCT_LOGGER, out.toString());
		
	}
	
	public int getMaxFlow(FlowNode source, FlowNode sink) {
		boolean done = false;
		boolean SINKFOUND = false;

		resetTempData();

    	LoggerManager.debug(Constants.STRUCT_LOGGER, "Calculating flow from '" + source.getPetriNetNode().getId() + "' to '" + sink.getPetriNetNode().getId() + "'");
		
		while (!done) {
			// Reset visited flag
			for (Iterator<FlowNode> i=m_netNodes.iterator();i.hasNext();)
				i.next().setVisited(false);
			
			source.setVisited(true);
			LinkedList<FlowNode> R = new LinkedList();
			R.add(source);
			source.setZ(Integer.MAX_VALUE);
			source.setPredecessor(null);
			SINKFOUND = false;
			Iterator<FlowArc> Y = null;
			FlowNode X = null;
			FlowArc tmparc = null;
			
			while ((!SINKFOUND) && (!done)) {
				// So lange R nicht leer und
				// Senke nicht gefunden

				// X = R[0];
				// R.ERASE(X); // Oberstes Element vom Stapel holen
				// if (!R.isEmpty()) {
				X = R.getFirst();
				R.removeFirst();
				
            	// LoggerManager.debug(Constants.STRUCT_LOGGER, "Removing node X='" + X.getPetriNetNode().getId() + "'");
            	// dumpList(R, "R= ");
            					

				// for (Y = X.OUTGOINGARCS().FIRST(); Y.ISVALID(); ++Y)
				
				Y = X.getOutgoingArcs();
				while (Y.hasNext()) {
					tmparc = Y.next();
	            	// LoggerManager.debug(Constants.STRUCT_LOGGER, 
	            	//		"Checking Arc( '" + tmparc.getSource().getPetriNetNode().getId() + 
	            	//		"'->'" + tmparc.getTarget().getPetriNetNode().getId() +"', Current Flow=" + tmparc.getFlow() + ")" );

					// Überprüfe alle abgehenden Kanten
					if ((tmparc.getFlow() < tmparc.getCapacity()) && !tmparc.getTarget().isVisited()) {

						tmparc.getTarget().setPredecessor(X);
						tmparc.getTarget().setForeward(true);
						tmparc.getTarget().setZ(
								Math.min(tmparc.getCapacity()
										- tmparc.getFlow(), X.getZ()));

						tmparc.getTarget().setVisited(true);
						R.addLast(tmparc.getTarget());
						
		            	// LoggerManager.debug(Constants.STRUCT_LOGGER, "Adding node Y.END()='" + tmparc.getTarget().getPetriNetNode().getId() + "'");
						
					}
				}
				// for (Y = X.INCOMINGARCS().FIRST(); Y.ISVALID(); ++Y) {
				Y = X.getIncomingArcs();
				while (Y.hasNext()) {
					tmparc = Y.next();

					// Überprüfe alle eingehenden Kanten
					if ((tmparc.getFlow() > 0) && !tmparc.getSource().isVisited()) {
						// if ((Y.FLOW > 0) && (!S.CONTAINS(Y))) {
						tmparc.getSource().setPredecessor(X);
						tmparc.getSource().setForeward(false);
						tmparc.getSource().setZ(
								Math.min(tmparc.getFlow(), X.getZ()));
						tmparc.getSource().setVisited(true);
						R.addLast(tmparc.getSource());
						
		            	// LoggerManager.debug(Constants.STRUCT_LOGGER, "Adding node Y.START()='" + tmparc.getSource().getPetriNetNode().getId() + "'");						
					}

				}
				SINKFOUND = sink.isVisited();
				done = R.isEmpty(); // Abbruchbedingung überprüfen
			}
			//resetTempData();

			if (SINKFOUND) {
            	// LoggerManager.debug(Constants.STRUCT_LOGGER, "Applying flow changes");						
				
				// for (Y = SINK; Y != null; Y = Y.PREDECESSOR())
				FlowNode test = sink;
				while (test.getPredecessor() != null ) {

					// Fluss optimieren, ausgehend von Senke
					if (test.isForeward() && (test.getPredecessor() != null)){
					    test.getPredecessor().getArcTo(test).addFlow(sink.getZ());
		            	// LoggerManager.debug(Constants.STRUCT_LOGGER, "{(" +
		            	//		test.getPredecessor().getArcTo(test).getSource().getPetriNetNode().getNameValue() 
		            	//		+ "," +
		            	//		test.getPredecessor().getArcTo(test).getTarget().getPetriNetNode().getNameValue()
		            	//		+ "): " + test.getPredecessor().getArcTo(test).getFlow());		            							    
					}

					if (!test.isForeward() && (test.getPredecessor() != null)){
					    test.getArcTo(test.getPredecessor()).subFlow(sink.getZ());
					    
		            	// LoggerManager.debug(Constants.STRUCT_LOGGER, "{(" +
		            	//		test.getArcTo(test.getPredecessor()).getSource().getPetriNetNode().getNameValue() 
		            	//		+ "," +
		            	//		test.getArcTo(test.getPredecessor()).getTarget().getPetriNetNode().getNameValue()
		            	//		+ "): " + test.getArcTo(test.getPredecessor()).getFlow());
					    
					}
					test = test.getPredecessor();					
				}
            	// LoggerManager.debug(Constants.STRUCT_LOGGER, "Found a SINK, starting over");						
				
			}			
			
		}
		int countIncoming = 0, countOutgoing = 0;
		
		for (Iterator<FlowArc> i=source.getIncomingArcs();i.hasNext();)
			countIncoming+=i.next().getFlow();
		for (Iterator<FlowArc> i=source.getOutgoingArcs();i.hasNext();)
			countOutgoing+=i.next().getFlow();
		

		// Eingehende - ausgehende = Ergebnis und return-Wert

		int maxFlow = countOutgoing-countIncoming;
    	LoggerManager.debug(Constants.STRUCT_LOGGER, "Resulting flow = " + maxFlow);						
		
	 		
		return maxFlow;
	}

	private void resetTempData() {
		Iterator<FlowNode> m_netNodesIter = getM_netNodesIter();
		FlowNode currentNode = null;

		// Fluss für alle Kanten auf 0 setzen
		while (m_netNodesIter.hasNext()) {
			// z zurücksetzen
			currentNode = m_netNodesIter.next();
			currentNode.setZ(Integer.MAX_VALUE);

			// predecessor zurücksetzen
			currentNode.setPredecessor(null);

			// isForward zurücksetzen
			currentNode.setForeward(false);

			// Kanten zurücksetzen
			Iterator<FlowArc> arcIter = currentNode.getOutgoingArcs();
			while (arcIter.hasNext()) {
				arcIter.next().setFlow(0);
			}
		}
	}

}
