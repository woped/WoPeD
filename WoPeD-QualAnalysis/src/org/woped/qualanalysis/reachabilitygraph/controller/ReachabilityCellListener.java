package org.woped.qualanalysis.reachabilitygraph.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPortModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphPanel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityWarning;
import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * 
 * 
 * @author <a href="mailto:b.joerger@gmx.de">Benjamin Joerger</a>
 * @since 02.01.2009
 * 
 *        Highlight and dehighlight methods are implemented for the reachability graph (markings are highlighted). The corresponding transitions in the petrinet
 *        are also highlighted. The virtual tokens were set in the net.
 * 
 */

public class ReachabilityCellListener implements MouseListener {

    private ReachabilityJGraph graph = null;
    private ReachabilityPlaceModel lastHighlighted = null;
    private ReachabilityEdgeModel lastHighlightedEdge = null;
    private IEditor editor = null;
    private ReachabilityGraphPanel rgp = null;

    public ReachabilityCellListener(ReachabilityJGraph graph, IEditor editor) {
        this.graph = graph;
        this.editor = editor;
    }

    public void mouseClicked(MouseEvent e) {
        if (this.editor.isTokenGameEnabled()) {
            graph.clearSelection();
            return;

        }
        DefaultGraphCell cell = (DefaultGraphCell) graph.getFirstCellForLocation(e.getPoint().x, e.getPoint().y);
        if ((cell == null) && (e.getClickCount() == 2)) {
            deHighlightModel(true);
            rgp.setUnselectButtonEnabled(false);
        } else {
            rgp = (ReachabilityGraphPanel) graph.getParent().getParent().getParent();
            if (rgp.getRefreshButtonEnabled()) {
                ReachabilityWarning.showRefreshWarning(null);
                return;
            }
            ;
            doSingleClick(cell);
        }
        e.consume();
    }

    private void doSingleClick(DefaultGraphCell cell) {
        if (cell != null && cell instanceof ReachabilityPlaceModel) {
            ReachabilityPlaceModel place = (ReachabilityPlaceModel) cell;
            if (!place.getHighlight()) {
                // remove all highlights and virtual tokens
                deHighlightModel(false);
                deHighlightEdges();
                // highlight the edges
                highlightEdges(place);
                // set the virtual tokens for the current place(marking)
                setTokens(place);
                // highlight the transitions in the petrinet for the current marking
                highlightTransitions(place);
                // highlight the place/marking in the RG
                place.setHighlight(true);
                rgp.setUnselectButtonEnabled(true);
                editor.setReadOnly(false);
                // repaint the instance of the editor
                editor.getGraph().repaint();
                // repaint the graph
                graph.clearSelection();
                graph.getGraphLayoutCache().reload();
            }
        } else
            if (cell != null && cell instanceof ReachabilityEdgeModel) {
                deHighlightModel(false);
                ReachabilityEdgeModel edge = (ReachabilityEdgeModel) cell;
                ReachabilityPlaceModel place = (ReachabilityPlaceModel) ((ReachabilityPortModel) edge.getTarget())
                        .getParent();
                deHighlightEdges();
                highlightEdges(place);
                // set the virtual tokens for the current place(marking)
                setTokens(place);
                // set color of clicked place
                highlightTransitions(place);
                place.setHighlight(true);
                this.lastHighlighted = place;
                rgp.setUnselectButtonEnabled(true);
                editor.setReadOnly(false);
                // repaint the instance of the editor
                editor.getGraph().repaint();
                // repaint the graph
                graph.clearSelection();
                graph.getGraphLayoutCache().reload();
            }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void deHighlightModel(boolean doRepaint) {

        if (this.lastHighlighted != null) {
            this.lastHighlighted.setHighlight(false);
        }
        ((PetriNetModelProcessor) editor.getModelProcessor()).resetRGHighlightAndVTokens();
        editor.setReadOnly(true);
        deHighlightEdges();
        if (doRepaint) {
            // repaint the instance of the editor
            editor.getGraph().repaint();
            graph.getGraphLayoutCache().reload();
        }
    }

    private void highlightTransitions(ReachabilityPlaceModel place) {
        IMarking ma = (IMarking) place.getUserObject();
        Iterator<String> netIt = ma.getActivatedTransitions().iterator();
        while (netIt.hasNext()) {
            TransitionModel trans = (TransitionModel) getPetriNet().getElementContainer().getElementById(netIt.next());
            trans.setRGHighlighted(true);
        }
    }

    private void setTokens(ReachabilityPlaceModel place) {

        IMarking marking = (IMarking) place.getUserObject();
        SortedMap<String, Integer> map = marking.getMarking();
        for (Iterator<AbstractElementModel> placeIter = getPetriNet().getElementContainer().getElementsByType(
                AbstractPetriNetModelElement.PLACE_TYPE).values().iterator(); placeIter.hasNext();) {
            try {
                PlaceModel curplace = (PlaceModel) placeIter.next();
                curplace.setVirtualTokens(map.get(curplace.getId()));
            } catch (Exception ex) {
            }
        }
    }

    private PetriNetModelProcessor getPetriNet() {
        return (PetriNetModelProcessor) editor.getModelProcessor();
    }

    private void deHighlightEdges() {
        Map editMap = new HashMap();
        if (lastHighlightedEdge != null) {
            lastHighlightedEdge.setIngoing(false);
            lastHighlightedEdge.setOutgoing(false);
            editMap.put(lastHighlightedEdge, lastHighlightedEdge.getAttributes());
            this.lastHighlightedEdge = null;
        }

        if (lastHighlighted != null) {
            ReachabilityPortModel port = (ReachabilityPortModel) this.lastHighlighted.getChildAt(0);
            Set<ReachabilityEdgeModel> edges = port.getEdges();
            Iterator<ReachabilityEdgeModel> iterEdges = edges.iterator();
            while (iterEdges.hasNext()) {
                ReachabilityEdgeModel edge = iterEdges.next();
                if (edge.getSource().equals(port) || edge.getTarget().equals(port)) {
                    edge.setIngoing(false);
                    edge.setOutgoing(false);
                    editMap.put(edge, edge.getAttributes());
                }
            }
        }

        if (editMap.size() > 0) {
            graph.getGraphLayoutCache().edit(editMap, null, null, null);
        }
    }

    private void highlightClickedEdge(ReachabilityEdgeModel edge) {
        Map editMap = new HashMap();
        GraphConstants.setLineColor(edge.getAttributes(), Color.magenta);
        GraphConstants.setLineWidth(edge.getAttributes(), 2);
        editMap.put(edge, edge.getAttributes());
        graph.getGraphLayoutCache().edit(editMap);
        this.lastHighlightedEdge = edge;
    }

    private void highlightEdges(ReachabilityPlaceModel place) {
        ReachabilityPortModel port = (ReachabilityPortModel) place.getChildAt(0);
        Set<ReachabilityEdgeModel> edges = port.getEdges();
        Iterator<ReachabilityEdgeModel> iterEdges = edges.iterator();
        Map editMap = new HashMap();
        while (iterEdges.hasNext()) {
            ReachabilityEdgeModel edge = iterEdges.next();
            edge.setOutgoing(false);
            edge.setIngoing(false);
            if (edge.getSource().equals(port)) {
                edge.setOutgoing(true);
                editMap.put(edge, edge.getAttributes());
            } else
                if (edge.getTarget().equals(port)) {
                    edge.setIngoing(true);
                    editMap.put(edge, edge.getAttributes());
                }
        }
        this.lastHighlighted = place;
        if (editMap.size() > 0) {
            graph.getGraphLayoutCache().edit(editMap, null, null, null);
        }
    }

}
