package org.woped.qualanalysis.coverabilitygraph.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewFactory;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.AbstractLowLevelPetriNetBuilder;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

public class ReachabilityGraphModelUsingMarkingNet extends AbstractReachabilityGraphModel {
    private IMarkingNet markingNet;
    private HashSet<DefaultGraphCell> cellsList;

    public ReachabilityGraphModelUsingMarkingNet(IEditor editor) {
        this(editor, new CoverabilityGraphViewFactory());
    }

    public ReachabilityGraphModelUsingMarkingNet(IEditor editor, CoverabilityGraphViewFactory viewFactory) {
        super(editor, viewFactory);
        initialize();

        computeReachabilityGraph();
    }

    @Override
    public String getLegendByID() {
        return markingNet.placesToStringId();
    }

    @Override
    public String getLegendByName() {
        return markingNet.placesToStringName();
    }

    public void reset() {
        clearGraph();
        computeReachabilityGraph();
        applyLayout();
    }

    public IMarkingNet getMarkingNet() {
        return markingNet;
    }

    private void computeReachabilityGraph() {
        AbstractLowLevelPetriNetBuilder builder = BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(getEditor());
        LowLevelPetriNet lowLevelPetriNet = builder.getLowLevelPetriNet();
        markingNet = BuilderFactory.createMarkingNet(lowLevelPetriNet);
        transformMarkingNet2ReachabilityJGraph();
    }

    private void transformMarkingNet2ReachabilityJGraph() {
        cellsList = new HashSet<>();

        for (IMarking marking : markingNet.getMarkings()) {
            cellsList.add(getPlace(marking));
        }

        for (IMarking marking : markingNet.getMarkings()) {
            for (Arc arc : marking.getSuccessors()) {
                getChildNode(arc, marking);
            }
        }

        combineParallelEdges();
        getView().insert(cellsList.toArray());
    }

    private void combineParallelEdges() {
        Collection<CoverabilityGraphEdge> edges = new ArrayList<>();
        for (DefaultGraphCell cell : cellsList) {
            if (cell instanceof CoverabilityGraphEdge) edges.add((CoverabilityGraphEdge) cell);
        }

        CoverabilityGraphEdge[] graphEdges = edges.toArray(new CoverabilityGraphEdge[0]);
        for (int i = 0; i < edges.size() - 1; i++) {
            CoverabilityGraphEdge current = graphEdges[i];
            boolean combined = false;
            for (int j = i + 1; j < graphEdges.length && !combined; j++) {
                CoverabilityGraphEdge other = graphEdges[j];

                if (current.getSourceNode().equals(other.getSourceNode()) && current.getTargetNode().equals(other.getTargetNode())) {
                    other.addTriggers(current.getTriggers());
                    combined = true;
                    cellsList.remove(current);
                }
            }
        }
    }

    private void getChildNode(Arc arc, IMarking parentMarking) {
        IMarking marking = arc.getTarget();
        CoverabilityGraphEdge edge = getEdge(arc, parentMarking);

        CoverabilityGraphNode src = getPlace(parentMarking);
        cellsList.add(src);

        DefaultGraphCell tar = getPlace(marking);
        cellsList.add(tar);

        edge.setSource(src.getChildAt(0));
        edge.setTarget(tar.getChildAt(0));
        cellsList.add(edge);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private CoverabilityGraphEdge getEdge(Arc arc, IMarking parentMarking) {
        CoverabilityGraphEdge returnEdge = new CoverabilityGraphEdge(arc.getTrigger());
        int differ = 1;
        if (cellsList != null && !cellsList.isEmpty()) {
            for (DefaultGraphCell cell : cellsList) {
                if (cell instanceof CoverabilityGraphEdge) {
                    CoverabilityGraphEdge edge = (CoverabilityGraphEdge) cell;
                    // Source
                    CoverabilityGraphPort srcRpom = (CoverabilityGraphPort) edge.getSource();
                    CoverabilityGraphNode srcRplm = (CoverabilityGraphNode) srcRpom.getParent();
                    // Target
                    CoverabilityGraphPort tarRpom = (CoverabilityGraphPort) edge.getTarget();
                    CoverabilityGraphNode tarRplm = (CoverabilityGraphNode) tarRpom.getParent();
                    if (parentMarking == srcRplm.getUserObject() && arc.getTarget() == tarRplm.getUserObject()) {
                        GraphConstants.setLabelPosition(edge.getAttributes(), new Point2D.Double(
                                GraphConstants.PERMILLE * (6 - (0.6 * differ++)) / 8, -20));
                    }
                }
            }
        }
        return returnEdge;
    }

    private CoverabilityGraphNode getPlace(IMarking marking) {
        for (DefaultGraphCell cell : cellsList) {
            if (cell instanceof CoverabilityGraphNode && ((CoverabilityGraphNode) cell).getMarking().equals(marking)) {
                return (CoverabilityGraphNode) cell;
            }
        }

        CoverabilityGraphNode place = new CoverabilityGraphNode(marking);
        GraphConstants.setBounds(place.getAttributes(), new Rectangle2D.Double(0, 0, 80, 20));
        CoverabilityGraphPort port = new CoverabilityGraphPort();
        place.add(port);
        port.setParent(place);
        return place;
    }
}
