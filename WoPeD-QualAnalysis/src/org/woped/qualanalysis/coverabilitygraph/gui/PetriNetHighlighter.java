package org.woped.qualanalysis.coverabilitygraph.gui;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseAdapter;
import org.woped.qualanalysis.coverabilitygraph.events.EmptySpaceClickedEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeClickedEvent;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * This class adds highlighting to the petri net if an marking is selected in the coverability graph.
 * The tokens are set according to the selected marking.
 */
public class PetriNetHighlighter {
    private final Color TRANSITION_BACKGROUND = new Color(188, 235, 253);
    private final GraphMouseListener mouseListener;

    private IEditor editor;
    private Map<TransitionModel, AttributeMap> modifiedTransitions;

    /**
     * Constructs a new petri net highlighter for the provided editor.
     *
     * @param editor the editor or the petri net to highlight
     */
    public PetriNetHighlighter( IEditor editor) {
        this.editor = editor;
        modifiedTransitions = new HashMap<>();
        this.mouseListener  = new GraphMouseListener();
    }

    /**
     * Removes all highlighting from the petri net.
     */
    public void removeHighlighting() {
        AttributeMap map = new AttributeMap();
        GraphConstants.setRemoveAttributes(map, new Object[]{GraphConstants.BACKGROUND});
        for (TransitionModel transition : modifiedTransitions.keySet()) {
            modifiedTransitions.put(transition, map);
        }
        editor.getGraph().getGraphLayoutCache().edit(modifiedTransitions);
        modifiedTransitions.clear();

        editor.getModelProcessor().resetVirtualTokens();
        refresh();
    }

    /**
     * Sets the provided marking in the petri net and highlights the enabled transitions.
     *
     * @param marking the marking to set
     */
    public void setMarking(IMarking marking) {

        removeHighlighting();
        setTokens(marking);

        highlightTransitions(marking);

        refresh();
    }

    /**
     * Gets the mouse listener that automatically highlight the petri net based on user interactions on the coverability graph.
     *
     * @return the mouse listener
     */
    public GraphMouseListener getMouseListener() {
        return mouseListener;
    }

    private void setTokens(IMarking marking) {
        ModelElementContainer container = editor.getModelProcessor().getElementContainer();
        SortedSet<PlaceNode> places = marking.getPlaces();
        int UNBOUND = 1000;

        for (PlaceNode place : places) {
            PlaceModel placeModel = (PlaceModel) container.getElementById(place.getOriginId());
            int tokens = marking.isPlaceUnbound(place) ? UNBOUND : marking.getTokens(place);
            placeModel.setVirtualTokens(tokens);
        }
    }

    private void highlightTransitions(IMarking marking) {

        ModelElementContainer container = editor.getModelProcessor().getElementContainer();
        Set<String> activatedTransitions = marking.getActivatedTransitions();

        for (String transitionId : activatedTransitions) {
            TransitionModel transitions = (TransitionModel) container.getElementById(transitionId);
            AttributeMap map = new AttributeMap();
            GraphConstants.setBackground(map, TRANSITION_BACKGROUND);
            modifiedTransitions.put(transitions, map);
        }

        editor.getGraph().getGraphLayoutCache().edit(modifiedTransitions);
    }

    private void refresh() {
        editor.getGraph().getGraphLayoutCache().reload();
        editor.repaint();
    }

    /**
     * Sets/Removes highlighting according to the user interaction on the coverability graph
     */
    private class GraphMouseListener extends CoverabilityGraphMouseAdapter {

        @Override
        public void emptySpaceClicked(EmptySpaceClickedEvent event) {
            removeHighlighting();
        }

        @Override
        public void nodeClicked(NodeClickedEvent event) {
            setMarking(event.getNode().getMarking());
        }
    }
}
