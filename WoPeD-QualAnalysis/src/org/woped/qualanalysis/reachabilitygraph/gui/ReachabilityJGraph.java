/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.gui;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.soundness.marking.IMarking;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Vector;

public class ReachabilityJGraph extends JGraph {

    private static final long serialVersionUID = 5078494841225380858L;

    // AttributeMap of graph
    private HashMap<String, String> graphAttributes = null;

    private ReachabilityGraphVC graphVC = null;

    public ReachabilityJGraph() {
        super(new DefaultGraphModel());
        initAttributeMap();
        ToolTipManager.sharedInstance().registerComponent(this);
    }


    public ReachabilityJGraph(GraphModel model, GraphLayoutCache view) {
        this(model, view, null);
    }

    public ReachabilityJGraph(GraphModel model, GraphLayoutCache view, ReachabilityGraphVC controller) {
        super(model, view);
        this.graphVC = controller;
        initAttributeMap();

        ToolTipManager.sharedInstance().registerComponent(this);
    }

    public ReachabilityGraphVC graphController() {
        return graphVC;
    }

    /**
     * returns the tooltip message for the element under the mouse cursor.
     */
    @Override
    public String getToolTipText(MouseEvent event) {
        Object cell = getFirstCellForLocation(event.getX(), event.getY());
        if (cell instanceof ReachabilityPlaceModel) {
            return ((ReachabilityPlaceModel) cell).getToolTipText();
        }
        if (cell instanceof ReachabilityEdgeModel) {
            return ((ReachabilityEdgeModel) cell).getToolTipText();
        }
        return null;
    }

    /**
     * returns all cells, that are at position x,y.
     *
     * @param x
     * @param y
     * @return
     */
    public Vector<Object> getAllCellsForLocation(double x, double y) {
        Object cell = getFirstCellForLocation(x, y);
        Object topMostCell = cell;
        Vector<Object> allCells = new Vector<Object>();
        allCells.add(cell);

        for (cell = getNextCellForLocation(cell, x, y); cell != topMostCell; cell = getNextCellForLocation(cell, x, y)) {
            allCells.add(cell);
        }
        return allCells;
    }

    /**
     * initializes the attributeMap with default values.
     */
    private void initAttributeMap() {
        graphAttributes = new HashMap<String, String>();
        graphAttributes.put("reachabilityGraph.place.width", "80");
        graphAttributes.put("reachabilityGraph.place.height", "20");
        graphAttributes.put("reachabilityGraph.color", "false");
        graphAttributes.put("reachabilityGraph.parallel", "true");
        graphAttributes.put("reachabilityGraph.hierarchic.verticalSpace", "80");
        graphAttributes.put("reachabilityGraph.hierarchic.horizontalSpace", "25");
        graphAttributes.put("coverabilityGraph.MarkingNotation", "MultiSet");

        this.setAntiAliased(true);
    }

    /**
     * returns the attributeMap
     *
     * @return
     */
    public HashMap<String, String> getAttributeMap() {
        return graphAttributes;
    }

    /**
     * sets a attributeMap
     *
     * @param graphAttributes
     */
    public void setAttributeMap(HashMap<String, String> graphAttributes) {
        this.graphAttributes = graphAttributes;
    }

    public void deHighlight() {
        GraphModel model = getModel();
        for (int j = 0; j < model.getRootCount(); j++) {
            if (model.getRootAt(j) instanceof ReachabilityPlaceModel) {
                ((ReachabilityPlaceModel) model.getRootAt(j)).setHighlight(false);
            } else if (model.getRootAt(j) instanceof ReachabilityEdgeModel) {
                ((ReachabilityEdgeModel) model.getRootAt(j)).setIngoing(false);
                ((ReachabilityEdgeModel) model.getRootAt(j)).setOutgoing(false);
            }
        }
        getGraphLayoutCache().reload();
        clearSelection();
    }

    /**
     * Highlights the in mark given marking(place) in the Reachabilty Graph. If the place is not in the visible range the graph will scroll automatically
     *
     * @param mark
     * @author <a href="mailto:b.joerger@gmx.de">Benjamin Joerger</a>
     * @since 02.01.2009
     */
    public void highlightMarking(IMarking mark) {
        deHighlight();
        GraphModel model = getModel();
        for (int j = 0; j < model.getRootCount(); j++) {
            if (model.getRootAt(j) instanceof ReachabilityPlaceModel) {

                ReachabilityPlaceModel rpm = (ReachabilityPlaceModel) model.getRootAt(j);
                if (mark.toString().equals(((IMarking) rpm.getUserObject()).toString())) {
                    rpm.setHighlight(true);
                    Rectangle2D re = GraphConstants.getBounds(rpm.getAttributes());
                    this.getVisibleRect();
                    JScrollBar scb = ((JScrollPane) getParent().getParent()).getVerticalScrollBar();
                    if (scb.isVisible()) {
                        // autoscroll down
                        while ((re.getMaxY() * this.scale) > (scb.getValue() + scb.getBounds().height)) {
                            scb.setValue(scb.getValue() + (scb.getBounds().height / 2));
                        }
                        // autoscroll up
                        while ((re.getMinY() * this.scale) < scb.getValue()) {
                            scb.setValue(scb.getValue() - (scb.getBounds().height / 2));
                        }
                    }
                    scb = ((JScrollPane) getParent().getParent()).getHorizontalScrollBar();
                    if (scb.isVisible()) {
                        // autoscroll right
                        while ((re.getMaxX() * this.scale) > (scb.getValue() + scb.getBounds().width)) {
                            scb.setValue(scb.getValue() + (scb.getBounds().width / 2));
                        }
                        // autoscroll left
                        while ((re.getMinX() * this.scale) < scb.getValue()) {
                            scb.setValue(scb.getValue() - (scb.getBounds().width / 2));
                        }
                    }
                }
            } else if (model.getRootAt(j) instanceof ReachabilityEdgeModel) {
                // ((ReachabilityEdgeModel)model.getRootAt(j)).setIngoing(false);
                // ((ReachabilityEdgeModel)model.getRootAt(j)).setOutgoing(false);
            }
        }
        getGraphLayoutCache().reload();
        clearSelection();
    }
}
