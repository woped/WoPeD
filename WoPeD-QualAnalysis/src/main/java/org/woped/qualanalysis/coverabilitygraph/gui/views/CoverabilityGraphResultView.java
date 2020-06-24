package org.woped.qualanalysis.coverabilitygraph.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseAdapter;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseListener;
import org.woped.qualanalysis.coverabilitygraph.events.EmptySpaceClickedEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeClickedEvent;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphSettings;
import org.woped.qualanalysis.coverabilitygraph.gui.PetriNetHighlighter;
import org.woped.qualanalysis.coverabilitygraph.gui.ZoomController;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayoutSettings;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.NodeFormatter;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphModel;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.model.ReachabilityGraphModelUsingMarkingNet;

/**
 * This view displays the resulting coverability graph for the petri net.
 */
public class CoverabilityGraphResultView extends CoverabilityGraphView {

    public final static String VIEW_NAME = "ResultView";

    private IEditor editor;
    private CoverabilityGraphModel graphModel;
    private StatusBarView statusBar;
    private CoverabilityGraphWrapper graphView;

    private NodeFormatter nodeFormatter;
    private PetriNetHighlighter netHighlighter;
    private EdgeHighlighter edgeHighlighter;

    /**
     * Constructs a new result view for the coverability graph of the given petri net.
     *
     * @param editor the editor of the petri net
     */
    public CoverabilityGraphResultView(IEditor editor) {
        super();
        this.editor = editor;
        initialize();
    }

    /**
     * Recomputes the coverability graph and refreshes the view.
     */
    @Override
    public void refresh() {
        graphModel.applyLayout();
        statusBar.refresh();
        graphView.refresh();

        this.validate();
        this.repaint();
    }

    @Override
    public void reset() {
        graphModel.reset();
        statusBar.setOutOfSync(false);
        refresh();
    }

    /**
     * Notifies the view that it is out of sync with the petri net.
     */
    @Override
    public void setOutOfSync() {
        statusBar.setOutOfSync(true);
        refresh();
    }

    /**
     * Gets the current settings of the view.
     *
     * @return the current settings of the view
     */
    @Override
    public CoverabilityGraphSettings getSettings() {
        CoverabilityGraphSettings settings = new CoverabilityGraphSettings();

        settings.markingFormatter = nodeFormatter.getNodeTextFormatter().getMarkingFormatter();
        settings.colorScheme = nodeFormatter.getColorScheme();
        settings.colorSchemeSupported = true;

        CoverabilityGraphLayoutSettings layoutSettings = graphModel.getLayoutSettings();
        settings.layout = layoutSettings.layout;
        settings.edgeRouting = layoutSettings.edgeRouting;
        settings.minNodeSize = layoutSettings.minNodeSize;
        settings.horizontalGap = layoutSettings.horizontalGap;
        settings.verticalGap = layoutSettings.verticalGap;
        return settings;
    }

    /**
     * Applies the provided settings to the view.
     *
     * @param settings the new settings
     */
    @Override
    public void applySettings(CoverabilityGraphSettings settings) {

        this.nodeFormatter.getNodeTextFormatter().setMarkingFormatter(settings.markingFormatter);
        this.nodeFormatter.setColorScheme(settings.colorScheme);

        CoverabilityGraphLayoutSettings layoutSettings = graphModel.getLayoutSettings();
        layoutSettings.layout = settings.layout;
        layoutSettings.edgeRouting = settings.edgeRouting;
        layoutSettings.minNodeSize = settings.minNodeSize;
        layoutSettings.horizontalGap = settings.horizontalGap;
        layoutSettings.verticalGap = settings.verticalGap;

        refresh();
    }

    /**
     * Gets the zoom controller of the view.
     *
     * @return the zoom controller of the view
     */
    @Override
    public ZoomController getZoomController() {
        return graphView.getZoomController();
    }

    /**
     * Gets the graph model of the view.
     *
     * @return the graph model of the view
     */
    @Override
    public CoverabilityGraphModel getGraphModel() {
        return graphModel;
    }

    /**
     * Removes all highlighting from the view.
     */
    @Override
    public void removeHighlighting() {
        super.removeHighlighting();
        getGraphModel().getGraph().clearSelection();
        netHighlighter.removeHighlighting();
        edgeHighlighter.removeHighlighting();
    }

    /**
     * Adds a listener that is interested in mouse events from the graph.
     *
     * @param listener the listener to add
     */
    @Override
    public void addCoverabilityGraphMouseListener(CoverabilityGraphMouseListener listener) {
        graphView.addCoverabilityGraphMouseListener(listener);
    }

    private void initialize() {

        CoverabilityGraphViewFactory viewFactory = new CoverabilityGraphViewFactory();
        this.nodeFormatter = viewFactory.getNodeFormatter();
        this.graphModel = new ReachabilityGraphModelUsingMarkingNet(editor, viewFactory);

        createView();

        this.edgeHighlighter = new EdgeHighlighter();
        graphView.addCoverabilityGraphMouseListener(edgeHighlighter);

        this.netHighlighter = new PetriNetHighlighter(editor);
        graphView.addCoverabilityGraphMouseListener(netHighlighter.getMouseListener());

        refresh();
    }

    private void createView() {

        this.statusBar = new StatusBarView(this.graphModel);
        this.graphView = new CoverabilityGraphWrapper(graphModel.getGraph());

        this.setLayout(new BorderLayout());
        this.add(statusBar, BorderLayout.SOUTH);
        this.add(graphView, BorderLayout.CENTER);
    }

    /**
     * Highlights edges based on the graph selection
     */
    private class EdgeHighlighter extends CoverabilityGraphMouseAdapter {

        private final Color INCOMING = new Color(0, 150, 0);
        private final Color OUTGOING = new Color(150, 0, 0);

        Map<CoverabilityGraphEdge, AttributeMap> undoMap;

        EdgeHighlighter() {
            undoMap = new HashMap<>();
        }

        @Override
        public void nodeClicked(NodeClickedEvent event) {
            highlightEdges(event.getNode());
        }

        @Override
        public void emptySpaceClicked(EmptySpaceClickedEvent event) {
            removeHighlighting();
        }

        /**
         * Removes all set edge highlighting.
         */
        void removeHighlighting() {
            getGraphModel().getGraph().getGraphLayoutCache().edit(undoMap);
            undoMap.clear();
        }

        private void highlightEdges(CoverabilityGraphNode node) {
            removeHighlighting();
            highlightIncomingEdges(node);
            highlightOutgoingEdges(node);
        }

        private void highlightIncomingEdges(CoverabilityGraphNode node) {
            AttributeMap highlightAttributes = new AttributeMap();
            GraphConstants.setForeground(highlightAttributes, INCOMING);
            GraphConstants.setLineColor(highlightAttributes, INCOMING);
            GraphConstants.setLineWidth(highlightAttributes, 2);

            setEdgeAttributes(node.getIncomingEdges(), highlightAttributes);
        }

        private void highlightOutgoingEdges(CoverabilityGraphNode node) {
            AttributeMap highlightAttributes = new AttributeMap();
            GraphConstants.setForeground(highlightAttributes, OUTGOING);
            GraphConstants.setLineColor(highlightAttributes, OUTGOING);
            GraphConstants.setLineWidth(highlightAttributes, 2);

            setEdgeAttributes(node.getOutgoingEdges(), highlightAttributes);
        }

        private void setEdgeAttributes(Collection<CoverabilityGraphEdge> edges, AttributeMap highlightAttributes) {
            Map<CoverabilityGraphEdge, AttributeMap> changeMap = new HashMap<>();
            for (CoverabilityGraphEdge edge : edges) {
                AttributeMap undoAttributes = new AttributeMap();
                GraphConstants.setForeground(undoAttributes, GraphConstants.getForeground(edge.getAttributes()));
                GraphConstants.setLineColor(undoAttributes, GraphConstants.getLineColor(edge.getAttributes()));
                GraphConstants.setLineWidth(undoAttributes, GraphConstants.getLineWidth(edge.getAttributes()));

                changeMap.put(edge, highlightAttributes);
                if (!undoMap.containsKey(edge)) undoMap.put(edge, undoAttributes);
            }

            graphModel.getGraph().getGraphLayoutCache().edit(changeMap);
        }
    }
}
