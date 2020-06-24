package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.MonotonePruningAnalysisAdapter;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.NodeChangedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.NodeChangedEventListener;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.NodeRelatedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeState;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeTextFormatter;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphAdapter;
import org.woped.qualanalysis.coverabilitygraph.events.EdgeEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.DefaultNodeTextFormatter;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.NodeFormatter;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphPort;


/**
 * This class is responsible to set the visual appearance of the elements of the coverability graph.
 */
class MonotonePruningVisualStateManager {

    private static Color LIGHT_GRAY = new Color(245, 245, 245);
    private static Color GRAY = new Color(150, 150, 150);
    private static Color DARK_GRAY = new Color(100, 100, 100);
    private static Color LIGHT_BLUE = new Color(205, 232, 255);
    private static Color DARK_BLUE = new Color(0, 0, 150);
    private static Color LIGHT_RED = new Color(255, 202, 202);
    private static Color DARK_GREEN = new Color(0, 150, 50);
    private static Color LIGHT_GREEN = new Color(180, 255, 180);

    private MonotonePruningEventTrigger eventTrigger;
    private NodeFormatter nodeFormatter;
    private NodeStateListener nodeStateListener;

    private List<MpNode> highlightedNodes;
    private boolean treeCompleted;

    /**
     * Constructs a new visual state manager.
     *
     * @param eventTrigger the object to fire events
     * @param formatter the default formatter for the coverability graph nodes
     */
    MonotonePruningVisualStateManager(MonotonePruningEventTrigger eventTrigger, NodeFormatter formatter) {
        this.eventTrigger = eventTrigger;
        this.highlightedNodes = new LinkedList<>();
        this.treeCompleted = false;

        this.nodeFormatter = formatter;
        nodeFormatter.setNodeTextFormatter(new MpNodeTextFormatter());

        GraphListener graphListener = new GraphListener();
        eventTrigger.addGraphListener(graphListener);

        AnalysisListener analysisListener = new AnalysisListener();
        eventTrigger.addAnalysisListener(analysisListener);

        nodeStateListener = new NodeStateListener();
    }

    /**
     * Removes all applied highlighting from the graph.
     */
    void removeHighlighting() {

        for (MpNode n : highlightedNodes) {
            n.setHighlighted(false);
            applyStyle(n);
        }

        highlightedNodes.clear();
        eventTrigger.fireRefreshRequest();
    }

    private void addHighlighting(MpNode n, AttributeMap style) {
        n.setHighlighted(true);
        highlightedNodes.add(n);

        eventTrigger.fireNodeModifiedEvent(n, style);
    }

    private void registerNode(MpNode node) {
        node.addNodeChangedListener(this.nodeStateListener);
        applyStyle(node);
    }

    private void applyStyle(MpNode node) {

        switch (node.getState()) {
            case UNPROCESSED:
                setUnprocessedStyle(node);
                break;
            case ACTIVE:
                setActiveStyle(node);
                break;
            case INACTIVE:
                setInactiveStyle(node);
                break;
        }
    }

    private void setUnprocessedStyle(MpNode node) {
        eventTrigger.fireNodeModifiedEvent(node, NodeStyle.UNPROCESSED);
    }

    private void setActiveStyle(MpNode node) {
        eventTrigger.fireNodeModifiedEvent(node, NodeStyle.ACTIVE);
        setIncomingEdgeStyle(node, EdgeStyle.ACTIVE);
    }

    private void setInactiveStyle(MpNode node) {
        eventTrigger.fireNodeModifiedEvent(node, NodeStyle.INACTIVE);
        setIncomingEdgeStyle(node, EdgeStyle.ACTIVE);
    }

    private void setIncomingEdgeStyle(CoverabilityGraphNode node, AttributeMap style) {
        for (CoverabilityGraphEdge edge : node.getIncomingEdges()) {
            eventTrigger.fireEdgeModifiedEvent(edge, style);
        }
    }

    private static class NodeStyle {
        static final AttributeMap UNPROCESSED;
        static final AttributeMap ACTIVE;
        static final AttributeMap INACTIVE;
        static final AttributeMap PROCESSING;
        static final AttributeMap LESS_OR_EQUAL;
        static final AttributeMap LARGER_OR_EQUAL;
        static final AttributeMap SELECTED;
        static final AttributeMap DEACTIVATED;

        static {
            UNPROCESSED = createUnprocessedStyle();
            SELECTED = createSelectedStyle();
            ACTIVE = createActiveStyle();
            INACTIVE = createInactiveStyle();
            PROCESSING = createProcessingStyle();
            LESS_OR_EQUAL = createLessOrEqualHighlightingStyle();
            LARGER_OR_EQUAL = createLargerOrEqualStyle();
            DEACTIVATED = createDeactivatedStyle();
        }

        private static AttributeMap createDeactivatedStyle() {

            AttributeMap map = new AttributeMap();

            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN});
            GraphConstants.setLineWidth(map, 2);
            GraphConstants.setForeground(map, GRAY);
            GraphConstants.setLineColor(map, Color.RED);
            GraphConstants.setBackground(map, LIGHT_RED);
            GraphConstants.setOpaque(map, true);

            return map;
        }

        private static AttributeMap createSelectedStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN});
            GraphConstants.setLineWidth(map, 2);
            GraphConstants.setForeground(map, Color.black);
            GraphConstants.setLineColor(map, DARK_BLUE);
            GraphConstants.setBackground(map, LIGHT_BLUE);
            GraphConstants.setOpaque(map, true);

            return map;
        }

        private static AttributeMap createUnprocessedStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setDashPattern(map, new float[]{2, 2});
            GraphConstants.setLineWidth(map, 1);
            GraphConstants.setForeground(map, GRAY);
            GraphConstants.setLineColor(map, GRAY);
            GraphConstants.setOpaque(map, false);

            return map;
        }

        private static AttributeMap createActiveStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN, GraphConstants.BACKGROUND});
            GraphConstants.setLineWidth(map, 2);
            GraphConstants.setForeground(map, Color.black);
            GraphConstants.setLineColor(map, Color.black);
            GraphConstants.setOpaque(map, false);

            return map;
        }

        private static AttributeMap createInactiveStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN});
            GraphConstants.setLineWidth(map, 1);
            GraphConstants.setForeground(map, GRAY);
            GraphConstants.setLineColor(map, GRAY);
            GraphConstants.setBackground(map, LIGHT_GRAY);
            GraphConstants.setOpaque(map, true);

            return map;
        }

        private static AttributeMap createLessOrEqualHighlightingStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN});
            GraphConstants.setLineWidth(map, 2);
            GraphConstants.setForeground(map, GRAY);
            GraphConstants.setLineColor(map, Color.RED);
            GraphConstants.setOpaque(map, true);

            return map;
        }

        private static AttributeMap createLargerOrEqualStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN});
            GraphConstants.setLineWidth(map, 2);
            GraphConstants.setForeground(map, Color.BLACK);
            GraphConstants.setLineColor(map, DARK_GREEN);
            GraphConstants.setBackground(map, LIGHT_GREEN);
            GraphConstants.setOpaque(map, true);

            return map;

        }

        private static AttributeMap createProcessingStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN});
            GraphConstants.setLineWidth(map, 2);
            GraphConstants.setForeground(map, DARK_BLUE);
            GraphConstants.setLineColor(map, DARK_BLUE);
            GraphConstants.setBackground(map, LIGHT_BLUE);
            GraphConstants.setOpaque(map, true);

            return map;
        }
    }

    private static class EdgeStyle {

        static final AttributeMap UNPROCESSED;
        static final AttributeMap ACTIVE;

        static {
            UNPROCESSED = createUnprocessedStyle();
            ACTIVE = createActiveStyle();
        }

        private static AttributeMap createUnprocessedStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setLineColor(map, GRAY);
            GraphConstants.setLineWidth(map, 1);
            GraphConstants.setDashPattern(map, new float[]{1, 2});

            return map;
        }

        private static AttributeMap createActiveStyle() {
            AttributeMap map = new AttributeMap();

            GraphConstants.setLineColor(map, DARK_GRAY);
            GraphConstants.setLineWidth(map, 1);
            GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.DASHPATTERN});

            return map;
        }
    }

    /**
     * Listens for state changes of registered nodes
     */
    private class NodeStateListener implements NodeChangedEventListener{

        @Override
        public void NodeChanged(NodeChangedEvent event) {
            applyStyle(event.getNode());
        }
    }

    /**
     * Listens for graph events.
     */
    private class GraphListener extends CoverabilityGraphAdapter {

        @Override
        public void nodeAdded(NodeEvent event) {
            registerNode((MpNode) event.getNode());
        }

        @Override
        public void edgeAdded(EdgeEvent event) {
            CoverabilityGraphPort port = (CoverabilityGraphPort) event.getEdge().getTarget();
            MpNode target = (MpNode) port.getParent();

            if (target.getState() == MpNodeState.UNPROCESSED) {
                eventTrigger.fireEdgeModifiedEvent(event.getEdge(), EdgeStyle.UNPROCESSED);
            }
        }

        @Override
        public void restartRequested() {
            treeCompleted = false;
            nodeFormatter.setNodeTextFormatter(new MpNodeTextFormatter());
        }
    }

    /**
     * Listens for analysis events.
     */
    private class AnalysisListener extends MonotonePruningAnalysisAdapter {

        @Override
        public void nodeSelected(NodeEvent event) {
            addHighlighting((MpNode) event.getNode(), NodeStyle.SELECTED);
        }

        @Override
        public void nodeRelated(NodeRelatedEvent event) {
            switch (event.getRelation()) {
                case CURRENTLY_PROCESSED:
                    addHighlighting((MpNode) event.getNode(), NodeStyle.PROCESSING);
                    break;
                case DEACTIVATED:
                    addHighlighting((MpNode) event.getNode(), NodeStyle.DEACTIVATED);
                    break;
                case LESS_THAN_CURRENT:
                    addHighlighting((MpNode) event.getNode(), NodeStyle.LESS_OR_EQUAL);
                    break;
                case LARGER_THAN_CURRENT:
                    addHighlighting((MpNode) event.getNode(), NodeStyle.LARGER_OR_EQUAL);
                    break;
                case NONE:
                    applyStyle((MpNode) event.getNode());
                    break;
            }
        }

        @Override
        public void convert2GraphRequest() {
            if (treeCompleted) nodeFormatter.setNodeTextFormatter(new DefaultNodeTextFormatter());
        }

        @Override
        public void coverabilityGraphCompleted() {
            treeCompleted = true;
        }
    }
}
