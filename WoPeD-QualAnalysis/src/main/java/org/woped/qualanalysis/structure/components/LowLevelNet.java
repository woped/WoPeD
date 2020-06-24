package org.woped.qualanalysis.structure.components;

import java.util.Iterator;
import java.util.LinkedList;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

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

    public FlowNode getNodeForElement(AbstractPetriNetElementModel petrinetElement, boolean first) {
        // get matching FlowNode element

        FlowNode returnValue = null;
        Iterator<FlowNode> i = m_netNodes.iterator();

        while (i.hasNext()) {
            FlowNode currentFlowNode = i.next();

            if (currentFlowNode.getPetriNetNode() != null) {
                if (currentFlowNode.getPetriNetNode().equals(petrinetElement) && (currentFlowNode.isFirst() == first)) {
                    returnValue = currentFlowNode;
                }
            }
        }
        return returnValue;
    }

    public void dumpList(LinkedList<FlowNode> list, String pref) {
        StringBuffer out = new StringBuffer(pref);
        for (Iterator<FlowNode> i = list.iterator(); i.hasNext();) {
            FlowNode next = i.next();
            out.append(next.getPetriNetNode().getNameValue() + ",");
        }

        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, out.toString());

    }

    public int getMaxFlow(FlowNode source, FlowNode sink) {
        boolean done = false;
        boolean SINKFOUND = false;

        resetTempData();

        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "Calculating flow from '" + source.getPetriNetNode().getId()
                + "' to '" + sink.getPetriNetNode().getId() + "'");

        while (!done) {
            // Reset visited flag
            for (Iterator<FlowNode> i = m_netNodes.iterator(); i.hasNext();) {
                i.next().setVisited(false);
            }

            source.setVisited(true);
            LinkedList<FlowNode> R = new LinkedList<FlowNode>();
            R.add(source);
            source.setZ(Integer.MAX_VALUE);
            source.setPredecessor(null);
            SINKFOUND = false;
            Iterator<FlowArc> Y = null;
            FlowNode X = null;
            FlowArc temp_arc = null;

            while ((!SINKFOUND) && (!done)) {
                X = R.getFirst();
                R.removeFirst();

                // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "Removing node X='" + X.getPetriNetNode().getId() + "'");
                // dumpList(R, "R= ");

                // work on all outgoing arcs based on current source
                Y = X.getOutgoingArcs();
                while (Y.hasNext()) {
                    temp_arc = Y.next();
                    // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER,
                    // "Checking Arc( '" + temp_arc.getSource().getPetriNetNode().getId() +
                    // "'->'" + temp_arc.getTarget().getPetriNetNode().getId() +"', Current Flow=" + temp_arc.getFlow() + ")" );

                    if ((temp_arc.getFlow() < temp_arc.getCapacity()) && !temp_arc.getTarget().isVisited()) {

                        temp_arc.getTarget().setPredecessor(X);
                        temp_arc.getTarget().setForeward(true);
                        temp_arc.getTarget().setZ(Math.min(temp_arc.getCapacity() - temp_arc.getFlow(), X.getZ()));

                        temp_arc.getTarget().setVisited(true);
                        R.addLast(temp_arc.getTarget());

                        // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "Adding node Y.END()='" + temp_arc.getTarget().getPetriNetNode().getId() + "'");
                    }
                }

                // work on all incoming arcs based on current source
                Y = X.getIncomingArcs();
                while (Y.hasNext()) {
                    temp_arc = Y.next();

                    if ((temp_arc.getFlow() > 0) && !temp_arc.getSource().isVisited()) {
                        temp_arc.getSource().setPredecessor(X);
                        temp_arc.getSource().setForeward(false);
                        temp_arc.getSource().setZ(Math.min(temp_arc.getFlow(), X.getZ()));
                        temp_arc.getSource().setVisited(true);
                        R.addLast(temp_arc.getSource());

                        // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "Adding node Y.START()='" + temp_arc.getSource().getPetriNetNode().getId() + "'");
                    }

                }
                SINKFOUND = sink.isVisited();
                done = R.isEmpty(); // Abbruchbedingung überprüfen
            }
            // resetTempData();

            if (SINKFOUND) {
                // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "Applying flow changes");

                FlowNode temp_sink = sink;
                while (temp_sink.getPredecessor() != null) {

                    if (temp_sink.isForeward() && (temp_sink.getPredecessor() != null)) {
                        temp_sink.getPredecessor().getArcTo(temp_sink).addFlow(sink.getZ());
                        // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "{(" +
                        // temp_sink.getPredecessor().getArcTo(temp_sink).getSource().getPetriNetNode().getNameValue()
                        // + "," +
                        // temp_sink.getPredecessor().getArcTo(temp_sink).getTarget().getPetriNetNode().getNameValue()
                        // + "): " + temp_sink.getPredecessor().getArcTo(temp_sink).getFlow());
                    }

                    if (!temp_sink.isForeward() && (temp_sink.getPredecessor() != null)) {
                        temp_sink.getArcTo(temp_sink.getPredecessor()).subFlow(sink.getZ());

                        // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "{(" +
                        // temp_sink.getArcTo(temp_sink.getPredecessor()).getSource().getPetriNetNode().getNameValue()
                        // + "," +
                        // temp_sink.getArcTo(temp_sink.getPredecessor()).getTarget().getPetriNetNode().getNameValue()
                        // + "): " + temp_sink.getArcTo(temp_sink.getPredecessor()).getFlow());

                    }
                    temp_sink = temp_sink.getPredecessor();
                }
                // LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "Found a SINK, starting over");

            }

        }
        int countIncoming = 0, countOutgoing = 0;

        // count incomming arcs
        for (Iterator<FlowArc> i = source.getIncomingArcs(); i.hasNext();) {
            countIncoming += i.next().getFlow();
        }

        // count outgoing arcs
        for (Iterator<FlowArc> i = source.getOutgoingArcs(); i.hasNext();) {
            countOutgoing += i.next().getFlow();
        }

        // calculate maxmimum flow
        int maxFlow = countOutgoing - countIncoming;
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "Resulting flow = " + maxFlow);

        return maxFlow;
    }

    private void resetTempData() {
        Iterator<FlowNode> m_netNodesIter = getM_netNodesIter();
        FlowNode currentNode = null;

        while (m_netNodesIter.hasNext()) {
            // reset z
            currentNode = m_netNodesIter.next();
            currentNode.setZ(Integer.MAX_VALUE);

            // reset predecessor
            currentNode.setPredecessor(null);

            // reset isForward
            currentNode.setForeward(false);

            // reset Arcs
            Iterator<FlowArc> arcIter = currentNode.getOutgoingArcs();
            while (arcIter.hasNext()) {
                arcIter.next().setFlow(0);
            }
        }
    }

}
