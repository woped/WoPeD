package org.woped.qualanalysis.coverabilitygraph.model;

import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayoutSettings;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewFactory;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides common functionality for graph models as applying layout or graph initialization.
 */
public abstract class AbstractReachabilityGraphModel implements CoverabilityGraphModel {

    private CoverabilityGraph graph;
    private GraphLayoutCache view;
    private IEditor editor;
    private GraphModel model;
    private CoverabilityGraphViewFactory viewFactory;
    private CoverabilityGraphLayoutSettings layoutSettings;

    /**
     * Gets the root node of the graph.
     * <p>
     * The root node contains the initial marking of the petri net.
     *
     * @param graphNodes the nodes of the graph
     * @return the initial node of the graph or null, if no initial marking exists
     */
    public static CoverabilityGraphNode lookupInitialMarking(Collection<CoverabilityGraphNode> graphNodes) {
        for (CoverabilityGraphNode actPlaceModel : graphNodes) {
            if (actPlaceModel.getMarking().isInitial()) {
                return actPlaceModel;
            }
        }
        return null;
    }

    /**
     * Gets the nodes contained in the provided graph.
     *
     * @param graph the graph that contains the nodes
     * @return the nodes of the graph or an empty list if there are no nodes contained
     */
    public static Collection<CoverabilityGraphNode> getPlaces(CoverabilityGraph graph) {
        List<CoverabilityGraphNode> places = new LinkedList<>();

        GraphModel model = graph.getModel();
        for (int i = 0; i < model.getRootCount(); i++) {
            Object node = model.getRootAt(i);
            if (node instanceof CoverabilityGraphNode) places.add((CoverabilityGraphNode) node);
        }

        return places;
    }

    /**
     * Constructs a new abstract coverability graph model.
     *
     * @param editor      the editor containing the petri net
     * @param viewFactory the view factory to use
     */
    protected AbstractReachabilityGraphModel(IEditor editor, CoverabilityGraphViewFactory viewFactory) {
        this.editor = editor;
        this.viewFactory = viewFactory;
        this.layoutSettings = CoverabilityGraphLayoutSettings.getDefaultSettings();
    }

    @Override
    public CoverabilityGraph getGraph() {
        return graph;
    }

    @Override
    public Collection<CoverabilityGraphNode> getNodes() {
        Collection<CoverabilityGraphNode> nodes = new LinkedList<>();

        for (int i = 0; i < graph.getModel().getRootCount(); i++) {
            Object cell = graph.getModel().getRootAt(i);
            if (cell instanceof CoverabilityGraphNode) {
                nodes.add((CoverabilityGraphNode) cell);
            }
        }

        return nodes;
    }

    @Override
    public Collection<CoverabilityGraphEdge> getEdges() {
        Collection<CoverabilityGraphEdge> edges = new LinkedList<>();

        for (int i = 0; i < graph.getModel().getRootCount(); i++) {
            Object cell = graph.getModel().getRootAt(i);
            if (cell instanceof CoverabilityGraphEdge) {
                edges.add((CoverabilityGraphEdge) cell);
            }
        }

        return edges;
    }

    @Override
    public CoverabilityGraphViewFactory getViewFactory() {
        return viewFactory;
    }

    @Override
    public CoverabilityGraphLayoutSettings getLayoutSettings() {
        return layoutSettings;
    }

    @Override
    public void applyLayout() {
        if (getNodes().isEmpty()) return;
        layoutSettings.layout.applyLayout(this);
    }

    /**
     * Gets the view of the coverability graph.
     *
     * @return the view of the coverability graph
     */
    protected GraphLayoutCache getView() {
        return view;
    }

    /**
     * Gets the editor of the underlying petri net.
     *
     * @return the editor of the underlying petri net
     */
    protected IEditor getEditor() {
        return editor;
    }

    /**
     * Gets the graph model.
     *
     * @return the graph model
     */
    protected GraphModel getModel() {
        return model;
    }

    /**
     * Initializes the graph.
     */
    protected void initialize() {
        model = new DefaultGraphModel();
        view = new GraphLayoutCache(model, viewFactory);
        view.setAutoSizeOnValueChange(true);
        view.setSelectsAllInsertedCells(false);
        graph = new CoverabilityGraph(model, view);

        graph.addComponentListener(new ResizeListener());
    }

    /**
     * Removes all elements from the graph.
     */
    protected void clearGraph(){

        Collection<Object> cells = new LinkedList<>();
        GraphModel model = getModel();

        for(int i = 0; i < model.getRootCount(); i++){
            cells.add(model.getRootAt(i));
        }

        getView().remove(cells.toArray());
    }

    /**
     * Adjusts layout when components size has changed.
     */
    private class ResizeListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            applyLayout();
        }
    }
}
