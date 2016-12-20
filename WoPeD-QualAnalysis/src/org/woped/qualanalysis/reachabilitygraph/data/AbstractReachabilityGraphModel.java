package org.woped.qualanalysis.reachabilitygraph.data;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.reachabilitygraph.controller.ParallelRouter;
import org.woped.qualanalysis.reachabilitygraph.controller.ReachabilityCellListener;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;
import org.woped.qualanalysis.reachabilitygraph.gui.layout.CircularLayout;
import org.woped.qualanalysis.reachabilitygraph.gui.layout.CoverabilityGraphLayout;
import org.woped.qualanalysis.reachabilitygraph.gui.layout.HierarchicLayout;
import org.woped.qualanalysis.reachabilitygraph.gui.layout.tree.TreeLayout;
import org.woped.qualanalysis.reachabilitygraph.gui.views.ReachabilityGraphViewFactory;
import org.woped.qualanalysis.soundness.marking.IMarking;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class AbstractReachabilityGraphModel implements IReachabilityGraphModel {

    protected ReachabilityJGraph graph;
    protected GraphLayoutCache view;
    protected IEditor editor;
    protected GraphModel model;

    public AbstractReachabilityGraphModel(IEditor editor) {
        this.editor = editor;
    }

    public static ReachabilityJGraph layoutGraph(ReachabilityJGraph graph, CoverabilityGraphLayout layout, Dimension dim) {
        switch (layout) {
            case HIERARCHIC:
                return new HierarchicLayout().layoutGraph(graph, dim);
            case CIRCULAR:
                return new CircularLayout().layoutGraph(graph, dim);
            case TREE:
                return new TreeLayout().layoutGraph(graph, dim);
            default:
                return null;
        }
    }

    public static void setParallelRouting(ReachabilityJGraph graph, boolean enabled) {
        Object[] nodes = graph.getRoots();
        HashMap<ReachabilityEdgeModel, AttributeMap> edit = new HashMap<ReachabilityEdgeModel, AttributeMap>();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof ReachabilityEdgeModel) {
                ReachabilityEdgeModel edge = (ReachabilityEdgeModel) nodes[i];
                if (enabled) {
                    GraphConstants.setRouting(edge.getAttributes(), ParallelRouter.getSharedInstance(graph
                            .getGraphLayoutCache()));
                } else {
                    GraphConstants.setRouting(edge.getAttributes(), GraphConstants.ROUTING_DEFAULT);
                }
                edit.put(edge, edge.getAttributes());
            }
        }
        graph.getGraphLayoutCache().edit(edit, null, null, null);
    }

    public static void setGrayScale(ReachabilityJGraph graph, boolean enabled) {
        Object[] nodes = graph.getRoots();
        HashMap<ReachabilityPlaceModel, AttributeMap> edit = new HashMap<ReachabilityPlaceModel, AttributeMap>();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof ReachabilityPlaceModel) {
                ReachabilityPlaceModel place = (ReachabilityPlaceModel) nodes[i];
                place.setGrayscaled(enabled);
                edit.put(place, place.getAttributes());
            }
        }
        graph.getGraphLayoutCache().edit(edit, null, null, null);
        graph.getGraphLayoutCache().reload();
    }

    public static int edgeCount(JGraph graph) {
        Object[] roots = graph.getRoots();
        int edges = 0;
        for (int i = 0; i < roots.length; i++) {
            if (roots[i] instanceof ReachabilityEdgeModel) {
                edges++;
            }
        }
        return edges;
    }

    public static int verticeCount(JGraph graph) {
        Object[] roots = graph.getRoots();
        int vertices = 0;
        for (int i = 0; i < roots.length; i++) {
            if (roots[i] instanceof ReachabilityPlaceModel) {
                vertices++;
            }
        }
        return vertices;
    }

    public static ReachabilityPlaceModel lookupInitialMarking(Collection<ReachabilityPlaceModel> markings) {
        Iterator<ReachabilityPlaceModel> markingsIter = markings.iterator();
        while (markingsIter.hasNext()) {
            ReachabilityPlaceModel actPlaceModel = markingsIter.next();
            if (((IMarking) actPlaceModel.getUserObject()).isInitial()) {
                return actPlaceModel;
            }
        }
        return null;
    }

    public static Collection<ReachabilityPlaceModel> getPlaces(ReachabilityJGraph graph) {
        GraphModel model = graph.getModel();

        List<ReachabilityPlaceModel> places = new LinkedList<>();
        for (int i = 0; i < model.getRootCount(); i++) {
            Object node = model.getRootAt(i);
            if (node instanceof ReachabilityPlaceModel) places.add((ReachabilityPlaceModel) node);
        }

        return places;
    }

    protected void init() {
        model = new DefaultGraphModel();
        view = new GraphLayoutCache(model, new ReachabilityGraphViewFactory());
        view.setAutoSizeOnValueChange(true);
        view.setSelectsAllInsertedCells(false);
        graph = new ReachabilityJGraph(model, view);

        ReachabilityCellListener mouseListener = new ReachabilityCellListener(graph, editor);
        graph.addMouseListener(mouseListener);
        graph.addMouseWheelListener(mouseListener);
    }

}
