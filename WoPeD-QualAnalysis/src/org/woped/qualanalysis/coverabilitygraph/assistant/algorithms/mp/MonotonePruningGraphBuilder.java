package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphAdapter;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphListener;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.MonotonePruningAnalysisAdapter;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.NodeRelatedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeState;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.AbstractLowLevelPetriNetBuilder;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

import java.util.*;

/**
 * This class implements the algorithm from the paper Minimal Coverability Set for Petri Nets: Karp and Miller Algorithm with Pruning
 */
class MonotonePruningGraphBuilder {

    private Set<MpNode> unprocessedNodes;
    private Set<MpNode> activeNodes;
    private Set<MpNode> inactiveNodes;

    private IEditor editor;
    private MonotonePruningEventTrigger eventTrigger;

    private IMarkingNet markingNet;
    private int steps;
    private MpNode currentNode;
    private boolean analysisFinished;

    MonotonePruningGraphBuilder(IEditor editor, MonotonePruningEventTrigger eventTrigger) {
        this.editor = editor;
        this.eventTrigger = eventTrigger;

        CoverabilityGraphListener graphListener = new GraphListener();
        eventTrigger.addGraphListener(graphListener);

        AnalysisListener analysisListener = new AnalysisListener();
        eventTrigger.addAnalysisListener(analysisListener);
    }

    public void initialize() {
        createMarkingNet();

        this.unprocessedNodes = new HashSet<>();
        this.activeNodes = new HashSet<>();
        this.inactiveNodes = new HashSet<>();
        this.steps = 0;

        addRootNode();
    }

    private void createMarkingNet() {
        AbstractLowLevelPetriNetBuilder converter = BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(editor);
        ILowLevelPetriNet lowLevelPetriNet = converter.getLowLevelPetriNet();
        this.markingNet = new MarkingNet(lowLevelPetriNet);
    }

    private void addRootNode() {
        IMarking initialMarking = markingNet.getInitialMarking();
        initialMarking.setInitial(true);

        MpNode root = new MpNode(initialMarking);

        unprocessedNodes.add(root);
        this.eventTrigger.fireNodeAddedEvent(root);
    }

    private void reset() {
        initialize();
        eventTrigger.fireRefreshRequest();
    }

    private void selectNode(MpNode node) {
        this.eventTrigger.fireNodeRelatedEvent(node, NodeRelatedEvent.Relation.CURRENTLY_PROCESSED);
        this.eventTrigger.fireRefreshRequest();
    }

    private void processNode(MpNode node) {

        currentNode = node;

        analyseNode();

        selectNode(currentNode);
    }

    private void convert2Graph() {
        if(!unprocessedNodes.isEmpty()) return;
        for (MpNode n : inactiveNodes) {

            MpNode parentNode = n.getParentNode();
            MpNode coveringNode = null;

            if (parentNode == null || parentNode.getState() == MpNodeState.ACTIVE) {

                for (MpNode m : activeNodes) {
                    if (n.getMarking().lessOrEqual(m.getMarking())) {
                        coveringNode = m;
                        break;
                    }
                }

                Collection<CoverabilityGraphEdge> incomingEdges = n.getIncomingEdges();
                for (CoverabilityGraphEdge e : incomingEdges) {
                    eventTrigger.fireEdgeReconnectedEvent(e, n, coveringNode);
                }

                if (coveringNode != null && n.getMarking().isInitial()) {
                    coveringNode.getMarking().setInitial(true);
                }
            }

            this.eventTrigger.fireNodeRemovedEvent(n);
        }

        for (MpNode n : activeNodes) {
            cleanUpEdges(n);
        }

        this.eventTrigger.fireCoverabilityGraphCompletedEvent();
        this.eventTrigger.fireRefreshRequest();
    }

    private void cleanUpEdges(MpNode n) {

        CoverabilityGraphEdge[] edges = n.getEdges().toArray(new CoverabilityGraphEdge[1]);

        for (int i = 0; i < edges.length - 1; i++) {
            CoverabilityGraphEdge currentEdge = edges[i];

            for (int j = i + 1; j < edges.length; j++) {
                CoverabilityGraphEdge otherEdge = edges[j];

                if (currentEdge.isParallel(otherEdge)) {
                    if (!currentEdge.toString().equals(otherEdge.toString())) {
                        for (TransitionNode t : currentEdge.getTriggers()) {
                            otherEdge.addTrigger(t);
                        }
                    }

                    eventTrigger.fireEdgeRemovedEvent(currentEdge);
                    break;
                }
            }
        }
    }

    private void analyseNode() {

        analysisFinished = false;

        doUnprocessedCheck();
        if (analysisFinished) return;

        doParentActiveCheck();
        if (analysisFinished) return;

        doOmegaCheck();

        doCoverCheck();
        if (analysisFinished) return;

        doDeactivationCheck();

        doDescendantsCheck();
    }

    private void doUnprocessedCheck() {
        if (unprocessedNodes.contains(currentNode)) {
            currentNode.setProcessedInStep(++steps);
            unprocessedNodes.remove(currentNode);

            eventTrigger.fireNodeProcessingStarted(currentNode);
        } else {
            finishAnalysis(false);
        }
    }

    private void doParentActiveCheck() {
        MpNode parentNode = currentNode.getParentNode();
        boolean parentActive = (parentNode == null) || activeNodes.contains(parentNode);

        eventTrigger.fireParentCheckCompletedEvent(parentNode, parentActive);

        if (!parentActive) {
            deactivateNode(currentNode);
            finishAnalysis();
        }
    }

    private void doOmegaCheck() {
        Set<MpNode> activeAncestors = new HashSet<>();
        activeAncestors.addAll(currentNode.getAncestors());
        activeAncestors.retainAll(activeNodes);
        Set<MpNode> smallerNodes = new HashSet<>();

        IMarking previousMarking = currentNode.getMarking();
        IMarking newMarking = previousMarking.copy();
        if (currentNode.getParentNode() == null) newMarking.setInitial(true);

        // detect smaller nodes
        for (MpNode ancestor : activeAncestors) {
            if (ancestor.getMarking().less(newMarking)) {
                smallerNodes.add(ancestor);
                eventTrigger.fireNodeRelatedEvent(ancestor, NodeRelatedEvent.Relation.LESS_THAN_CURRENT);
            }
        }

        // create omega marking
        if (!smallerNodes.isEmpty()) {
            Set<PlaceNode> places = newMarking.getPlaces();
            for (PlaceNode place : places) {
                if (newMarking.isPlaceUnbound(place)) continue;

                for (MpNode ancestor : smallerNodes) {
                    if (newMarking.getTokens(place) > ancestor.getMarking().getTokens(place))
                        newMarking.setPlaceUnbound(place, true);
                }
            }
        }

        eventTrigger.fireOmegaCheckCompletedEvent(previousMarking, newMarking, smallerNodes);
        currentNode.setMarking(newMarking);
    }

    private void doCoverCheck() {

        Set<MpNode> largerNodes = new HashSet<>();
        for (MpNode activeNode : activeNodes) {
            if (currentNode.getMarking().lessOrEqual(activeNode.getMarking())) {
                largerNodes.add(activeNode);
                eventTrigger.fireNodeRelatedEvent(activeNode, NodeRelatedEvent.Relation.LARGER_THAN_CURRENT);
            }
        }

        eventTrigger.fireCoverCheckCompletedEvent(largerNodes);

        if (!largerNodes.isEmpty()) {
            deactivateNode(currentNode);
            finishAnalysis();
        }
    }

    private void doDeactivationCheck() {

        Map<MpNode, MpNode> nodes2Remove = new HashMap<>();
        for (MpNode n : activeNodes) {
            for (MpNode ancestor : n.getAncestors()) {
                if (ancestor.getMarking().lessOrEqual(currentNode.getMarking()) &&
                        (activeNodes.contains(ancestor) || !currentNode.getAncestors().contains(ancestor))) {
                    nodes2Remove.put(n, ancestor);
                }
            }
        }

        for (MpNode n : nodes2Remove.keySet()) {
            activeNodes.remove(n);
            deactivateNode(n, nodes2Remove.get(n));
        }

        eventTrigger.fireDeactivationCheckCompleted(nodes2Remove.keySet());
    }

    private void doDescendantsCheck() {

        IMarking marking = currentNode.getMarking();
        TransitionNode[] activatedTransitions = markingNet.getActivatedTransitions(marking);
        for (TransitionNode transition : activatedTransitions) {
            IMarking m = markingNet.calculateSucceedingMarking(marking, transition);
            marking.addSuccessor(new Arc(m, transition));
            MpNode n = new MpNode(m);
            this.unprocessedNodes.add(n);
            CoverabilityGraphEdge edge = new CoverabilityGraphEdge(currentNode, n, transition);

            this.eventTrigger.fireNodeAddedEvent(n);
            this.eventTrigger.fireEdgeAddedEvent(edge);
        }

        activeNodes.add(currentNode);
        currentNode.setState(MpNodeState.ACTIVE);

        eventTrigger.fireDescendantsCheckCompletedEvent(Arrays.asList(activatedTransitions));
        finishAnalysis();
    }

    private void finishAnalysis() {
        finishAnalysis(true);
    }

    private void finishAnalysis(boolean fireEvent) {
        analysisFinished = true;
        if (fireEvent) eventTrigger.fireNodeProcessingFinishedEvent(currentNode);

        if (unprocessedNodes.isEmpty()) {
            eventTrigger.fireCoverabilityTreeCompletedEvent();
        }
    }

    private void deactivateNode(MpNode node) {
        deactivateNode(node, currentNode);
    }

    private void deactivateNode(MpNode node, MpNode deactivationNode) {
        inactiveNodes.add(node);
        node.setDeactivatedInStep(steps);
        node.setDeactivationNode(deactivationNode);
        node.setState(MpNodeState.INACTIVE);

        eventTrigger.fireNodeRelatedEvent(deactivationNode, NodeRelatedEvent.Relation.LESS_THAN_CURRENT);
        eventTrigger.fireNodeRelatedEvent(node, NodeRelatedEvent.Relation.DEACTIVATED);
    }

    private class GraphListener extends CoverabilityGraphAdapter{
        @Override
        public void restartRequested() {
            reset();
        }
    }

    private class AnalysisListener extends MonotonePruningAnalysisAdapter {

        @Override
        public void nodeProcessingRequested(NodeEvent event) {
            processNode((MpNode) event.getNode());
        }

        @Override
        public void convert2GraphRequest() {
            convert2Graph();
        }

        @Override
        public void processRandomNodeRequest() {
            if (unprocessedNodes.isEmpty()) return;
            processNode(unprocessedNodes.iterator().next());
        }
    }
}
