package org.woped.qualanalysis.coverabilitygraph.gui.views;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphModel;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseListener;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphSettings;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphVC;
import org.woped.qualanalysis.coverabilitygraph.gui.ZoomController;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class defines the public functionality of an coverability graph view that can be registered in a {@link CoverabilityGraphVC}.
 */
public abstract class CoverabilityGraphView extends JPanel {

    private MarkingHighlighter markingHighlighter;

    protected CoverabilityGraphView(){
        markingHighlighter = new MarkingHighlighter();
    }

    /**
     * Refreshes the view.
     */
    public abstract void refresh();

    /**
     * Resets the view to its initial state.
     */
    public abstract void reset();

    /**
     * Notifies the view that it is out of sync with the petri net.
     */
    public abstract void setOutOfSync();

    /**
     * Gets the current settings of the view.
     *
     * @return the current settings of the view
     */
    public abstract CoverabilityGraphSettings getSettings();

    /**
     * Applies the provided settings to the view.
     *
     * @param settings the new settings
     */
    public abstract void applySettings(CoverabilityGraphSettings settings);

    /**
     * Gets the zoom controller of the view.
     *
     * @return the zoom controller of the view
     */
    public abstract ZoomController getZoomController();

    /**
     * Gets the graph model of the view.
     *
     * @return the graph model of the view
     */
    public abstract CoverabilityGraphModel getGraphModel();

    /**
     * Highlight all markings which covers the provide marking.
     *
     * @param marking the marking to highlight
     */
    public void highlightMarking(IMarking marking){
        markingHighlighter.addHighlight(marking);
    }

    /**
     * Removes all highlighting from the view.
     */
    public void removeHighlighting(){
        markingHighlighter.removeHighlighting();
    }

    /**
     * Adds a listener that is interested in mouse events from the graph.
     *
     * @param listener the listener to add
     */
    public abstract void addCoverabilityGraphMouseListener(CoverabilityGraphMouseListener listener);

    /**
     * This class adds a highlight to all nodes based on their marking
     */
    private class MarkingHighlighter {
        private Color DARK_GREEN = new Color(0, 150, 50);
        private Color LIGHT_GREEN = new Color(180, 255, 180);
        private Map<DefaultGraphCell, AttributeMap> undoMap;

        MarkingHighlighter(){
            undoMap  = new HashMap<>();
        }

        /**
         * Adds a highlight to all nodes whose marking is larger of equal the provided marking.
         *
         * @param marking the marking to highlight
         */
        void addHighlight(IMarking marking){
            removeHighlighting();

            Collection<CoverabilityGraphNode> nodes = getGraphModel().getNodes();
            Collection<CoverabilityGraphNode> coveringNodes = new LinkedList<>();


            for(CoverabilityGraphNode node: nodes){
                if(marking.lessOrEqual(node.getMarking())) coveringNodes.add(node);
            }

            Map<DefaultGraphCell, AttributeMap> highlights = new HashMap<>();
            for(CoverabilityGraphNode node : coveringNodes){
                AttributeMap changedAttributes = new AttributeMap();
                AttributeMap undoAttributes = new AttributeMap();
                Collection<String> removeAttributes = new LinkedList<>();

                Color background = GraphConstants.getBackground(node.getAttributes());
                if(background == null){
                    removeAttributes.add(GraphConstants.BACKGROUND);
                } else {
                    GraphConstants.setBackground(undoAttributes, background);
                }

                Color lineColor = GraphConstants.getLineColor(node.getAttributes());
                if(lineColor == null){
                    removeAttributes.add(GraphConstants.LINECOLOR);
                } else
                {
                    GraphConstants.setLineColor(undoAttributes, lineColor);
                }

                if(!GraphConstants.isOpaque(node.getAttributes())){
                    GraphConstants.setOpaque(undoAttributes, false);
                    GraphConstants.setOpaque(changedAttributes, true);
                }

                GraphConstants.setRemoveAttributes(undoAttributes, removeAttributes.toArray());
                GraphConstants.setBackground(changedAttributes, LIGHT_GREEN);
                GraphConstants.setLineColor(changedAttributes, DARK_GREEN);

                highlights.put(node, changedAttributes);
                undoMap.put(node, undoAttributes);
            }

            getGraphModel().getGraph().getGraphLayoutCache().edit(highlights);
        }

        /**
         * Removes all highlighting added highlights.
         */
        void removeHighlighting(){
            getGraphModel().getGraph().getGraphLayoutCache().edit(undoMap);
            undoMap.clear();
        }
    }
}
