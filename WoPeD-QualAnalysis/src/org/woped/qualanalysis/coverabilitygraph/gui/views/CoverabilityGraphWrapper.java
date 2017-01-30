package org.woped.qualanalysis.coverabilitygraph.gui.views;

import org.jgraph.JGraph;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.events.*;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.coverabilitygraph.gui.ZoomController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This class wraps an {@link CoverabilityGraph} and provides additional functionality like zooming or scrolling to it.
 */
public class CoverabilityGraphWrapper extends JScrollPane {

    private final GraphMouseEventTrigger mouseEventTrigger;
    private ZoomController zoom;
    private JPanel wrapper;
    private CoverabilityGraph graph;

    /**
     * Constructs a new coverability graph view.
     *
     * @param graph the graph to display
     */
    public CoverabilityGraphWrapper(CoverabilityGraph graph) {
        super();
        this.graph = graph;
        createView();

        this.zoom = new GraphZoomController(graph, 0.25, 2.0, 0.125);
        wrapper.addMouseWheelListener(new ZoomListener());

        mouseEventTrigger = new GraphMouseEventTrigger();
        graph.addMouseListener(mouseEventTrigger);
        wrapper.addMouseListener(mouseEventTrigger);

        mouseEventTrigger.addListener(new GraphMouseEventListener());
    }

    /**
     * Refreshes the graph view.
     */
    public void refresh() {

        graph.validate();
        graph.repaint();

        this.validate();
        this.repaint();
    }

    /**
     * Gets the zoom controller of the graph.
     *
     * @return the zoom controller of the graph.
     */
    public ZoomController getZoomController() {
        return zoom;
    }

    /**
     * Adds a listener that is interested in mouse events from the graph.
     *
     * @param listener the listener to add
     */
    public void addCoverabilityGraphMouseListener(CoverabilityGraphMouseListener listener) {
        mouseEventTrigger.addListener(listener);
    }

    private void createView() {
        // The graph is wrapped with an panel to place it in the center of the view
        wrapper = new JPanel();
        wrapper.setBackground(graph.getBackground());
        wrapper.setLayout(new GridBagLayout());
        wrapper.add(graph);

        this.setViewportView(wrapper);
    }

    /**
     * Encapsulates zooming logic
     */
    private class GraphZoomController implements ZoomController {

        private final double ZOOM_STEP;
        private final Double ZOOM_MIN;
        private final Double ZOOM_MAX;
        private final JGraph graph;

        GraphZoomController(JGraph graph, double min, double max, double step) {
            ZOOM_MIN = min;
            ZOOM_MAX = max;
            ZOOM_STEP = step;

            this.graph = graph;
        }

        @Override
        public void zoomIn() {
            zoomIn(1, getGraphCenter());
        }

        @Override
        public void zoomIn(int steps, Point2D center) {
            double scale = graph.getScale();
            setZoom(scale + steps * ZOOM_STEP, center);
        }

        @Override
        public void zoomOut() {
            zoomOut(1, getGraphCenter());
        }

        @Override
        public void zoomOut(int steps, Point2D center) {
            double scale = graph.getScale();
            setZoom(scale - steps * ZOOM_STEP, center);
        }

        @Override
        public void setZoom(double factor) {
            Point2D center = getGraphCenter();
            setZoom(factor, center);
        }

        @Override
        public void setZoom(double factor, Point2D center) {
            factor = Math.max(factor, ZOOM_MIN);
            factor = Math.min(factor, ZOOM_MAX);
            graph.setScale(factor, center);
        }

        private Point2D getGraphCenter() {
            Dimension size = graph.getSize();
            return new Point2D.Double(size.getWidth() / 2, size.getHeight() / 2);
        }
    }

    /**
     * Enables zooming per mouse wheel
     */
    private class ZoomListener extends MouseAdapter {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            Point2D point = new Point2D.Double(e.getX(), e.getY());
            int turns = e.getWheelRotation() * -1;

            if (turns < 0) {
                getZoomController().zoomOut(Math.abs(turns), point);
            } else {
                getZoomController().zoomIn(turns, point);
            }
        }

    }

    /**
     * Listens on mouse events on the graph and notifies listeners about related events.
     */
    private class GraphMouseEventTrigger extends MouseAdapter {

        private Collection<CoverabilityGraphMouseListener> listeners;

        GraphMouseEventTrigger() {
            listeners = new LinkedList<>();
        }

        void addListener(CoverabilityGraphMouseListener listener) {
            this.listeners.add(listener);
        }

        void fireNodeClickedEvent(CoverabilityGraphNode node, int clickCount) {
            NodeClickedEvent event = new NodeClickedEvent(CoverabilityGraphWrapper.this, node, clickCount);

            for (CoverabilityGraphMouseListener listener : listeners) {
                listener.nodeClicked(event);
            }
        }

        void fireEdgeClickedEvent(CoverabilityGraphEdge edge, int clickCount) {
            EdgeClickedEvent event = new EdgeClickedEvent(CoverabilityGraphWrapper.this, edge, clickCount);

            for (CoverabilityGraphMouseListener listener : listeners) {
                listener.edgeClicked(event);
            }
        }

        void fireEmptySpaceClickedEvent(int clickCount) {
            EmptySpaceClickedEvent event = new EmptySpaceClickedEvent(CoverabilityGraphWrapper.this, clickCount);

            for (CoverabilityGraphMouseListener listener : listeners) {
                listener.emptySpaceClicked(event);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            // check if click was outside of graph in wrapper panel
            if (!(e.getSource() instanceof CoverabilityGraph)) fireEmptySpaceClickedEvent(e.getClickCount());

            // do not send events if graph is disabled
            if(!graph.isEnabled()) return;

            Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());

            if (cell == null)
                fireEmptySpaceClickedEvent(e.getClickCount());

            else if (cell instanceof CoverabilityGraphNode)
                fireNodeClickedEvent((CoverabilityGraphNode) cell, e.getClickCount());

            else if (cell instanceof CoverabilityGraphEdge)
                fireEdgeClickedEvent((CoverabilityGraphEdge) cell, e.getClickCount());

            e.consume();
        }
    }

    /**
     * Listens on mouse events outside the graph area
     */
    private class GraphMouseEventListener extends CoverabilityGraphMouseAdapter {
        @Override
        public void emptySpaceClicked(EmptySpaceClickedEvent event) {
            graph.clearSelection();
        }
    }
}



