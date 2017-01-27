package org.woped.qualanalysis.coverabilitygraph.gui;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * This class is in charge for displaying the coverability graph.
 */
public class CoverabilityGraph extends JGraph {

    private static final long serialVersionUID = 5078494841225380858L;

    /**
     * Constructs a new coverability graph using the default graph model.
     */
    public CoverabilityGraph() {
        this(new DefaultGraphModel(), null);
    }

    /**
     * Constructs a new coverability graph using the provided model and view.
     *
     * @param model the model to display
     * @param view the view to display the model in
     */
    public CoverabilityGraph(GraphModel model, GraphLayoutCache view) {
        super(model, view);

        initAttributeMap();
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    @Override
    public Rectangle2D getCellBounds(Object[] cells) {
        if (cells != null && cells.length > 0) {
            Rectangle2D r = getCellBounds(cells[0]);
            Rectangle2D ret = (r != null) ? (Rectangle2D) r.clone() : null;
            for (int i = 1; i < cells.length; i++) {
                r = getCellBounds(cells[i]);
                if (r != null) {
                    if (ret == null)
                        ret = (Rectangle2D) r.clone();
                    else
                        Rectangle2D.union(ret, r, ret);
                }
            }
            return ret;
        }
        return null;
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        Object cell = getFirstCellForLocation(event.getX(), event.getY());
        if (cell instanceof CoverabilityGraphNode) {
            return ((CoverabilityGraphNode) cell).getToolTipText();
        }
        if (cell instanceof CoverabilityGraphEdge) {
            return ((CoverabilityGraphEdge) cell).getToolTipText();
        }
        return super.getToolTipText(event);
    }

    private void initAttributeMap() {
        this.setAntiAliased(true);
    }
}
